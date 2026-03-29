package me.aris.arisspvp;

import org.bukkit.Material;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {
    private final ArisSPVP plugin;

    public ConfigManager(ArisSPVP plugin) {
        this.plugin = plugin;
    }

    public List<String> getBlockedRegions() {
        return plugin.getConfig().getStringList("settings.blocked-tnt-cart-regions");
    }

    public Set<Material> getBlockedMaterials() {
        return plugin.getConfig().getStringList("settings.blocked-items").stream()
                .map(Material::matchMaterial)
                .collect(Collectors.toSet());
    }
}
