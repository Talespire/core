package studio.talespire.core.profile.menu.ranks;

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
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.UniversePlugin;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.menu.button.GrantInfoButton;
import studio.talespire.core.profile.menu.conversation.ReasonInputPrompt;
import studio.talespire.core.profile.packet.ProfileGrantPacket;
import studio.talespire.core.rank.Rank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class ReasonGrantMenu extends Menu {
    private final Profile profile;
    private long time;
    private @Nullable Rank rank = null;
    private @Nullable String permission = null;

    public ReasonGrantMenu(Profile profile, @Nonnull Rank rank, long time) {
        this.profile = profile;
        this.rank = rank;
        this.time = time;
    }

    public ReasonGrantMenu(Profile profile, @Nonnull String permission, long time) {
        this.profile = profile;
        this.permission = permission;
        this.time = time;
    }

    @Override
    public String getTitle(Player player) {
        return "Please Select a Reason";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        GrantInfoButton button;
        if (rank != null) {
            button = new GrantInfoButton(profile.getUsername(), rank, time);
        } else {
            button = new GrantInfoButton(profile.getUsername(), permission, time);
        }
        return Map.of(
                4, button,
                10, new ReasonButton("Giveaway", reason -> handleReason(player, reason)),
                11, new ReasonButton("Gifted", reason -> handleReason(player, reason)),
                12, new ReasonButton("Accepted", reason -> handleReason(player, reason)),

                14, new ReasonButton("Purchase", reason -> handleReason(player, reason)),
                15, new ReasonButton("Demoted", reason -> handleReason(player, reason)),
                16, new ReasonButton("Promoted", time -> handleReason(player, time)),

                22, new CustomReasonButton(reason -> handleReason(player, reason))
        );
    }

    private void handleReason(Player player, String reason) {
        player.closeInventory();
        player.sendMessage(
                Component.text()
                                .append(Component.text("You granted", NamedTextColor.GOLD))
                        .append(profile.getFormattedName())
                        .append(Component.space())
                        .append(rank != null ? rank.getPrefix() : Component.text(permission))
        );
        Grant grant;
        if(rank != null) {
            grant = new GrantRank(UUID.randomUUID(), rank, reason, time, player.getUniqueId(), System.currentTimeMillis());
        } else {
            grant = new GrantPermission(UUID.randomUUID(), permission, reason, time, player.getUniqueId(), System.currentTimeMillis());
        }
        profile.getGrants().add(grant);
        Universe.get(RedisService.class).publish(new ProfileGrantPacket(profile.getUuid(), grant));
        Universe.get(ProfileService.class).saveProfile(profile);

    }

    private class CustomReasonButton extends Button {
        private final Consumer<String> callback;

        public CustomReasonButton(Consumer<String> callback) {
            this.callback = callback;
        }

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(Material.PAINTING);
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
                    .withFirstPrompt(new ReasonInputPrompt(callback))
                    .buildConversation(player);
            player.beginConversation(conversation);
        }
    }

    private class ReasonButton extends Button {
        private final String reason;
        private final Consumer<String> callback;

        public ReasonButton(String reason, Consumer<String> callback) {
            this.reason = reason;
            this.callback = callback;
        }

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(Material.OAK_SIGN);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text(reason, NamedTextColor.WHITE));
            stack.setItemMeta(meta);

            return stack;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            callback.accept(reason);
        }
    }

}
