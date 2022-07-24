package gg.solarmc.datacenter.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

public class Credits extends SingleData<Double> {
    private long lastAccessed = 0;

    public Credits(DataCenter center, String uuid, Double value) {
        super(center, uuid, value);
    }

    @Override
    public Double get() {
        if (value != null && System.currentTimeMillis() - this.lastAccessed < 30000) return value;

        value = getValue(Double.class);
        this.lastAccessed = System.currentTimeMillis();
        return value;
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");

        this.value = value;
        setValue(value);
        this.lastAccessed = System.currentTimeMillis();
    }

    public void add(double balance) {
        // if (value == null) get();
        value = getValue(Double.class);
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
