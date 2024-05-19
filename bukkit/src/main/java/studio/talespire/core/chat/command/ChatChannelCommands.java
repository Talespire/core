package studio.talespire.core.chat.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.ChatChannelService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.global.BukkitGlobalService;
import studio.talespire.core.social.global.chat.DefaultChatChannel;
import studio.talespire.core.social.guild.BukkitGuildService;
import studio.talespire.core.social.guild.chat.GuildChatChannel;

/**
 * @author Disunion
 * @date 5/17/2024
 */

@Command(names = {"chat", "c"}, permission = "talespire.chat", description = "Chat commands")
public class ChatChannelCommands {

    @Children(names = {"guild", "g"}, description = "Switch to guild chat channel")
    public void handleGuildChat(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        GuildChatChannel channel = Universe.get(BukkitGuildService.class).getChannel();
        Universe.get(ChatChannelService.class).setChatChannel(player, channel);
        player.sendMessage(Component.text("You are now in guild chat!", NamedTextColor.GREEN));
    }

    @Children(names = {"all", "a"}, description = "Switch to global chat channel")
    public void handleGlobalChat(Player player) {
        DefaultChatChannel channel = Universe.get(BukkitGlobalService.class).getChannel();
        Universe.get(ChatChannelService.class).setChatChannel(player, channel);
        player.sendMessage(Component.text("You are now in global chat!", NamedTextColor.GREEN));
    }
}