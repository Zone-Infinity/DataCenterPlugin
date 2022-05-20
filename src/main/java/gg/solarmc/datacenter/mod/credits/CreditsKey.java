package gg.solarmc.datacenter.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.util.HashMap;
import java.util.Map;

public class CreditsKey implements SingleDataKey<Credits, Double> {
    public static final CreditsKey INSTANCE = new CreditsKey();

    private final Map<String, Double> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private CreditsKey() {
        constants = new SingleDataConstants("credits", new Column("balance", "NUMERIC(15, 3)", "0"));
    }

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery();
    }

    @Override
    public Credits getData(DataCenter center, String uuid) {
        Double value = cache.get(uuid);
        System.out.println(value);
        return new Credits(center, uuid, value);
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }

    @Override
    public void updateCache(String uuid, Double value) {
        System.out.println(value);
        cache.put(uuid, value);
    }
}
