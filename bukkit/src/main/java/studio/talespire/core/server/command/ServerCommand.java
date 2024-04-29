package studio.talespire.core.server.command;

import me.andyreckt.raspberry.annotation.Command;
import org.bukkit.entity.Player;
import studio.talespire.core.server.menu.ServerListMenu;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class ServerCommand {
    @Command(names = "servers", permission = "admin", async = true)
    public void execute(Player player){
        new ServerListMenu().openAsync(player);
    }
}
