package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.chat.BukkitChatService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.utils.MenuUtils;

import java.util.Objects;
import java.util.UUID;


/**
 * @author Moose1301
 * @date 5/25/2024
 */
@Command(names = {"friend", "f"}, description = "Friend Commands")
public class FriendCommand {

    @Children(names = {"friend", "f", "add"}, async = true, description = "Add a friend")
    public void AddFriend(Player player, @Param(name = "Target") UUID target) {
        //-- Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target);

        if (targetProfile == null) {
            player.sendMessage(Component.text("No player found with that name!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        // If the player is already friends with the target, send a message to the player
        if (profile.getFriends().contains(target)) {
            player.sendMessage(Component.text("You are already friends with ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // If the player has already sent a friend request to the target, send a message to the player
        if (profile.getIncomingFriendRequests().contains(target)) {
            // Update the player's profile
            profile.getIncomingFriendRequests().remove(target);
            profile.getFriends().add(target);

            // Update the target's profile
            targetProfile.getOutGoingFriendRequests().remove(player.getUniqueId());
            targetProfile.getFriends().add(player.getUniqueId());

            player.sendMessage(Component.text("You already have a friend request from ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        targetProfile.getIncomingFriendRequests().add(player.getUniqueId());
        profile.getOutGoingFriendRequests().add(target);

        player.sendMessage(Component.text("You have sent a friend request to ", NamedTextColor.GREEN).append(BukkitProfileUtils.getRankedName(target)));
        //TODO: Send a message to the target
    }

    @Children(names = {"accept", "a"}, async = true, description = "Accept a friend request")
    public void handleAccept(Player player,  @Param(name = "Target") UUID target) {
        //-- Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target);

        // Check if the target's profile is null
        if (targetProfile == null) {
            player.sendMessage(Component.text("No player found with that name!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        // If the player has a friend request from the target, remove the target from the friend requests and add the target to the friends list
        if (profile.getIncomingFriendRequests().contains(target)) {
            // Get the profile and remove the target from the friend requests and add the target to the friends list
            profile.getIncomingFriendRequests().remove(target);
            profile.getFriends().add(target);

            // Get the target profile and remove the player from the friend requests and add the player to the friends list
            targetProfile.getOutGoingFriendRequests().remove(player.getUniqueId());
            targetProfile.getFriends().add(player.getUniqueId());

            //TODO: Send a message to the target

            // Send a message to the player
            player.sendMessage(Component.text("You are now friends with ", NamedTextColor.GREEN).append(BukkitProfileUtils.getRankedName(target)));

        } else {
            // Send a message to the player if they do not have a friend request from the target
            player.sendMessage(Component.text("You do not have a friend request from ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @Children(names = {"best", "b"}, async = true, description = "Add a friend")
    public void handleBest(Player player, @Param(name = "Target") UUID target) {
        //-- Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target);

        // Check if the target's profile is null
        if (targetProfile == null) {
            player.sendMessage(Component.text("No player found with that name!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        // Check if the player has them added as a friend
        if (profile.getFriends().contains(target)) {
            profile.getBestFriends().add(target);
            player.sendMessage(Component.text("You have added ", NamedTextColor.GREEN).append(BukkitProfileUtils.getRankedName(target)).append(Component.text(" to your best friends list", NamedTextColor.GREEN)));
        } else {
            // Otherwise send them a message that they aren't friends with that player
            player.sendMessage(Component.text("You are not friends with ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @Children(names = {"deny", "d"}, async = true, description = "Deny a friend request")
    public void handleDeny(Player player, @Param(name = "Target") UUID target) {
        //-- Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target);

        // Check if the target's profile is null
        if (targetProfile == null) {
            player.sendMessage(Component.text("No player found with that name!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        // If the player has a friend request from the target, remove the target from the friend requests
        if (profile.getIncomingFriendRequests().contains(target)) {
            // Get the profile and remove the target from the friend requests
            profile.getIncomingFriendRequests().remove(target);

            // Get the target profile and remove the player from the friend requests
            targetProfile.getOutGoingFriendRequests().remove(player.getUniqueId());

            // Send a message to the player
            player.sendMessage(Component.text("You have denied the friend request from ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
        } else {
            // Send a message to the player if they do not have a friend request from the target
            player.sendMessage(Component.text("You do not have a friend request from ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @Children(names = {"list", "l"}, async = true, description = "List all friends")
    public void handleList(Player player, @Param(name = "Page", baseValue = "1") int page) {
        //-- Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        int pagesOfFriends = (int) Math.ceil(profile.getFriends().size() / 8.0);

        Component message;

        if (page == pagesOfFriends) {
            message.append(Component.text("(Page " + page + " + " + pagesOfFriends + ") ", NamedTextColor.GOLD))
                .append(Component.text("« Friends", NamedTextColor.GOLD)
                        .clickEvent(ClickEvent.runCommand("/friend list " + (page - 1)))
                );
        } else if (page > 1) {
            message = Component.text("« Friends ", NamedTextColor.GOLD)
                    .clickEvent(ClickEvent.runCommand("/friend list " + (page - 1)))
                    .append(Component.text("Friends ", NamedTextColor.GOLD))
                    .append(Component.text("(Page " + page + " + " + pagesOfFriends + ") ", NamedTextColor.GOLD))
                    .append(
                            Component.text("»", NamedTextColor.GOLD)
                                    .clickEvent(ClickEvent.runCommand("/friend list " + (page + 1)))
                    );
        } else {
            message = Component.text("Friends ", NamedTextColor.GOLD)
                .append(Component.text("(Page " + page + " + " + pagesOfFriends + ") ", NamedTextColor.GOLD))
                .append(
                        Component.text("»", NamedTextColor.GOLD)
                                .clickEvent(ClickEvent.runCommand("/friend list " + (page + 1)))
                );
        }

        // List 8 of their friends, prioritizing friend that are online
        for (UUID friend : profile.getFriends()) {
            Profile friendProfile = Universe.get(ProfileService.class).getOrLoadProfile(friend);


        }

        Component toSend = Component.text()
            .append(MenuUtils.chatSeparator(NamedTextColor.BLUE))
            .append(Component.newline())
            .append(message)
            .append(Component.newline())
            .append(MenuUtils.chatSeparator(NamedTextColor.BLUE))
            .build();

        player.sendMessage(toSend);

    }



//    @Children(names = "list", async = true)
//    public void handleList(Player player) {
//        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
//        if(profile == null) {
//            return;
//        }
//        player.sendMessage(Component.text("You have " + profile.getFriends().size() + " friends"));
//        ProfileService profileService=  Universe.get(ProfileService.class);
//        for (UUID uuid : profile.getFriends().stream().sorted((o1, o2) -> Boolean.compare(profileService.isPlayerServerCached(o1), profileService.isPlayerServerCached(o2))).toList()) {
//            try {
//                String name = Universe.get(UUIDCacheService.class).get().getName(uuid).get(1, TimeUnit.SECONDS);
//                String serverId = profileService.getPlayerServer(uuid);
//                if(serverId == null) {
//                    player.sendMessage(name + " is offline since ");
//                } else {
//                    player.sendMessage(name + " is on " + serverId);
//                }
//
//            } catch (InterruptedException | TimeoutException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//    @Children(names = "", async = true)
//    public void handle(Player player, @Param(name = "UUID") UUID targetId) {
//
//    }


    // list

    // Nickname

    // notifications

    // remove

    // removeall

    // requests
}
