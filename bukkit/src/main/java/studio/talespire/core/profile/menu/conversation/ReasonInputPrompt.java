package studio.talespire.core.profile.menu.conversation;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.util.time.TimeUtil;

import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
@AllArgsConstructor
public class ReasonInputPrompt extends ValidatingPrompt {
    private final Consumer<String> callback;
    @Override
    protected boolean isInputValid(@NotNull ConversationContext ctx, @NotNull String input) {
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext ctx, @NotNull String input) {
        callback.accept(input);
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext ctx) {
        return ChatColor.GOLD + "Please input the reason you are granting this rank";
    }
}
