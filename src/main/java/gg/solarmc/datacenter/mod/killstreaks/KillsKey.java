package gg.solarmc.datacenter.mod.killstreaks;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.Column;
import gg.solarmc.datacenter.database.data.multiple.MultipleDataConstants;
import gg.solarmc.datacenter.database.data.multiple.MultipleDataKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class KillsKey extends MultipleDataKey<Kills> {
    public static final KillsKey INSTANCE = new KillsKey();
    public static Column<Integer> TOP_KILL_STREAK;
    public static Column<Integer> KILLS;

    private final MultipleDataConstants constants;

    private KillsKey() {
        TOP_KILL_STREAK = new Column<>("top_streak", "INTEGER", Types.INTEGER, Integer.class, 0);
        KILLS = new Column<>("kills", "INTEGER", Types.INTEGER, Integer.class, 0);
        constants = new MultipleDataConstants("kills", KILLS, TOP_KILL_STREAK);
    }

    @Override
    public String getName() {
        return "kills";
    }

    @Override
    public String getCreateQuery() {
        return constants.createTableQuery();
    }

    @Override
    public Kills getData(DataCenter center, String uuid) {
        return new Kills(center, uuid, cache.get(uuid));
    }

    public Map<UUID, Integer> getTopKillStreaks(DataCenter center, int limit) {
        String topKillStreakColumnName = TOP_KILL_STREAK.getName();
        try (Connection connection = center.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(
                     "SELECT %s, %s FROM %s ORDER BY %s DESC LIMIT %s",
                     constants.getUUIDName(),
                     topKillStreakColumnName,
                     constants.getTableName(),
                     topKillStreakColumnName,
                     limit
             ))
        ) {
            Map<UUID, Integer> killStreaks = new LinkedHashMap<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    UUID uuid = UUID.fromString(result.getString(constants.getUUIDName()));
                    int streak = result.getInt(topKillStreakColumnName);
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
    public MultipleDataConstants getConstants() {
        return constants;
    }
}
