package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.multiple.MultipleData;

public abstract class SingleData<T> extends MultipleData {
    protected T value;

    public SingleData(DataCenter center, String uuid, T value) {
        super(center, uuid, null);
        this.value = value;
    }

    public abstract T get();

    public abstract void set(T value);

    protected abstract SingleDataKey<? extends SingleData<T>, T> getDataKey();

    protected final T getValue() {
        T got = getValue(getDataKey().getConstants().getValueColumn());
        getDataKey().updateCache(uuid, got);
        return got;
    }

    protected final void setValue(T value) {
        setValue(getDataKey().getConstants().getValueColumn(), value);
        getDataKey().updateCache(uuid, value);
    }
}
