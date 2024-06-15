package studio.talespire.core.tablist.api.packets;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public interface PacketSender {
    void sendPacketOnce(Player player) throws InvocationTargetException;
}
