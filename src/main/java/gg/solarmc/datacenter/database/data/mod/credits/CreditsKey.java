package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.SingleDataConstants;
import gg.solarmc.datacenter.database.data.DataKey;

import java.util.HashMap;
import java.util.Map;

public class CreditsKey implements DataKey<Credits> {
    public static CreditsKey INSTANCE = new CreditsKey();

    private final Map<String, Credits> cache = new HashMap<>();
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
        return new Credits(center, uuid);
    }

    @Override
    public Map<String, Credits> getCache() {
        return cache;
    }

    public SingleDataConstants getConstants() {
        return constants;
    }
}
