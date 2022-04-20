package gg.solarmc.datacenter.config;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface DatabaseConfig {

    @ConfKey("host")
    @ConfDefault.DefaultString("localhost")
    String host();

    @ConfKey("port")
    @ConfDefault.DefaultInteger(3306)
    int port();

    @ConfKey("database")
    @ConfDefault.DefaultString("datacenter")
    String database();

    @ConfKey("user")
    @ConfDefault.DefaultString("root")
    String user();

    @ConfKey("password")
    @ConfDefault.DefaultString("1234")
    String password();
}
