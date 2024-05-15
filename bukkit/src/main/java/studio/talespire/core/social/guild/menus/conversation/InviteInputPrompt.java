package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import studio.talespire.core.social.guild.command.GuildCommand;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.packet.GuildInvitePacket;

import java.util.UUID;
import java.util.function.Consumer;

@AllArgsConstructor
public class InviteInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;


    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        if (Bukkit.getPlayer(s) == null) {
            if (Bukkit.getOfflinePlayer(s) == null) {
                conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "Player not found");
                return false;
            }
        }
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String input) {
        callback.accept(input);
        Player invitor = (Player) conversationContext.getForWhom();
        Player target = Bukkit.getPlayer(input);
        Profile profile = Universe.get(ProfileService.class).getProfile(invitor.getUniqueId());

        // Check if the player is in a guild, if they have perms to invite, if the target is already in the guild, if the target has already been invited, and if the target can be invited
        if (profile.getGuild() == null) {
            invitor.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return Prompt.END_OF_CONVERSATION;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(invitor.getUniqueId(), GuildPermission.INVITE)) {
            invitor.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target.getUniqueId());
        if (guild.getMembers().containsKey(targetProfile.getUuid())) {
            invitor.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" is already in your guild", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }
        if (guild.getInvites().containsKey(targetProfile.getUuid())) {
            invitor.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" has already been invited to your guild", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }
        if (!targetProfile.getSetting(GuildInviteSetting.class).value().doesMatch(profile, targetProfile)) {
            invitor.sendMessage(Component.text("You cannot invite ", NamedTextColor.RED)
                    .append(Component.text(targetProfile.getUsername(), NamedTextColor.YELLOW))
            );
            return Prompt.END_OF_CONVERSATION;
        }

        GuildCommand.SendGuildMessage(invitor, Component.text("You have invited", NamedTextColor.GREEN)
                .append(Component.space())
                .append(targetProfile.getFormattedName())
                .append(Component.space())
                .append(Component.text("to join the guild", NamedTextColor.GREEN)));

        // Invite the player to the guild
        guild.createInvite(profile.getUuid(), targetProfile.getUuid());
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildInvitePacket(guild.getUuid(), profile.getUuid(), targetProfile.getUuid()));

        GuildCommand.SendGuildMessage(target, Component.text(invitor.getPlayerListName())
                .append(Component.text(" has invited you to join their guild!", NamedTextColor.GREEN))
                .append(Component.newline())
                .append(
                        Component.text("Click Here", NamedTextColor.AQUA, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand(("/guild join " + invitor.getName())))
                )
                .append(Component.text(" or type ", NamedTextColor.GREEN))
                .append(Component.text("/guild join " + invitor.getName(), NamedTextColor.AQUA))
                .append(Component.text(" to accept their request!", NamedTextColor.GREEN))
        );

        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Please input the name of the player you want to invite!";
    }
}
