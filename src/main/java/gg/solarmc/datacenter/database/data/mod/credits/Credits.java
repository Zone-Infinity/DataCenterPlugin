package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.SingleData;
import gg.solarmc.datacenter.database.data.SingleDataConstants;

import java.util.Map;

public class Credits extends SingleData<Double> {
    public Credits(DataCenter center, String uuid) {
        super(center, uuid);
        value = 0.0;
    }

    @Override
    public Double get() {
        Map<String, Credits> cache = CreditsKey.INSTANCE.getCache();
        if (cache.containsKey(uuid)) return cache.get(uuid).value;

        value = getValue(CreditsKey.INSTANCE.getConstants(), 0.0, Double.class);
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        return value;
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");
        this.value = value;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        setValue(CreditsKey.INSTANCE.getConstants(), value);
    }

    public void add(double balance) {
        value += balance;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        SingleDataConstants constants = CreditsKey.INSTANCE.getConstants();
        setValueWithQuery(String.format("UPDATE %s SET %s = MAX(0, %s + ?) WHERE %s = ?",
                        constants.getTableName(), constants.getValueName(), constants.getValueName(), constants.getUUIDName()),
                constants, value);
    }

    public void remove(double balance) {
        add(-balance);
    }
}
