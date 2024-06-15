package studio.talespire.core.tablist.api.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.Reset;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.config.StaticConfiguration;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.tablist.api.utils.LatencyEnum;
import studio.talespire.core.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Getter
@NoArgsConstructor
public class TablistConfig implements StaticConfiguration {

    private final Tablist tablist = new Tablist();
    private final HeaderFooter headerFooter = new HeaderFooter();

    @Getter
    public static class Tablist {
        private final boolean tablistPerWorld = false;
        private final boolean useRealLatency = false;
        private final LatencyEnum defaultLatency = LatencyEnum.ONE;
        private final boolean fillWithFakePlayers = true;
        private final int fillUntil = 80;
    }

    @Getter
    public static class HeaderFooter {
        private final List<Component> header = new ArrayList<>(
                List.of(
                        MenuUtils.separator(100, NamedTextColor.YELLOW),
                        Component.newline(),
                        Component.text("Talespire ", NamedTextColor.AQUA, TextDecoration.BOLD).append(Component.text("| Fantasy MMO RPG", NamedTextColor.GRAY, TextDecoration.BOLD)),
                        Component.newline(),
                        Component.text("Online players: ", NamedTextColor.GRAY).append(Component.text(Bukkit.getOnlinePlayers().size(), NamedTextColor.WHITE)),
                        Component.newline(),
                        Component.text("Online Staff: ", NamedTextColor.GOLD).append(Component.text(getOnlineStaff(), NamedTextColor.YELLOW))
                ));
        private final List<Component> footer = new ArrayList<>(
                List.of(
                        Component.text("Visit our website: ", NamedTextColor.GRAY).append(Component.text("talespire.net", NamedTextColor.YELLOW)),
                        Component.newline(),
                        MenuUtils.separator(100, NamedTextColor.YELLOW)
                ));

        private int getOnlineStaff() {
            int staffOnline = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
                if (profile.getRank().isStaff()) {
                    staffOnline++;
                }
            }
            return staffOnline;
        }
    }

}
