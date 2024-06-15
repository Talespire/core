package studio.talespire.core.tablist.api.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.config.StaticConfiguration;
import studio.talespire.core.tablist.api.utils.LatencyEnum;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Getter
@NoArgsConstructor
public class TablistConfig implements StaticConfiguration {

    private final Tablist tablist = new Tablist();

    @Getter
    public static class Tablist {
        private final boolean tablistPerWorld = true;
        private final boolean useRealLatency = false;
        private final LatencyEnum defaultLatency = LatencyEnum.ONE;
        private final boolean fillWithFakePlayers = true;
        private final int fillUntil = 21;
    }

}
