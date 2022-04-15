package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;

import java.time.Instant;

public class MonthlyRewards extends SingleData<Long> {
    private final static long month = 2592000000L;

    public MonthlyRewards(DataCenter center, String uuid, Long value) {
        super(center, uuid, value);
    }

    @Override
    public Long get() {
        if (value != null) return value;

        value = getValue(MonthlyRewardsKey.INSTANCE.getConstants(), 0L, Long.class);
        MonthlyRewardsKey.INSTANCE.getCache().put(uuid, value);
        return value;
    }

    @Override
    public void set(Long value) {
        this.value = value;
        MonthlyRewardsKey.INSTANCE.getCache().put(uuid, value);
        setValue(MonthlyRewardsKey.INSTANCE.getConstants(), value);
    }

    public boolean isAvailable() {
        return Instant.now().toEpochMilli() - get() > month;
    }

    public String getFormatted() {
        if (isAvailable()) return "0";

        long milliseconds = value + month;
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        long days = milliseconds / (1000 * 60 * 60 * 24);

        StringBuilder builder = new StringBuilder();
        if (milliseconds < 1000) {
            return milliseconds + " milliseconds";
        } else {
            if (days > 0)
                builder.append(days).append(" day, ");
            if (hours > 0)
                builder.append(hours).append(" hour, ");
            if (minutes > 0)
                builder.append(minutes).append(" minute, ");
            if (seconds > 0)
                builder.append(seconds).append(" second, ");
        }
        builder.setLength(builder.length() - 2);

        int comma = builder.lastIndexOf(",");
        builder.replace(comma, comma + 1, " and");

        return builder.toString();
    }

    public void setCoolDownNow() {
        set(Instant.now().getEpochSecond());
    }
}
