package gg.solarmc.datacenter.database.data;

import gg.solarmc.datacenter.database.DataCenter;

public abstract class SingleData<T> extends Data {
    protected T value;

    public SingleData(DataCenter center, String uuid) {
        super(center, uuid);
        value = null;
    }

    public abstract T get();

    public abstract void set(T value);
}
