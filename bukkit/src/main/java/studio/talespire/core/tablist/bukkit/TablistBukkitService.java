package studio.talespire.core.tablist.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.TablistService;
import studio.talespire.core.tablist.bukkit.listeners.TablistManager;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistBukkitService {
    public TablistBukkitService(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new TablistManager(Universe.get(TablistService.class)), plugin);
    }
}
