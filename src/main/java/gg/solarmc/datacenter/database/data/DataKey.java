package gg.solarmc.datacenter.database.data;

import gg.solarmc.datacenter.database.DataCenter;

public interface DataKey<T extends Data> {
    String getName();

    String getCreateQuery();

    T getData(DataCenter center, String uuid);
}
