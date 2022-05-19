package gg.solarmc.datacenter.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.util.HashMap;
import java.util.Map;

public class RewardsKey implements SingleDataKey<Rewards, String> {
    public static RewardsKey INSTANCE = new RewardsKey();

    private final Map<String, String> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private RewardsKey() {
        constants = new SingleDataConstants("rewards_cooldown", new Column("claims_expiry", "TEXT", "'-'"));
    }

    @Override
    public String getName() {
        return "rewards";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery();
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
    public void updateCache(String uuid, String value) {
        cache.put(uuid, value);
    }
}
