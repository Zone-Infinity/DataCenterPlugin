package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.data.DataKey;

public interface SingleDataKey<T extends SingleData<K>, K> extends DataKey<T> {
    void updateCache(String uuid, K value);

    SingleDataConstants getConstants();
}
