package studio.talespire.core.tablist.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.TablistService;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.bukkit.utils.TablistUtil;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistManager implements Listener {
    private final TablistConfig config = Universe.get(TablistConfig.class);
    private final TablistService manager;

    public TablistManager(TablistService manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        updateTabList(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateTabList(event.getPlayer());
    }

    private void updateTabList(Player player) {
        TablistUtil.updatePlayerTablist(this.manager, this.config, player);
    }


}
