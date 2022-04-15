package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;

public class MonthlyRewards extends SingleData<Long> {

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
}
