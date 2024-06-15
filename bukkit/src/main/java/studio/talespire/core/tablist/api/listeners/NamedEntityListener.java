package studio.talespire.core.tablist.api.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.CoreBukkit;
import studio.talespire.core.CorePlugin;
import studio.talespire.core.tablist.api.TablistService;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.api.packets.PacketSender;
import studio.talespire.core.tablist.api.packets.TablistAddPlayerPacket;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class NamedEntityListener extends PacketAdapter {
    private final TablistConfig config = Universe.get(TablistConfig.class);

    public NamedEntityListener(TablistService service, ListenerPriority listenerPriority, PacketType... types) {
        super(service.getPlugin(), listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (config.getTablist().isTablistPerWorld()) return;
        var packet = event.getPacket();
        var targetPlayer = event.getPlayer();
        var entityUUID = packet.getUUIDs().read(0);
        var packetPlayer = Bukkit.getPlayer(entityUUID);
        if (packetPlayer != null) {
            PacketSender tablistAddpacket = new TablistAddPlayerPacket(packetPlayer);
            PacketSender tablistAddpacket2 = new TablistAddPlayerPacket(targetPlayer);
            try {
                tablistAddpacket.sendPacketOnce(targetPlayer);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            try {
                tablistAddpacket2.sendPacketOnce(packetPlayer);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
