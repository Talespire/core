package studio.talespire.core.tablist.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.TabListService;

/**
 * @author Disunion
 * @date 6/7/2024
 */
public class PlayerLoginListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Universe.get(TabListService.class).initializeTabList(event.getPlayer());
    }
}
