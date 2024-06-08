package studio.talespire.core.tablist.adapter;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.talespire.core.tablist.setup.TabEntry;

import java.util.List;

/**
 * @author Disunion
 * @date 6/7/2024
 */
public interface TabAdapter {

    /**
     * Gets the tab header for a player
     * @param player The player to get the header for
     * @return The header component
     */
    Component getHeader(Player player);

    /**
     * Gets the tab footer for a player
     * @param player The player to get the footer for
     * @return The footer component
     */
    Component getFooter(Player player);

    /**
     * Gets the tab lines for a player
     * @param player The player to get the lines for
     * @return The list of tab entries
     */
    List<TabEntry> getLines(Player player);
}
