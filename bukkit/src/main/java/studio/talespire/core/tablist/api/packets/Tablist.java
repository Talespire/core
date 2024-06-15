package studio.talespire.core.tablist.api.packets;

import lombok.Getter;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Getter
public class Tablist {
    private final PacketSender packet;

    protected Tablist(TablistTemplate template) {
        this.packet = new TabListPacket(template);
    }
}
