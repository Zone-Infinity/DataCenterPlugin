package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;

public class Credits extends SingleData<Double> {
    public Credits(DataCenter center, String uuid, Double value) {
        super(center, uuid, value);
    }

    @Override
    public Double get() {
        if (value != null) return value;

        value = getValue(CreditsKey.INSTANCE.getConstants(), 0.0,
                it -> it.getDouble(CreditsKey.INSTANCE.getConstants().getValueName()));
        CreditsKey.INSTANCE.updateCache(uuid, value);
        return value;
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");

        this.value = value;
        CreditsKey.INSTANCE.updateCache(uuid, value);
        setValue(CreditsKey.INSTANCE.getConstants(), value);
    }

    public void add(double balance) {
        if (value == null) get();

        value += balance;
        set(value);
    }

    public void remove(double balance) {
        add(-balance);
    }
}
