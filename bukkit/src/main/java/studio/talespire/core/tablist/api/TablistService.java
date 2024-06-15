package studio.talespire.core.tablist.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.api.listeners.BukkitListener;
import studio.talespire.core.tablist.api.listeners.NamedEntityListener;
import studio.talespire.core.tablist.api.listeners.PlayerInfoListener;
import studio.talespire.core.tablist.api.listeners.PlayerRemoveListener;
import studio.talespire.core.tablist.api.packets.TablistHandler;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Getter
public class TablistService {

    private final BukkitListener listener;
    private final TablistHandler handler;
    private final TablistConfig config = Universe.get(TablistConfig.class);

    public TablistService(JavaPlugin plugin) {
        handler = new TablistHandler(plugin);
        this.listener = new BukkitListener();
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PlayerRemoveListener(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO_REMOVE));
        manager.addPacketListener(new PlayerInfoListener(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO));
        manager.addPacketListener(new NamedEntityListener(this, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_ENTITY_SPAWN));

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void reload() {
        this.listener.reloadChanges();
    }
}
