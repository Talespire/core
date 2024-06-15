package studio.talespire.core.tablist.api.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.TablistService;
import studio.talespire.core.tablist.api.config.TablistConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class PlayerInfoListener extends PacketAdapter {
    private final TablistConfig config = Universe.get(TablistConfig.class);

    public PlayerInfoListener(TablistService service, ListenerPriority listenerPriority, PacketType... types) {
        super(service.getPlugin(), listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player destinationPlayer = event.getPlayer();
        PacketContainer packetContainer = event.getPacket();
        var action = packetContainer.getPlayerInfoActions().read(0);

        if (action.contains(EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT) || action.contains(EnumWrappers.PlayerInfoAction.UPDATE_LISTED)) {
            return;
        }

        List<PlayerInfoData> playerInfoDataList = packetContainer.getPlayerInfoDataLists().read(1);
        List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<>();
        if (config.getTablist().isTablistPerWorld()) {
            for (PlayerInfoData data : playerInfoDataList) {
                Player dataPlayer = Bukkit.getPlayer(data.getProfile().getName());
                if (dataPlayer != null) {
                    if (dataPlayer.getWorld().equals(destinationPlayer.getWorld())) {
                        if (config.getTablist().isUseRealLatency()) {
                            newPlayerInfoDataList.add(data);
                            continue;
                        }
                        PlayerInfoData newData = new PlayerInfoData(
                                data.getProfile(),
                                config.getTablist().getDefaultLatency().getLatency(),
                                data.getGameMode(),
                                data.getDisplayName()
                        );
                        newPlayerInfoDataList.add(newData);
                    }
                } else {
                    newPlayerInfoDataList.add(data);
                }
            }
        } else {
            for (PlayerInfoData data : playerInfoDataList) {
                if (config.getTablist().isUseRealLatency()) {
                    newPlayerInfoDataList.add(data);
                } else {
                    PlayerInfoData newData = new PlayerInfoData(
                            data.getProfile(),
                            config.getTablist().getDefaultLatency().getLatency(),
                            data.getGameMode(),
                            data.getDisplayName()
                    );
                }
            }
        }
        packetContainer.getPlayerInfoDataLists().write(1, newPlayerInfoDataList);
    }
}
