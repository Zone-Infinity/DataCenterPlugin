package gg.solarmc.datacenter.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.sql.Types;

public class CreditsKey extends SingleDataKey<Credits, Double> {
    public static final CreditsKey INSTANCE = new CreditsKey();
    private final SingleDataConstants<Double> constants;

    private CreditsKey() {
        constants = new SingleDataConstants<>("credits", new Column<>("balance", "NUMERIC(15, 3)", Types.DOUBLE, 0.0));
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
        return new Credits(center, uuid, cache.get(uuid));
    }

    @Override
    public SingleDataConstants<Double> getConstants() {
        return constants;
    }
}
