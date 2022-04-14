package gg.solarmc.datacenter.database.data.mod.credits;

import gg.solarmc.datacenter.database.DataCenter;
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

        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(CreditsConstants.SELECT_BALANCE_QUERY)
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    value = result.getDouble(CreditsConstants.BALANCE);
                    CreditsKey.INSTANCE.getCache().put(uuid, this);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(CreditsConstants.INSERT_PLAYER)) {
                insert.setString(1, uuid);
                insert.execute();

                CreditsKey.INSTANCE.getCache().put(uuid, this);
                return 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database#getBalance caught an exception");
        }
    }

    @Override
    public void set(Double value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null!");
        this.value = value;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        update(CreditsConstants.SET_BALANCE_QUERY, value);
    }

    public void add(double balance) {
        value += balance;
        CreditsKey.INSTANCE.getCache().put(uuid, this);

        update(CreditsConstants.ADD_BALANCE_QUERY, value);
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
                try (PreparedStatement insert = connection.prepareStatement(CreditsConstants.INSERT_PLAYER_WITH_BALANCE)) {
                    insert.setString(1, uuid);
                    insert.setDouble(2, balance);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database#updateBalance caught an exception:", e);
        }
    }
}
