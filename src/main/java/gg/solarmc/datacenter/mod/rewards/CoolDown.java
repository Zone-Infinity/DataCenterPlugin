package gg.solarmc.datacenter.mod.rewards;

import java.time.Instant;
import java.util.ArrayList;
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
        return getCoolDownInString("D", "H", "M", "S", "ms");
    }

    public String getPretty() {
        String formatted = getCoolDownInString("day(s),", "hour(s),", "minute(s),", "second(s),", "milliseconds");

        if (formatted.contains("milli")) return formatted;

        formatted = formatted.substring(0, formatted.length() - 2);
        int comma = formatted.lastIndexOf(",");
        return formatted.substring(0, comma) + " and" + formatted.substring(comma + 1);
    }

    public String getCoolDownInString(String d, String h, String m, String s, String ms) {
        if (isAvailable()) return "0";

        long milliseconds = getCoolDown();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        long days = milliseconds / (1000 * 60 * 60 * 24);

        StringBuilder builder = new StringBuilder();
        if (milliseconds < 1000) {
            return milliseconds + " " + ms;
        } else {
            if (days > 0)
                builder.append(days).append(" ").append(d).append(" ");
            if (hours > 0)
                builder.append(hours).append(" ").append(h).append(" ");
            if (minutes > 0)
                builder.append(minutes).append(" ").append(m).append(" ");
            if (seconds > 0)
                builder.append(seconds).append(" ").append(s).append(" ");
        }

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
        if (value.equals("-")) return new ArrayList<>();
        return Arrays.stream(value.split(","))
                .map(it -> {
                    String[] coolDown = it.split("=");
                    return new CoolDown(coolDown[0], Long.parseLong(coolDown[1]));
                })
                .collect(Collectors.toList());
    }
}
