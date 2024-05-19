package studio.talespire.core.social.guild.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.model.ChatChannel;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.packet.GuildChatPacket;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Moose1301
 * @date 5/17/2024
 */
public class GuildChatChannel extends ChatChannel {
    public GuildChatChannel() {
        super("Guild", '$');
    }

    @Override
    public Collection<? extends Player> getRecipients(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if(profile == null || profile.getGuild() == null) {
            return List.of();
        }
        Guild guild = Universe.get(GuildService.class).getGuild(profile.getGuildId());
        if(guild == null) {
            return List.of();
        }

        return guild.getMembers().keySet().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public ComponentLike formatMessage(Player player, Component component) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if(profile == null || profile.getGuild() == null) {
            return component;
        }
        Guild guild = Universe.get(GuildService.class).getGuild(profile.getGuildId());
        if(guild == null) {
            return component;
        }

        return  Component.text("Guild ", NamedTextColor.DARK_GREEN)
                .append(BukkitProfileUtils.getRankedNameLoaded(player.getUniqueId())
                        .appendSpace()
                        .append(Component.text("[", guild.getColor()))
                        .append(Component.text(guild.getMember(player.getUniqueId()).getRole().name(), guild.getColor()))
                        .append(Component.text("]", guild.getColor()))
                        .append(Component.text(" » ", NamedTextColor.WHITE))
                        .append(component.color(NamedTextColor.WHITE)));

    }

    @Override
    public boolean canAccess(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if(profile == null) {
            return false;
        }
        return profile.getGuild() != null;
    }
    @Override
    public void sendMessage(Player player, Component message) {
        super.sendMessage(player, message);
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if(profile == null) {
            return;
        }
        Guild guild = Universe.get(GuildService.class).getGuild(profile.getGuildId());
        if(guild == null) {
            return;
        }
        Universe.get(RedisService.class).publish(new GuildChatPacket(
                profile.getGuildId(),
                player.getUniqueId(),
                GsonComponentSerializer.gson().serialize(message)
        ));

    }
}
