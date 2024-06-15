package studio.talespire.core.tablist.api.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.config.TablistConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistRemovePlayerPacket implements PacketSender {
    private final PacketContainer packet;
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public TablistRemovePlayerPacket(List<Player> playersToRemove) {
        this.packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        List<UUID> uuidList = playersToRemove.stream().map(Player::getUniqueId).collect(Collectors.toList());
        packet.getLists(Converters.passthrough(UUID.class)).write(0, uuidList);
    }

    public TablistRemovePlayerPacket(Player player) {
        this(Collections.singletonList(player));
    }

    @Override
    public void sendPacketOnce(Player player) throws InvocationTargetException {
        protocolManager.sendServerPacket(player, packet);
    }
}
