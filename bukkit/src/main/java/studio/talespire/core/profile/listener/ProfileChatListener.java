package studio.talespire.core.profile.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.rank.Rank;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
public class ProfileChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Profile profile = Universe.get(ProfileService.class).getProfile(event.getPlayer().getUniqueId());

        Component component = Component.text()
                .append(profile.getRank().getPrefix())
                .append(profile.getRank() == Rank.DEFAULT ? Component.empty() : Component.space())
                .append(Component.text("%1$s", profile.getRank().getColor()))
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text("%2$s"))
                .build();
        event.setFormat(
                LegacyComponentSerializer.legacySection().serialize(component)
        );
    }


}
