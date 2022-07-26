package gg.solarmc.datacenter.database.data.multiple;

import gg.solarmc.datacenter.database.data.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleDataConstants {
    protected final String tableName;
    protected final String id;
    protected final String uuid;
    protected final List<Column<?>> columns;

    public MultipleDataConstants(String tableName, Column<?>... valueColumn) {
        this.tableName = tableName;
        this.id = "id";
        this.uuid = "uuid";
        this.columns = Arrays.asList(valueColumn);
    }

    public String createTableQuery() {
        String columns = this.columns.stream().map(it -> it.getName() + " " + it.getType()
                        + " NOT NULL DEFAULT " + it.getDefaultValue())
                .collect(Collectors.joining(",\n"));
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTO_INCREMENT," +
                        "%s VARCHAR(36) NOT NULL," +
                        columns +
                        ");",
                tableName, id, uuid
        );
    }

    public String insertPlayerQuery() {
        return String.format("INSERT INTO %s (%s) VALUES (?)", tableName, uuid);
    }

    public String insertPlayerWithValuesQuery(Column<?>... columns) {
        if (columns.length == 0)
            throw new IllegalArgumentException("Specify columns!");
        if (!new HashSet<>(this.columns).containsAll(Arrays.asList(columns)))
            throw new IllegalArgumentException("One of the columns doesn't exist in this table!");

        String sqlFormats = String.join(", ", Collections.nCopies(columns.length + 1, "?"));
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, uuid + ", " + convertToQuery(", ", columns), sqlFormats);
    }

    public String selectValueQuery(Column<?>... columns) {
        if (columns.length == 0)
            throw new IllegalArgumentException("Specify columns!");
        return String.format("SELECT %s FROM %s WHERE %s = ?", convertToQuery(", ", columns), tableName, uuid);
    }

    public String setValueQuery(Column<?>... columns) {
        if (columns.length == 0)
            throw new IllegalArgumentException("Specify columns!");
        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, convertToQuery(" = ?, ", columns) + " = ?", uuid);
    }

    private String convertToQuery(String delimiter, Column<?>... columns) {
        if (columns.length == 0)
            throw new IllegalArgumentException("Specify columns!");
        return Arrays.stream(columns).map(Column::getName).collect(Collectors.joining(delimiter));
    }

    public String getTableName() {
        return tableName;
    }

    public String getUUIDName() {
        return uuid;
    }

    public List<String> getColumnNames() {
        return columns.stream().map(Column::getName).collect(Collectors.toList());
    }
}
