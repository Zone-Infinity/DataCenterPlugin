package gg.solarmc.datacenter.mod.killstreaks;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.multiple.MultipleData;
import gg.solarmc.datacenter.database.data.multiple.MultipleDataKey;

import java.util.Map;

public class Kills extends MultipleData {
    public Kills(DataCenter center, String uuid, Map<Column<?>, Object> values) {
        super(center, uuid, values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Column<T> column) {
        Object value = values.get(column);
        if (value != null) return (T) value;

        value = getValue(column, column.getTypeClass());
        return (T) value;
    }

    @Override
    public <T> void set(Column<T> column, T value) {
        values.put(column, value);
        setValue(column, value);
    }

    @Override
    protected MultipleDataKey<? extends MultipleData> getDataKey() {
        return KillsKey.INSTANCE;
    }
}
