package gg.solarmc.datacenter.database.data;

public class Column {

    private final String name;
    // https://mariadb.com/kb/en/data-types/
    private final String type;
    private final String defaultValue;

    public Column(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
