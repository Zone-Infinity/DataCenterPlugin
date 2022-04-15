package gg.solarmc.datacenter.database.data;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;

/**
 * {@link SingleData} : Tables with single value e.g. balance
 * Multiple values will be made when needed
 */
public abstract class Data {
    protected final DataCenter center;
    protected final String uuid;

    public Data(DataCenter center, String uuid) {
        if (uuid == null) throw new IllegalArgumentException("uuid should not be null!");
        this.center = center;
        this.uuid = uuid;
    }
}
