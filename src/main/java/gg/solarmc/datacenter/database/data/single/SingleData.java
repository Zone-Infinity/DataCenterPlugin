package gg.solarmc.datacenter.database.data.single;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SingleData<T> extends Data {
    protected T value;

    public SingleData(DataCenter center, String uuid, T value) {
        super(center, uuid);
        this.value = value;
    }

    public abstract T get();

    public abstract void set(T value);

    abstract <K extends SingleData<T>> SingleDataKey<K, T> getDataKey();

    protected final T getValue(SingleDataConstants constants, T defaultValue, Class<T> klass) {
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(constants.selectValueQuery())
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    T value = result.getObject(constants.getValueName(), klass);
                    getDataKey().updateCache(uuid, value);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerQuery())) {
                insert.setString(1, uuid);
                insert.executeUpdate();

                getDataKey().updateCache(uuid, defaultValue);
                return defaultValue;
            }
        } catch (SQLException e) {
            throw new RuntimeException("SingleData#getValue caught an exception", e);
        }
    }

    protected final void setValue(SingleDataConstants constants, T value, int sqlType) {
        try (Connection connection = center.getConnection();
             PreparedStatement update = connection.prepareStatement(constants.setValueQuery())
        ) {
            update.setObject(1, value, sqlType);
            update.setString(2, uuid);

            int rows = update.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerWithValueQuery())) {
                    insert.setString(1, uuid);
                    insert.setObject(2, value, sqlType);
                    insert.executeUpdate();

                    getDataKey().updateCache(uuid, value);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SingleData#setValue caught an exception:", e);
        }
    }
}
