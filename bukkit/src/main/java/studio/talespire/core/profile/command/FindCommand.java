package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.uuid.UUIDCacheService;
import studio.talespire.core.profile.ProfileService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 5/22/2024
 */

public class FindCommand {
    @Command(names = "find", permission = "MOD", async = true)
    public void handleSeen(CommandSender sender, @Param(name = "Target")UUID targetId) {
        ProfileService service = Universe.get(ProfileService.class);
        if(!service.isPlayerServerCached(targetId)) {
            sender.sendMessage(Component.text("That player is not online!", NamedTextColor.RED));
            return;
        }
        String server = service.getPlayerServer(targetId);

        String username = null;
        try {
            username = Universe.get(UUIDCacheService.class).get().name(targetId).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage(Component.text(username + " is on " + server, NamedTextColor.GOLD));
    }
}
