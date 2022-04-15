package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.data.DataKey;

import java.util.Map;

public interface SingleDataKey<T extends SingleData<K>, K> extends DataKey<T> {
    Map<String, K> getCache();

    SingleDataConstants getConstants();
}
