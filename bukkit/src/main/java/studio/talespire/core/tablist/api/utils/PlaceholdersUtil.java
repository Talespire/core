package studio.talespire.core.tablist.api.utils;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.attribute.Attribute;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.tablist.api.packets.PlaceholderCallback;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@NoArgsConstructor
public class PlaceholdersUtil {

    /**
     * @return Callback related to the player that the tablist is going to show to
     */
    public static PlaceholderCallback getPlayerPlaceholders() {
        return (((tablistTemplate, player) -> {
            tablistTemplate.replace("%player_display_name%", Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getFormattedName());
            tablistTemplate.replace("%player_name%", LegacyComponentSerializer.legacyAmpersand().deserialize(player.getName()));
            tablistTemplate.replace("%player_health%", Component.text(player.getHealth()));
            tablistTemplate.replace("%player_max_health%", Component.text(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            tablistTemplate.replace("%player_world%", Component.text(player.getWorld().getName()));
            tablistTemplate.replace("%player_gamemode%", Component.text(player.getGameMode().toString()));
        }));
    }

    public static PlaceholderCallback getServerplaceholders() {
        return (((tablistTemplate, player) -> {
            tablistTemplate.replace("%server_players%", Component.text(player.getServer().getOnlinePlayers().size()));
            tablistTemplate.replace("%server_max_players%", Component.text(player.getServer().getMaxPlayers()));
            tablistTemplate.replace("%server_motd%", Component.text(player.getServer().getMotd()));
        }));
    }

    public static PlaceholderCallback compose(PlaceholderCallback... callbacks) {
        return ((tablistTemplate, player) -> {
            for (var placeholder : callbacks) {
                placeholder.callback(tablistTemplate, player);
            }
        });
    }
}
