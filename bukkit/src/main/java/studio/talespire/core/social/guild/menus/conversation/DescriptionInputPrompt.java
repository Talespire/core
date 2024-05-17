package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildPermission;

import java.util.function.Consumer;

@AllArgsConstructor
public class DescriptionInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;


    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        if (s.length() > 512) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(Component.text("You cannot have more than 512 characters in the description! Please try again.", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return false;
        }
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
        callback.accept(s);

        Player player = (Player) conversationContext.getForWhom();
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        Guild guild = profile.getGuild();

        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.DESCRIPTION)) {
            player.sendMessage(Component.text("You do not have permission to do this", NamedTextColor.RED));
            return Prompt.END_OF_CONVERSATION;
        }

        guild.setDescription(s);
        Universe.get(GuildService.class).saveGuild(guild);
        player.sendMessage(Component.text("Guild description successfuly updated!", NamedTextColor.GREEN));
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return "Enter a new line for the Description or type \"cancel\" to cancel.";
    }
}
