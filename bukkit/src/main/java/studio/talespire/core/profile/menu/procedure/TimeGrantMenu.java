package studio.talespire.core.profile.menu.procedure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.UniversePlugin;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.button.GrantInfoButton;
import studio.talespire.core.profile.menu.conversation.TimeInputPrompt;
import studio.talespire.core.rank.Rank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class TimeGrantMenu extends Menu {
    private final Profile profile;
    private @Nullable Rank rank = null;
    private @Nullable String permission = null;

    public TimeGrantMenu(Profile profile, @Nonnull Rank rank) {
        this.profile = profile;
        this.rank = rank;
    }

    public TimeGrantMenu(Profile profile, @Nonnull String permission) {
        this.profile = profile;
        this.permission = permission;
    }

    @Override
    public String getTitle(Player player) {
        return "Please Select a Time";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        GrantInfoButton button;
        if (rank != null) {
            button = new GrantInfoButton(profile.getUsername(), rank);
        } else {
            button = new GrantInfoButton(profile.getUsername(), permission);
        }
        return Map.of(
                4, button,
                10, new TimeButton(TimeUnit.HOURS.toMillis(1), time -> handleTime(player, time)),
                11, new TimeButton(TimeUnit.HOURS.toMillis(12), time -> handleTime(player, time)),
                12, new TimeButton(TimeUnit.DAYS.toMillis(1), time -> handleTime(player, time)),
                13, new TimeButton(TimeUnit.DAYS.toMillis(7), time -> handleTime(player, time)),
                14, new TimeButton(TimeUnit.DAYS.toMillis(30), time -> handleTime(player, time)),
                15, new TimeButton(TimeUnit.DAYS.toMillis(356), time -> handleTime(player, time)),
                16, new TimeButton(-1, time -> handleTime(player, time)),
                22, new CustomTimeButton(time -> handleTime(player, time))
        );
    }

    private void handleTime(Player player, long time) {
        player.closeInventory();
        if (this.rank != null) {
            new ReasonGrantMenu(profile, rank, time).openAsync(player);
            return;
        }
        new ReasonGrantMenu(profile, permission, time).openAsync(player);
    }

    private class CustomTimeButton extends Button {
        private final Consumer<Long> callback;

        public CustomTimeButton(Consumer<Long> callback) {
            this.callback = callback;
        }

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text("Custom", NamedTextColor.WHITE));
            stack.setItemMeta(meta);

            return stack;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            Conversation conversation = new ConversationFactory(UniversePlugin.get())
                    .withModality(true)
                    .withPrefix(new NullConversationPrefix())
                    .withLocalEcho(false)
                    .withEscapeSequence("cancel")
                    .withTimeout(60)
                    .withFirstPrompt(new TimeInputPrompt(callback))
                    .buildConversation(player);
            player.beginConversation(conversation);
        }
    }

    private class TimeButton extends Button {
        private final long duration;
        private final Consumer<Long> callback;

        public TimeButton(long duration, Consumer<Long> callback) {
            this.duration = duration;
            this.callback = callback;
        }

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(Material.CLOCK);
            ItemMeta meta = stack.getItemMeta();
            if (duration == -1) {
                meta.displayName(Component.text("Permanent", NamedTextColor.WHITE));
            } else {
                meta.displayName(Component.text(TimeUtil.formatTimeDetailed(duration), NamedTextColor.WHITE));
            }

            stack.setItemMeta(meta);
            return stack;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            callback.accept(duration);
        }
    }
}
