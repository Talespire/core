package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.setting.types.privacy.GuildInviteSetting;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.packet.GuildInvitePacket;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@AllArgsConstructor
public class InviteInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;


    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String input) {
        callback.accept(input);

        Player player = conversationContext.getForWhom() instanceof Player ? (Player) conversationContext.getForWhom() : null;
        UUID targetId = Bukkit.getPlayer(input).getUniqueId();
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return Prompt.END_OF_CONVERSATION;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.INVITE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }

        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(targetId);
        if (guild.getMembers().containsKey(targetProfile.getUuid())) {
            player.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" is already in your guild", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }
        if (guild.getInvites().containsKey(targetProfile.getUuid())) {
            player.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" has already been invited to your guild", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }
        if (!targetProfile.getSetting(GuildInviteSetting.class).value().doesMatch(profile, targetProfile)) {
            player.sendMessage(Component.text("You cannot invite ", NamedTextColor.RED)
                    .append(Component.text(targetProfile.getUsername(), NamedTextColor.YELLOW))
            );
            return Prompt.END_OF_CONVERSATION;
        }

        guild.createInvite(profile.getUuid(), targetProfile.getUuid());
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildInvitePacket(guild.getUuid(), profile.getUuid(), targetProfile.getUuid()));
        player.sendMessage(ChatColor.GREEN + "You have invited " + targetProfile.getUsername() + " to your guild!");
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GOLD + "Please input the name of the player you want to invite!";
    }
}
