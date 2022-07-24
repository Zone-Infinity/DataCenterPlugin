package gg.solarmc.datacenter.database.data.multiple;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class MultipleData extends Data {
    protected Map<Column<?>, Object> values;

    public MultipleData(DataCenter center, String uuid, Map<Column<?>, Object> values) {
        super(center, uuid);
        this.values = values;
    }

    public <T> T get(Column<T> column) {
        throw new UnsupportedOperationException();
    }

    public <T> void set(Column<T> column, T value) {
        throw new UnsupportedOperationException();
    }

    protected abstract MultipleDataKey<? extends MultipleData> getDataKey();

    protected final <T> T getValue(Column<T> column, Class<T> klass) {
        MultipleDataConstants constants = getDataKey().getConstants();
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(constants.selectValueQuery(column))
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    T value = result.getObject(column.getName(), klass);
                    getDataKey().updateCache(uuid, column, value);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerQuery())) {
                insert.setString(1, uuid);
                insert.executeUpdate();

                T defaultValue = column.getDefaultValue();
                getDataKey().updateCache(uuid, column, defaultValue);
                return defaultValue;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // for better debugging
            throw new RuntimeException("SingleData#getValue caught an exception");
        }
    }

    protected final <T> void setValue(Column<T> column, T value) {
        MultipleDataConstants constants = getDataKey().getConstants();
        try (Connection connection = center.getConnection();
             PreparedStatement update = connection.prepareStatement(constants.setValueQuery(column))
        ) {
            int sqlType = column.getSqlType();
            update.setObject(1, value, sqlType);
            update.setString(2, uuid);

            int rows = update.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerWithValuesQuery(column))) {
                    insert.setString(1, uuid);
                    insert.setObject(2, value, sqlType);
                    insert.executeUpdate();
                }
            }
            getDataKey().updateCache(uuid, column, value);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SingleData#setValue caught an exception");
        }
    }
}
