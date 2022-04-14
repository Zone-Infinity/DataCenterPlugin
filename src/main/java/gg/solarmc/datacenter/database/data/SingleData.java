package gg.solarmc.datacenter.database.data;

import gg.solarmc.datacenter.database.DataCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SingleData<T> extends Data {
    protected T value;

    public SingleData(DataCenter center, String uuid) {
        super(center, uuid);
        value = null;
    }

    public abstract T get();

    public abstract void set(T value);

    protected final T getValue(SingleDataConstants constants, T defaultValue, Class<T> klass) {
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(constants.selectValueQuery())
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    value = result.getObject(constants.getValueName(), klass);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerQuery())) {
                insert.setString(1, uuid);
                insert.execute();

                return defaultValue;
            }
        } catch (SQLException e) {
            throw new RuntimeException("SingleData#get caught an exception");
        }
    }

    protected final void setValue(SingleDataConstants constants, T value) {
        setValueWithQuery(constants.setValueQuery(), constants, value);
    }

    protected final void setValueWithQuery(String query, SingleDataConstants constants, T value) {
        try (Connection connection = center.getConnection();
             PreparedStatement update = connection.prepareStatement(query)
        ) {
            update.setObject(1, value);
            update.setString(2, uuid);

            int rows = update.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerWithValueQuery())) {
                    insert.setString(1, uuid);
                    insert.setObject(2, value);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SingleData#update caught an exception:", e);
        }
    }
}
