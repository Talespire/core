package studio.talespire.core.profile.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.talespire.core.profile.utils.BukkitProfileUtils;

public class JoinMessageListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Component joinMessage = BukkitProfileUtils.getRankedNameLoaded(event.getPlayer().getUniqueId())
                        .append(Component.text(" joined!", NamedTextColor.GOLD));

        event.joinMessage(joinMessage);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        Component leaveMessage = BukkitProfileUtils.getRankedNameLoaded(event.getPlayer().getUniqueId())
                        .append(Component.text(" left!", NamedTextColor.GOLD));

        event.quitMessage(leaveMessage);
    }
}