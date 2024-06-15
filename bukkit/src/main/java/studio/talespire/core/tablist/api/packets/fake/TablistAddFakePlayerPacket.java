package studio.talespire.core.tablist.api.packets.fake;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.config.TablistConfig;

import java.util.Collections;
import java.util.EnumSet;
import java.util.UUID;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class TablistAddFakePlayerPacket extends FakePlayerPacket {
    protected final TablistConfig tablistConfig = Universe.get(TablistConfig.class);

    protected TablistAddFakePlayerPacket(UUID playerUuid, String playerName, Component displayText) {
        super(playerUuid, playerName, displayText);
        WrappedGameProfile gameProfile = new WrappedGameProfile(playerUuid, playerName);
        PacketContainer packet = this.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        setPacket(packet);
        var data = new PlayerInfoData(
                FakePlayerPacket.changeGameProfileSkin(gameProfile),
                tablistConfig.getTablist().getDefaultLatency().getLatency() + 1,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(displayText))
                );
        var infoLists = Collections.singletonList(data);
        EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME
        );
        packet.getPlayerInfoActions().write(0, actions);
        packet.getPlayerInfoDataLists().write(1, infoLists);
    }
}