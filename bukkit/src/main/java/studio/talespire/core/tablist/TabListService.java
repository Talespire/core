package studio.talespire.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.talespire.core.tablist.listener.PlayerLoginListener;

/**
 * @author Disunion
 * @date 6/6/2024
 */
public class TabListService {

    public TabListService(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), plugin);
    }

    public void initializeTabList(Player player) {
        PlayerList playerList = new PlayerList(player, PlayerList.SIZE_FOUR);
        playerList.initTable();
        playerList.updateSlot(0, "Top left!");
        playerList.updateSlot(19, "Bottom left!");
        playerList.updateSlot(60, "Top right!");
        playerList.updateSlot(79, "Bottom right!");
    }

    public void removePlayer(Player player) {

    }
}
