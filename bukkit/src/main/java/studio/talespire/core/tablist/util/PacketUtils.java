package studio.talespire.core.tablist.util;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.TabListService;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@UtilityClass
public class PacketUtils {
    public void sendPacket(Player target, PacketWrapper<?> packetWrapper) {
        PacketEventsAPI<?> packetEvents = Universe.get(TabListService.class).getPacketEvents();
        PlayerManager manager = packetEvents.getPlayerManager();
        manager.sendPacket(target, packetWrapper);
    }
}
