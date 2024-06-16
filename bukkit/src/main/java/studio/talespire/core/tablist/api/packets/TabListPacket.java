package studio.talespire.core.tablist.api.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import studio.talespire.core.tablist.api.utils.PlaceholdersUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TabListPacket implements PacketSender {

    private final TablistTemplate tablistTemplate;
    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private final PacketContainer packet;

    protected TabListPacket(TablistTemplate tablistTemplate) {
        this.packet = this.manager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        this.tablistTemplate = tablistTemplate;
    }

    @Override
    public void sendPacketOnce(Player player) {
        PlaceholdersUtil.compose(
                PlaceholdersUtil.getPlayerPlaceholders(), PlaceholdersUtil.getServerplaceholders(), this.tablistTemplate.getPlaceholderCallback()
        ).callback(this.tablistTemplate, player);
        this.packet.getChatComponents().write(0, WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(this.tablistTemplate.getHeader())));
        this.packet.getChatComponents().write(1, WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(this.tablistTemplate.getFooter())));
        this.manager.sendServerPacket(player, this.packet);
    }
}
