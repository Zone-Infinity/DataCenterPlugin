package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.util.HashMap;
import java.util.Map;

public class RewardsKey implements SingleDataKey<Rewards, String> {
    public static RewardsKey INSTANCE = new RewardsKey();

    private final Map<String, String> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private RewardsKey() {
        constants = new SingleDataConstants("rewards", "claims_expiry");
    }

    @Override
    public String getName() {
        return "rewards";
    }

    @Override
    public String getCreateQuery() {
            return constants.createTableQuery("VARCHAR", "\"-\"");
    }

    @Override
    public Rewards getData(DataCenter center, String uuid) {
        return new Rewards(center, uuid, cache.get(uuid));
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }

    @Override
    public Map<String, String> getCache() {
        return cache;
    }
}
