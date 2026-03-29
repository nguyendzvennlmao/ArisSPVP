package me.aris.arisspvp;

import org.bukkit.plugin.java.JavaPlugin;

public class ArisSPVP extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
