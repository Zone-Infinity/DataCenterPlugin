package gg.solarmc.datacenter.mod.killstreaks;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.single.SingleDataConstants;
import gg.solarmc.datacenter.database.data.single.SingleDataKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillStreaksKey implements SingleDataKey<KillStreaks, Integer> {
    public static final KillStreaksKey INSTANCE = new KillStreaksKey();

    private final Map<String, Integer> cache = new HashMap<>();
    private final SingleDataConstants constants;

    private KillStreaksKey() {
        constants = new SingleDataConstants("kill_streaks", new Column("top_streak", "INTEGER", "0"));
    }

    @Override
    public String getName() {
        return "kill_streaks";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery();
    }

    @Override
    public KillStreaks getData(DataCenter center, String uuid) {
        return new KillStreaks(center, uuid, cache.get(uuid));
    }

    public Map<UUID, Integer> getTopKillStreaks(DataCenter center, int limit) {
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(
                     "SELECT %s, %s FROM %s ORDER BY %s DESC LIMIT %s",
                     constants.getUUIDName(),
                     constants.getValueName(),
                     constants.getTableName(),
                     constants.getValueName(),
                     limit
             ))
        ) {
            Map<UUID, Integer> killStreaks = new HashMap<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    UUID uuid = UUID.fromString(result.getString(constants.getUUIDName()));
                    int streak = result.getInt(constants.getValueName());
                    System.out.println(uuid + " " + streak);
                    killStreaks.put(uuid, streak);
                }
            }

            return killStreaks;
        } catch (SQLException e) {
            e.printStackTrace(); // for better debugging
            throw new RuntimeException("KillStreaksKey#getTopKillStreaks caught an exception");
        }
    }

    @Override
    public void updateCache(String uuid, Integer value) {
        cache.put(uuid, value);
    }

    @Override
    public SingleDataConstants getConstants() {
        return constants;
    }
}
