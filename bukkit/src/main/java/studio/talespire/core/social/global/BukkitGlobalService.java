package studio.talespire.core.social.global;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.ChatChannelService;
import studio.talespire.core.social.global.chat.DefaultChatChannel;

/**
 * @author Disunion
 * @date 5/17/2024
 */

@Getter
public class BukkitGlobalService {
    private final DefaultChatChannel channel = new DefaultChatChannel();

    public BukkitGlobalService(JavaPlugin plugin) {
        Universe.get(ChatChannelService.class).registerChatChannel(channel);
    }
}
