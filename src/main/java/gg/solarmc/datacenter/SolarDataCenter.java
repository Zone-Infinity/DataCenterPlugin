package gg.solarmc.datacenter;

import gg.solarmc.datacenter.config.ConfigManager;
import gg.solarmc.datacenter.config.DatabaseConfig;
import gg.solarmc.datacenter.database.DataCenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolarDataCenter extends JavaPlugin {
    private DataCenter center;

    @Override
    public void onEnable() {
        ConfigManager<DatabaseConfig> config = ConfigManager.create(getDataFolder().toPath(), "config.yml", DatabaseConfig.class);
        config.reloadConfig();
        DatabaseConfig databaseConfig = config.getConfigData();

        center = new DataCenter(databaseConfig, getLogger());

        getLogger().info("SolarDataCenter successfully enabled");
    }

    @Override
    public void onDisable() {
    }

    public DataCenter getDataCenter() {
        return center;
    }
}
