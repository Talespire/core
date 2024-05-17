package studio.talespire.core.chat;

import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.chat.command.ChatChannelCommands;
import studio.talespire.core.chat.command.QuickChatCommands;

/**
 * @author Disunion
 * @date 5/17/2024
 */
public class BukkitChatService {

    public BukkitChatService(JavaPlugin plugin) {
        CommandUtil.registerAll(
                new ChatChannelCommands(),
                new QuickChatCommands()
        );
    }
}
