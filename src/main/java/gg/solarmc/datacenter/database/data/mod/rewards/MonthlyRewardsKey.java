package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.DataKey;
import gg.solarmc.datacenter.database.data.SingleDataConstants;

import java.util.HashMap;
import java.util.Map;

public class MonthlyRewardsKey implements DataKey<MonthlyRewards> {
    public static MonthlyRewardsKey INSTANCE = new MonthlyRewardsKey();

    private final Map<String, MonthlyRewards> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private MonthlyRewardsKey() {
        constants = new SingleDataConstants("monthly_rewards", "last_claimed");
    }

    @Override
    public String getName() {
        return "monthly_rewards";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery("INTEGER", "0");
    }

    @Override
    public MonthlyRewards getData(DataCenter center, String uuid) {
        return new MonthlyRewards(center, uuid);
    }

    @Override
    public Map<String, MonthlyRewards> getCache() {
        return cache;
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }
}
