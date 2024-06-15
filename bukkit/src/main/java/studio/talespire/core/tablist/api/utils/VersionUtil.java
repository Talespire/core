package studio.talespire.core.tablist.api.utils;

import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.NoArgsConstructor;
import com.comphenix.protocol.ProtocolLibrary;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@NoArgsConstructor
public class VersionUtil {
    private static final MinecraftVersion tablistBreakingChange = MinecraftVersion.getCurrentVersion();
    public static boolean isNewTablist() {
        return ProtocolLibrary.getProtocolManager().getMinecraftVersion().isAtLeast(tablistBreakingChange);
    }
}
