package studio.talespire.core.tablist.api.packets.fake;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.Converters;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.UUID;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistRemoveFakePlayerPacket extends FakePlayerPacket{
    protected TablistRemoveFakePlayerPacket(UUID uuid, String playerName, Component displayText) {
        super(uuid, playerName, displayText);
        PacketContainer packet = this.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        packet.getLists(Converters.passthrough(UUID.class)).write(0, Collections.singletonList(uuid));
        this.setPacket(packet);
    }
}
