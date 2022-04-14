package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.SingleDataConstants;
import gg.solarmc.datacenter.database.data.SingleData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Credits extends SingleData<Double> {
    public Credits(DataCenter center, String uuid) {
        super(center, uuid);
        value = 0.0;
    }

    @Override
    public Double get() {
        Map<String, Credits> cache = CreditsKey.INSTANCE.getCache();
        if (cache.containsKey(uuid)) return cache.get(uuid).value;

        SingleDataConstants constants = CreditsKey.INSTANCE.getConstants();
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(constants.selectValueQuery())
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    value = result.getDouble(constants.getValueName());
                    CreditsKey.INSTANCE.getCache().put(uuid, this);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerQuery())) {
                insert.setString(1, uuid);
                insert.execute();

                CreditsKey.INSTANCE.getCache().put(uuid, this);
                return 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Credits#get caught an exception");
        }
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");
        this.value = value;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        update(CreditsKey.INSTANCE.getConstants().setValueQuery(), value);
    }

    public void add(double balance) {
        value += balance;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        SingleDataConstants constants = CreditsKey.INSTANCE.getConstants();
        update(String.format("UPDATE %s SET %s = MAX(0, %s + ?) WHERE %s = ?",
                constants.getTableName(), constants.getValueName(), constants.getValueName(), constants.getUUIDName()), value);
    }

    public void remove(double balance) {
        add(-balance);
    }

    private void update(String query, double balance) {
        try (Connection connection = center.getConnection();
             PreparedStatement update = connection.prepareStatement(query)
        ) {
            update.setDouble(1, balance);
            update.setString(2, uuid);

            int rows = update.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement insert = connection.prepareStatement(CreditsKey.INSTANCE.getConstants().insertPlayerWithValueQuery())) {
                    insert.setString(1, uuid);
                    insert.setDouble(2, balance);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Credits#update caught an exception:", e);
        }
    }
}
