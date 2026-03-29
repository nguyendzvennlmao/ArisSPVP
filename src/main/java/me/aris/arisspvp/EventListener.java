package me.aris.arisspvp;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class EventListener implements Listener {

    private final ArisSPVP plugin;

    public EventListener(ArisSPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Material item = event.getItemInHand().getType();
        if (plugin.getConfigManager().getBlockedMaterials().contains(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;
        Material result = event.getRecipe().getResult().getType();
        if (plugin.getConfigManager().getBlockedMaterials().contains(result)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.MINECART_TNT) {
            if (isInBlockedRegion(event.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.MINECART_TNT && event.getEntity() instanceof Player) {
            if (isInBlockedRegion(event.getDamager().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isInBlockedRegion(org.bukkit.Location loc) {
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            return query.getApplicableRegions(BukkitAdapter.adapt(loc)).getRegions().stream()
                    .anyMatch(region -> plugin.getConfigManager().getBlockedRegions().contains(region.getId()));
        } catch (Exception e) {
            return false;
        }
    }
  }
