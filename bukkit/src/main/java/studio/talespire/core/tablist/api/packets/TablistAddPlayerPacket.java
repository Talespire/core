package studio.talespire.core.tablist.api.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.tablist.api.config.TablistConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistAddPlayerPacket implements PacketSender{
    private final PacketContainer packet;
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private final TablistConfig tablistConfig = Universe.get(TablistConfig.class);

    public TablistAddPlayerPacket(List<Player> playersToAdd) {
        this.packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        List<PlayerInfoData> playerInfoDataList = new ArrayList<>();
        for (Player player : playersToAdd) {
            playerInfoDataList.add(
                    new PlayerInfoData(WrappedGameProfile.fromPlayer(player),
                            tablistConfig.getTablist().getDefaultLatency().getLatency(),
                            EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                            WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getFormattedName()))
                            )
            );
        }
        EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT
        );
        this.packet.getPlayerInfoActions().write(0, actions);
        packet.getPlayerInfoDataLists().write(1, playerInfoDataList);
    }

    public TablistAddPlayerPacket(Player player) {
        this(Collections.singletonList(player));
    }

    @Override
    public void sendPacketOnce(Player player) {
        protocolManager.sendServerPacket(player, this.packet);
    }
}
