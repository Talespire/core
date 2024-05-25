package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.uuid.UUIDCacheService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 5/25/2024
 */
@Command(names = {"friend","friends"})
public class FriendCommand {

    @Children(names = "list", async = true)
    public void handleList(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if(profile == null) {
            return;
        }
        player.sendMessage(Component.text("You have " + profile.getFriends().size() + " friends"));
        ProfileService profileService=  Universe.get(ProfileService.class);
        for (UUID uuid : profile.getFriends().stream().sorted((o1, o2) -> Boolean.compare(profileService.isPlayerServerCached(o1), profileService.isPlayerServerCached(o2))).toList()) {
            try {
                String name = Universe.get(UUIDCacheService.class).get().getName(uuid).get(1, TimeUnit.SECONDS);
                String serverId = profileService.getPlayerServer(uuid);
                if(serverId == null) {
                    player.sendMessage(name + " is offline since ");
                } else {
                    player.sendMessage(name + " is on " + serverId);
                }

            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
    @Children(names = "", async = true)
    public void handle(Player player, @Param(name = "UUID") UUID targetId) {

    }
}
