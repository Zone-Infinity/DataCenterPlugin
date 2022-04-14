package gg.solarmc.datacenter.database.data;

import gg.solarmc.datacenter.database.DataCenter;

import java.util.Map;

public interface DataKey<T extends Data> {
    String getName();

    String getCreateQuery();

    T getData(DataCenter center, String uuid);

    Map<String, T> getCache();

    SingleDataConstants getConstants();
}
