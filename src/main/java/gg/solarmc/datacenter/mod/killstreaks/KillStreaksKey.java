package gg.solarmc.datacenter.mod.killstreaks;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.util.HashMap;
import java.util.Map;

public class KillStreaksKey implements SingleDataKey<KillStreaks, Integer> {
    public static final KillStreaksKey INSTANCE = new KillStreaksKey();

    private final Map<String, Integer> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private KillStreaksKey() {
        constants = new SingleDataConstants("killstreaks", new Column("streak", "INTEGER", "0"));
    }

    @Override
    public String getName() {
        return "killstreaks";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery();
    }

    @Override
    public KillStreaks getData(DataCenter center, String uuid) {
        return new KillStreaks(center, uuid, cache.get(uuid));
    }

    @Override
    public void updateCache(String uuid, Integer value) {
        cache.put(uuid, value);
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }
}
