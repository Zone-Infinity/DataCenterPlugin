package gg.solarmc.datacenter.database.data.mod.rewards;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoolDown {
    private final String key;
    private final Long expiry;

    public CoolDown(String key, Long expiry) {
        this.key = key;
        this.expiry = expiry;
    }

    public String getKey() {
        return key;
    }

    public Long getExpiryTime() {
        return expiry;
    }

    public Long getCoolDown() {
        return expiry - Instant.now().toEpochMilli();
    }

    public boolean isAvailable() {
        return expiry < Instant.now().toEpochMilli();
    }

    public String getFormatted() {
        if (isAvailable()) return "0";

        long milliseconds = getCoolDown();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        long days = milliseconds / (1000 * 60 * 60 * 24);

        StringBuilder builder = new StringBuilder();
        if (milliseconds < 1000) {
            return milliseconds + " milliseconds";
        } else {
            if (days > 0)
                builder.append(days).append(" day(s), ");
            if (hours > 0)
                builder.append(hours).append(" hour(s), ");
            if (minutes > 0)
                builder.append(minutes).append(" minute(s), ");
            if (seconds > 0)
                builder.append(seconds).append(" second(s), ");
        }
        builder.setLength(builder.length() - 2);

        int comma = builder.lastIndexOf(",");
        builder.replace(comma, comma + 1, " and");

        return builder.toString();
    }

    @Override
    public String toString() {
        return key + "=" + expiry;
    }

    public static String serialize(List<CoolDown> coolDowns) {
        return coolDowns.stream()
                .map(CoolDown::toString)
                .collect(Collectors.joining(","));
    }

    public static List<CoolDown> deserialize(String value) {
        return Arrays.stream(value.split(","))
                .map(it -> {
                    String[] coolDown = it.split("=");
                    return new CoolDown(coolDown[0], Long.parseLong(coolDown[1]));
                })
                .collect(Collectors.toList());
    }
}
