package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.packet.GuildTagPacket;

import java.util.function.Consumer;

@AllArgsConstructor
public class TagInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;

    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        if (!GuildService.NAME_REGEX.matcher(s).matches() || (s.length() > 5 || s.length() < 3)) {
            Player player = conversationContext.getForWhom() instanceof Player ? (Player) conversationContext.getForWhom() : null;
            player.sendMessage(Component.text("That is not a valid guild tag", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
        callback.accept(s);

        Guild guild = Universe.get(ProfileService.class).getProfile(((Player) conversationContext.getForWhom()).getUniqueId()).getGuild();
        Player player = conversationContext.getForWhom() instanceof Player ? (Player) conversationContext.getForWhom() : null;

        guild.setTag(s);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildTagPacket(guild.getUuid(), player.getUniqueId(), s));
        player.sendMessage(ChatColor.GREEN + "Guild tag set to " + s + "!");
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Please input what you would like to set the guild tag to. (3-5 characters)";
    }
}
