package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.multiple.MultipleDataConstants;

public class SingleDataConstants<T> extends MultipleDataConstants {
    private final Column<T> value;

    public SingleDataConstants(String tableName, Column<T> valueColumn) {
        super(tableName, valueColumn);
        this.value = valueColumn;
    }

    public String getValueName() {
        return value.getName();
    }

    public Column<T> getValueColumn() {
        return value;
    }
}
