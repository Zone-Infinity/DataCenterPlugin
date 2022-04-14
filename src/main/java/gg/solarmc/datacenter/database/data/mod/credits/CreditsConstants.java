package gg.solarmc.datacenter.database.data.mod.credits;

public class CreditsConstants {
    CreditsConstants() {
    }

    public static final String CREDITS_TABLE = "credits";
    public static final String ID = "id";
    public static final String UUID = "uuid";
    public static final String BALANCE = "balance";

    public static final String CREATE_CREDITS_TABLE_QUERY =
            String.format("CREATE TABLE IF NOT EXISTS %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s VARCHAR(36) NOT NULL," +
                            "%s NUMERIC(15, 3) NOT NULL DEFAULT 0" +
                            ");",
                    CREDITS_TABLE, ID, UUID, BALANCE);

    public static final String INSERT_PLAYER =
            String.format("INSERT INTO %s (%s) VALUES (?)",
                    CREDITS_TABLE, UUID);

    public static final String INSERT_PLAYER_WITH_BALANCE =
            String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                    CREDITS_TABLE, UUID, BALANCE);

    public static final String SELECT_BALANCE_QUERY =
            String.format("SELECT %s FROM %s WHERE %s = ?",
                    BALANCE, CREDITS_TABLE, UUID);

    public static final String SET_BALANCE_QUERY =
            String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                    CREDITS_TABLE, BALANCE, UUID);

    public static final String ADD_BALANCE_QUERY =
            String.format("UPDATE %s SET %s = MAX(0, %s + ?) WHERE %s = ?",
                    CREDITS_TABLE, BALANCE, BALANCE, UUID);
}
