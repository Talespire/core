package studio.talespire.core.placeholder.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.placeholder.PlaceholderService;

/**
 * @date 4/24/2024
 * @author Moose1301
 */
@Command(names = "placeholder", permission = "talespire.command.placeholder" )
public class PlaceholderCommand {

    @Children(names = "parse")
    public void handle(CommandSender sender, @Param(name = "Target Player") Player target, @Param(name = "Message", wildcard = true) String message) {
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(Universe.get(PlaceholderService.class).resolvePlaceholders(target, message))
        );
    }
}
