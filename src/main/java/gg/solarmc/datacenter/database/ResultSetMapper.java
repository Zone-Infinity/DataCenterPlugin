package gg.solarmc.datacenter.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<T> {

    T map(ResultSet set) throws SQLException;
}
