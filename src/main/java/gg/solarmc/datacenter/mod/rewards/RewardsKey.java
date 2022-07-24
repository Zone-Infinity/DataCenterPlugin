package gg.solarmc.datacenter.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.sql.Types;

public class RewardsKey extends SingleDataKey<Rewards, String> {
    public static final RewardsKey INSTANCE = new RewardsKey();
    private final SingleDataConstants<String> constants;

    private RewardsKey() {
        constants = new SingleDataConstants<>("rewards_cooldown", new Column<>("claims_expiry", "TEXT", Types.VARCHAR, String.class, "'-'"));
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
    public SingleDataConstants<String> getConstants() {
        return constants;
    }
}
