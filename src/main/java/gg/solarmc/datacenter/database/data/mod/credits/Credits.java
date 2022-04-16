package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;

public class Credits extends SingleData<Double> {
    public Credits(DataCenter center, String uuid, Double value) {
        super(center, uuid, value);
    }

    @Override
    public Double get() {
        if (value != null) return value;

        value = getValue(CreditsKey.INSTANCE.getConstants(), 0.0, Double.class);
        CreditsKey.INSTANCE.getCache().put(uuid, value);
        return value;
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");
        this.value = value;
        CreditsKey.INSTANCE.getCache().put(uuid, value);
        setValue(CreditsKey.INSTANCE.getConstants(), value);
    }

    public void add(double balance) {
        if (value == null) value = 0.0;
        value += balance;
        CreditsKey.INSTANCE.getCache().put(uuid, value);

        SingleDataConstants constants = CreditsKey.INSTANCE.getConstants();
        setValueWithQuery(constants.addNumericValueQuery(), constants, value);
    }

    public void remove(double balance) {
        add(-balance);
    }
}
