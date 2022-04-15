package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.util.HashMap;
import java.util.Map;

public class CreditsKey implements SingleDataKey<Credits, Double> {
    public static CreditsKey INSTANCE = new CreditsKey();

    private final Map<String, Double> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private CreditsKey() {
        constants = new SingleDataConstants("credits", "balance");
    }

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery("NUMERIC(15, 3)", "0");
    }

    @Override
    public Credits getData(DataCenter center, String uuid) {
        return new Credits(center, uuid, cache.get(uuid));
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }

    public Map<String, Double> getCache() {
        return cache;
    }
}
