package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Moose1301
 * @date 5/25/2024
 */
@Command(names = {"friend","friends"})
public class FriendCommand {

    @Command(names = "list")
    public void handleList(Player player) {

    }
}
