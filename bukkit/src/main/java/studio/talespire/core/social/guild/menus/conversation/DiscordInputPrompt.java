package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

@AllArgsConstructor
public class DiscordInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;

    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        try {
            URL url = new URL(s);
        } catch (MalformedURLException e) {
            conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "Invalid URL. Please enter a valid Discord URL.");
            return false;
        }
        if (!s.contains("discord.gg") && !s.contains("discord.com") && !s.contains("discordapp.com")) {
            conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "Invalid URL. Please enter a valid Discord URL.");
            return false;
        }
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String input) {
        callback.accept(input);

        Player player = (Player) conversationContext.getForWhom();
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        Guild guild = profile.getGuild();

        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.INVITE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return Prompt.END_OF_CONVERSATION;
        }

        guild.setDiscord(input);
        Universe.get(GuildService.class).saveGuild(guild);
        player.sendMessage(ChatColor.GREEN + "Successfully set the Discord URL to " + input + "!");
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Please input the Discord URL for your guild!";
    }
}
