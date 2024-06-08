package studio.talespire.core.tablist.setup;

import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import studio.lunarlabs.universe.util.Skin;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@Getter @Setter
@RequiredArgsConstructor
public class TabEntryInfo {

    private final UserProfile profile;
    private int ping = 0;
    private Skin skin = Skin.DEFAULT_SKIN;
    private Component prefix = Component.empty(), suffix = Component.empty();
    private WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo = null;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TabEntryInfo)) return false;
        if (o != this) return false;

        return ((TabEntryInfo) o).getProfile().equals(this.profile);
    }

    @Override
    public int hashCode() {
        return this.profile.getUUID().hashCode() + 645;
    }

}
