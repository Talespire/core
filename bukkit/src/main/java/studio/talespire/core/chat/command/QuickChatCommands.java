package studio.talespire.core.chat.command;

import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
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
public class QuickChatCommands {

    @Command(names = "gc", description = "Quick guild chat")
    public void handleQuickGuildChat(Player player, @Param(name = "message") String message) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        GuildChatChannel channel = Universe.get(BukkitGuildService.class).getChannel();
        channel.sendMessage(player, Component.text(message));
    }

    @Command(names = "ac", description = "Quick all chat")
    public void handleQuickAllChat(Player player, @Param(name = "message") String message) {

        DefaultChatChannel channel = Universe.get(BukkitGlobalService.class).getChannel();
        channel.sendMessage(player, Component.text(message));
    }
}
