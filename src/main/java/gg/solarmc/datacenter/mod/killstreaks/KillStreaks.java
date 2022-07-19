package gg.solarmc.datacenter.mod.killstreaks;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.sql.Types;

public class KillStreaks extends SingleData<Integer> {
    public KillStreaks(DataCenter center, String uuid, Integer value) {
        super(center, uuid, value);
    }

    @Override
    public Integer get() {
        if (value != null) return value;

        value = getValue(0, Integer.class);
        return value;
    }

    @Override
    public void set(Integer value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");

        this.value = value;
        setValue(value, Types.INTEGER);
    }

    @Override
    protected SingleDataKey<? extends SingleData<Integer>, Integer> getDataKey() {
        return KillStreaksKey.INSTANCE;
    }
}
