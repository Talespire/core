package studio.talespire.core.tablist.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;
import studio.talespire.core.tablist.TabListService;
import studio.talespire.core.tablist.setup.TabLayout;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@RequiredArgsConstructor
public class TabListener implements Listener {

    private final TabListService instance;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        TabLayout layout = new TabLayout(player);

        layout.create();
        layout.setHeaderAndFooter();

        this.instance.getLayoutMap().put(player.getUniqueId(), layout);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Team team = player.getScoreboard().getTeam("ztab");
        if (team != null) {
            team.removeEntry(player.getName());
            team.unregister();
        }

        this.instance.getLayoutMap().remove(player.getUniqueId());
    }
}
