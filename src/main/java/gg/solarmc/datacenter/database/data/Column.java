package gg.solarmc.datacenter.database.data;

public class Column<T> {

    private final String name;
    // https://mariadb.com/kb/en/data-types/
    private final String type;
    private final int sqlType;
    private final T defaultValue;

    public Column(String name, String type, int sqlType, T defaultValue) {
        this.name = name;
        this.type = type;
        this.sqlType = sqlType;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getSqlType() {
        return sqlType;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
