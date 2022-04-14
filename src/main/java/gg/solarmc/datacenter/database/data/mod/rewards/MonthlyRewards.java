package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.SingleData;

import java.util.Map;

public class MonthlyRewards extends SingleData<Long> {

    public MonthlyRewards(DataCenter center, String uuid) {
        super(center, uuid);
    }

    @Override
    public Long get() {
        Map<String, MonthlyRewards> cache = MonthlyRewardsKey.INSTANCE.getCache();
        if (cache.containsKey(uuid)) return cache.get(uuid).value;

        value = getValue(MonthlyRewardsKey.INSTANCE.getConstants(), 0L, Long.class);
        MonthlyRewardsKey.INSTANCE.getCache().put(uuid, this);
        return value;
    }

    @Override
    public void set(Long value) {
        this.value = value;
        MonthlyRewardsKey.INSTANCE.getCache().put(uuid, this);
        setValue(MonthlyRewardsKey.INSTANCE.getConstants(), value);
    }
}
