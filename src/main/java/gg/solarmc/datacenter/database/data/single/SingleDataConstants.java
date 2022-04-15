package gg.solarmc.datacenter.database.data.single;

public class SingleDataConstants {
    private final String tableName;
    private final String id;
    private final String uuid;
    private final String value;

    public SingleDataConstants(String tableName, String value) {
        this.tableName = tableName;
        this.id = "id";
        this.uuid = "uuid";
        this.value = value;
    }

    /**
     * credits - NUMERIC(15, 4)
     * monthly_rewards - INTEGER
     */
    public String createTableQuery(String valueType, String defaultValue) {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s VARCHAR(36) NOT NULL," +
                        "%s %s NOT NULL DEFAULT %s" +
                        ");",
                tableName, id, uuid, value, valueType, defaultValue
        );
    }

    public String insertPlayerQuery() {
        return String.format("INSERT INTO %s (%s) VALUES (?)", tableName, uuid);
    }

    public String insertPlayerWithValueQuery() {
        return String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", tableName, uuid, value);
    }

    public String selectValueQuery() {
        return String.format("SELECT %s FROM %s WHERE %s = ?", value, tableName, uuid);
    }

    public String setValueQuery() {
        return String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, value, uuid);
    }

    public String addNumericValueQuery() {
        return String.format("UPDATE %s SET %s = MAX(0, %s + ?) WHERE %s = ?", tableName, value, value, uuid);
    }

    public String getTableName() {
        return tableName;
    }

    public String getUUIDName() {
        return uuid;
    }

    public String getValueName() {
        return value;
    }
}
