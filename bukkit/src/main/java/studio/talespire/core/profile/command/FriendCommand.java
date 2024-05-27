package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.packet.friend.ProfileFriendAcceptPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendDenyPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendRemovePacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendRequestCreatePacket;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.utils.MenuUtils;

import java.util.UUID;


/**
 * @author Moose1301
 * @date 5/25/2024
 */
@Command(names = {"friend", "f"}, description = "Friend Commands")
public class FriendCommand {


    @Children(names = {"add"}, async = true, description = "Add a friend")
    public void handleAdd(Player player, @Param(name = "Target") UUID target) {
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
            new ProfileFriendAcceptPacket(player.getUniqueId(), target).send();

            // Update the target's profile
            targetProfile.getOutGoingFriendRequests().remove(player.getUniqueId());
            targetProfile.getFriends().add(player.getUniqueId());

            player.sendMessage(Component.text("You already have a friend request from ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }
        new ProfileFriendRequestCreatePacket(player.getUniqueId(), target).send();
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
            new ProfileFriendAcceptPacket(player.getUniqueId(), target).send();
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
            new ProfileFriendDenyPacket(player.getUniqueId(), target).send();
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

        int maxFriendsDisplayed = 8;
        int pagesOfFriends = (int) Math.ceil((double) profile.getFriends().size() / maxFriendsDisplayed);

        Component message;

        if (page == pagesOfFriends) {
            message = (Component.text("« Friends", NamedTextColor.GOLD)
                    .clickEvent(ClickEvent.runCommand("/friend list " + (page - 1)))
                )
                    .append(Component.text("(Page " + page + " of " + pagesOfFriends + ") ", NamedTextColor.GOLD));
        } else if (page > 1) {
            message = Component.text("« Friends ", NamedTextColor.GOLD)
                    .clickEvent(ClickEvent.runCommand("/friend list " + (page - 1)))
                    .append(Component.text("Friends ", NamedTextColor.GOLD))
                    .append(Component.text("(Page " + page + " of " + pagesOfFriends + ") ", NamedTextColor.GOLD))
                    .append(
                        Component.text("»", NamedTextColor.GOLD)
                            .clickEvent(ClickEvent.runCommand("/friend list " + (page + 1)))
                    );
        } else if (pagesOfFriends > 1){
            message = Component.text("Friends ", NamedTextColor.GOLD)
                .append(Component.text("(Page " + page + " of " + pagesOfFriends + ") ", NamedTextColor.GOLD))
                .append(
                    Component.text("»", NamedTextColor.GOLD)
                        .clickEvent(ClickEvent.runCommand("/friend list " + (page + 1)))
                );
        } else {
            message = Component.text("Friends (Page 1 of 1)", NamedTextColor.GOLD);
        }

        int numFriendsDisplayed = 0;
        ProfileService profileService = Universe.get(ProfileService.class);

        // List the amount of friends that corresponds to (pageNumber * maxFriendsDisplayed) + numFriendsDisplayed
        for (int i = (page - 1) * maxFriendsDisplayed; i < profile.getFriends().size() && numFriendsDisplayed < maxFriendsDisplayed; i++) {
            UUID uuid = profile.getFriend(i);
            String serverId = profileService.getPlayerServer(uuid);
            if (serverId == null) {
                message = profile.getBestFriends().contains(uuid) ?
                    message.append(BukkitProfileUtils.getRankedName(uuid).append(Component.text(" is offline", NamedTextColor.RED)))
                        .decorate(TextDecoration.BOLD)
                    : message.append(BukkitProfileUtils.getRankedName(uuid).append(Component.text(" is offline", NamedTextColor.RED)));
            } else {
                message = profile.getBestFriends().contains(uuid) ?
                    message.append(BukkitProfileUtils.getRankedName(uuid).append(Component.text(" is on " + serverId, NamedTextColor.YELLOW)))
                        .decorate(TextDecoration.BOLD)
                    : message.append(BukkitProfileUtils.getRankedName(uuid).append(Component.text(" is on " + serverId, NamedTextColor.YELLOW)));
            }
            numFriendsDisplayed++;
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

    @Children(names = {"remove", "r"}, async = true, description = "Remove a friend")
    public void handleRemove(Player player, @Param(name = "Target") UUID target) {
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

        // If the player has the target added as a friend, remove the target from the friends list
        if (profile.getFriends().contains(target)) {
            // Get the profile and remove the target from the friends list
            profile.getFriends().remove(target);

            // Get the target profile and remove the player from the friends list
            targetProfile.getFriends().remove(player.getUniqueId());
            new ProfileFriendRemovePacket(player.getUniqueId(), target).send();
            // Send a message to the player
            player.sendMessage(Component.text("You have removed ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)).append(Component.text(" from your friends list", NamedTextColor.RED)));
        } else {
            // Send a message to the player if they do not have the target added as a friend
            player.sendMessage(Component.text("You do not have ", NamedTextColor.RED).append(BukkitProfileUtils.getRankedName(target)).append(Component.text(" added as a friend", NamedTextColor.RED)));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @Children(names = {"removeall", "ra"}, async = true, description = "Remove all friends")
    public void handleRemoveAll(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Check if the profile is null
        if (profile == null) {
            return;
        }

        // Chick if the player has any friends
        if (profile.getFriends().isEmpty()) {
            player.sendMessage(Component.text("You do not have any friends to remove", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Remove all friends from the player's friends list
        for (UUID friend : profile.getFriends()) {
            Profile friendProfile = Universe.get(ProfileService.class).getOrLoadProfile(friend);
            friendProfile.getFriends().remove(player.getUniqueId());
            new ProfileFriendRemovePacket(player.getUniqueId(), friend).send();
        }

        player.sendMessage(Component.text("You have removed all friends", NamedTextColor.GREEN));
    }

    @Children(names = {"requests", "req"}, async = true, description = "List all friend requests")
    public void handleRequets(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getIncomingFriendRequests().isEmpty()) {
            player.sendMessage(Component.text("You do not have any friend requests", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        Component message = Component.text("You have friend Requests from: ", NamedTextColor.GREEN);

        for (UUID request : profile.getIncomingFriendRequests()) {
            message = message.append(BukkitProfileUtils.getRankedName(request)).append(Component.text(" has sent you a friend request", NamedTextColor.GREEN));
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

    //TODO: Make a nickname command (maybe)
}