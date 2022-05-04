package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.data.Column;

public class SingleDataConstants {
    private final String tableName;
    private final String id;
    private final String uuid;
    private final Column value;

    public SingleDataConstants(String tableName, Column valueColumn) {
        this.tableName = tableName;
        this.id = "id";
        this.uuid = "uuid";
        this.value = valueColumn;
    }

    public String createTableQuery() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTO_INCREMENT," +
                        "%s VARCHAR(36) NOT NULL," +
                        "%s %s NOT NULL DEFAULT %s" +
                        ");",
                tableName, id, uuid, value.getName(), value.getType(), value.getDefaultValue()
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

    public String getTableName() {
        return tableName;
    }

    public String getUUIDName() {
        return uuid;
    }

    public String getValueName() {
        return value.getName();
    }
}
