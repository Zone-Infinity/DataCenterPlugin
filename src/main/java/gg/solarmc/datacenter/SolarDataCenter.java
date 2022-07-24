package gg.solarmc.datacenter;

import gg.solarmc.datacenter.config.ConfigManager;
import gg.solarmc.datacenter.config.DatabaseConfig;
import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.mod.credits.CreditsKey;
import gg.solarmc.datacenter.mod.killstreaks.KillsKey;
import gg.solarmc.datacenter.mod.rewards.RewardsKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolarDataCenter extends JavaPlugin {

    public static SolarDataCenter ins;

    private DataCenter center;

    @Override
    public void onEnable() {
        ins = this;
        ConfigManager<DatabaseConfig> config = ConfigManager.create(getDataFolder().toPath(), "config.yml", DatabaseConfig.class);
        config.reloadConfig();
        DatabaseConfig databaseConfig = config.getConfigData();

        center = new DataCenter(databaseConfig, getLogger());
        center.registerKey(CreditsKey.INSTANCE);
        center.registerKey(RewardsKey.INSTANCE);
        center.registerKey(KillsKey.INSTANCE);

        getLogger().info("SolarDataCenter successfully enabled");
    }

    @Override
    public void onDisable() {
    }

    public DataCenter getDataCenter() {
        return center;
    }
}
