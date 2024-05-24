package studio.talespire.core.social.global.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.model.ChatChannel;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.rank.Rank;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Disunion
 * @date 5/17/2024
 */
public class DefaultChatChannel extends ChatChannel {

    public DefaultChatChannel() {
        super("Public", '!', "");
    }

    @Override
    public Collection<? extends Player> getRecipients(Player sender) {
        return new HashSet<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public ComponentLike formatMessage(Player player, Component message) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile == null) {
            return message;
        }

        if (profile.getRank() == Rank.DEFAULT) {
            return Component.text()
                    .append(BukkitProfileUtils.getRankedNameLoaded(player.getUniqueId()))
                    .append(Component.text(" » ", NamedTextColor.WHITE))
                    .append(message.color(NamedTextColor.GRAY))
                    .build();
        }

        return Component.text()
                .append(BukkitProfileUtils.getRankedNameLoaded(player.getUniqueId()))
                .append(Component.text(" » ", NamedTextColor.WHITE))
                .append(message.color(NamedTextColor.WHITE))
                .build();
    }

    @Override
    public boolean canAccess(Player player) {
        return true;
    }
}
