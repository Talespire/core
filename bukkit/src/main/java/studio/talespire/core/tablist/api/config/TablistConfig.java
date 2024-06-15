package studio.talespire.core.tablist.api.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.config.StaticConfiguration;
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
                        Component.text("Talespire ", NamedTextColor.AQUA).append(Component.text("| Fantasy MMO RPG", NamedTextColor.GRAY))
                ));
        private final List<Component> footer = new ArrayList<>(
                List.of(
                        Component.text("Visit our website: ", NamedTextColor.GRAY).append(Component.text("talespire.net", NamedTextColor.YELLOW))
                ));
    }

}
