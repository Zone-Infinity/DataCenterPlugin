package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.SingleData;
import gg.solarmc.datacenter.database.data.SingleDataConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MonthlyRewards extends SingleData<Long> {

    public MonthlyRewards(DataCenter center, String uuid) {
        super(center, uuid);
    }

    @Override
    public Long get() {
        Map<String, MonthlyRewards> cache = MonthlyRewardsKey.INSTANCE.getCache();
        if (cache.containsKey(uuid)) return cache.get(uuid).value;

        SingleDataConstants constants = MonthlyRewardsKey.INSTANCE.getConstants();
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(constants.selectValueQuery())
        ) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    value = result.getLong(constants.getValueName());
                    MonthlyRewardsKey.INSTANCE.getCache().put(uuid, this);
                    return value;
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerQuery())) {
                insert.setString(1, uuid);
                insert.execute();

                MonthlyRewardsKey.INSTANCE.getCache().put(uuid, this);
                return 0L;
            }
        } catch (SQLException e) {
            throw new RuntimeException("MonthlyRewards#get caught an exception");
        }
    }

    @Override
    public void set(Long value) {
        SingleDataConstants constants = MonthlyRewardsKey.INSTANCE.getConstants();
        try (Connection connection = center.getConnection();
             PreparedStatement update = connection.prepareStatement(constants.setValueQuery())
        ) {
            update.setDouble(1, value);
            update.setString(2, uuid);

            int rows = update.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement insert = connection.prepareStatement(constants.insertPlayerWithValueQuery())) {
                    insert.setString(1, uuid);
                    insert.setDouble(2, value);
                    insert.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("MonthlyRewards#update caught an exception:", e);
        }
    }
}
