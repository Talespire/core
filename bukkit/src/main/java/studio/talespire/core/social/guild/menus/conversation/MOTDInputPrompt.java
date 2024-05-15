package studio.talespire.core.social.guild.menus.conversation;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@AllArgsConstructor
public class MOTDInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;

    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
        callback.accept(s);
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Enter a new line for the MOTD or type \"cancel\" to cancel.";
    }
}
