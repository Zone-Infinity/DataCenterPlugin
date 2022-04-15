package gg.solarmc.datacenter;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.mod.credits.CreditsKey;
import gg.solarmc.datacenter.database.data.mod.rewards.RewardsKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Alternative to DataLoader, not that good
 * Not able to figure out how to Launch DataLoader
 */

public final class SolarDataCenter extends JavaPlugin {
    private DataCenter center;

    @Override
    public void onEnable() {
        center = new DataCenter(getLogger(), getDataFolder().toPath());
        center.registerKey(CreditsKey.INSTANCE);
        center.registerKey(RewardsKey.INSTANCE);

        getLogger().info("SolarDataCenter successfully enabled");
    }

    @Override
    public void onDisable() {
    }

    public DataCenter getDataCenter() {
        return center;
    }
}
