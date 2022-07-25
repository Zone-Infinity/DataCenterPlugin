package gg.solarmc.datacenter.database.data.multiple;

import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.Data;
import gg.solarmc.datacenter.database.data.DataKey;

import java.util.HashMap;
import java.util.Map;

public abstract class MultipleDataKey<T extends Data> implements DataKey<T> {
    protected Map<String, Map<Column<?>, Object>> cache = new HashMap<>();

    public <K> void updateCache(String uuid, Column<K> column, K value) {
        cache.computeIfAbsent(uuid, it -> new HashMap<>()).put(column, value);
    }

    public abstract MultipleDataConstants getConstants();
}
