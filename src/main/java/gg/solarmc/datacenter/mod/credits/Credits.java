package gg.solarmc.datacenter.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.sql.Types;

public class Credits extends SingleData<Double> {
    public Credits(DataCenter center, String uuid, Double value) {
        super(center, uuid, value);
    }

    @Override
    public Double get() {
        if (value != null) return value;

        value = getValue(0.0, Double.class);
        return value;
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");

        this.value = value;
        setValue(value, Types.DOUBLE);
    }

    public void add(double balance) {
        if (value == null) get();

        value += balance;
        set(value);
    }

    public void remove(double balance) {
        add(-balance);
    }

    @Override
    protected SingleDataKey<? extends SingleData<Double>, Double> getDataKey() {
        return CreditsKey.INSTANCE;
    }
}
