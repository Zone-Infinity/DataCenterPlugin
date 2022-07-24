package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.data.multiple.MultipleDataKey;

import java.util.HashMap;
import java.util.Map;

public abstract class SingleDataKey<T extends SingleData<K>, K> extends MultipleDataKey<T> {
    protected Map<String, K> cache = new HashMap<>();

    public void updateCache(String uuid, K value) {
        cache.put(uuid, value);
    }

    public abstract SingleDataConstants<K> getConstants();
}
