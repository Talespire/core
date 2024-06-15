package studio.talespire.core.tablist.api.packets;

import org.bukkit.entity.Player;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@FunctionalInterface
public interface PlaceholderCallback {
    void callback(TablistTemplate tablistTemplate, Player player);
}
