package studio.talespire.core.tablist.api.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import studio.talespire.core.tablist.api.TablistService;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class PlayerRemoveListener extends PacketAdapter {
    public PlayerRemoveListener(TablistService service, ListenerPriority listenerPriority, PacketType... types) {
        super(service.getPlugin(), listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {}
}
