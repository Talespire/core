package studio.talespire.core.profile.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.UniversePlugin;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.button.api.PlayerHeadButton;
import studio.talespire.core.profile.menu.conversation.PermissionInputPrompt;
import studio.talespire.core.profile.menu.procedure.RankGrantMenu;
import studio.talespire.core.profile.menu.procedure.TimeGrantMenu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */

public class GrantMenu extends Menu {
    private final Profile profile;

    public GrantMenu(Profile profile) {
        this.profile = profile;
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return ("Granting " + profile.getUsername());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(2, 1), new PermissionGrantButton());
        buttons.put(getSlot(4, 1), new PlayerHeadButton(profile.getUsername(), profile.getRank()));
        buttons.put(getSlot(6, 1), new RankGrantButton());

        return buttons;
    }

    private class PermissionGrantButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .setName(ChatColor.GREEN + "Grant a Permission.")
                    .addLoreLine(ChatColor.GRAY + "Click to grant a permission to this player.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 20f, 0.1f);
            player.closeInventory();

            Conversation conversation = new ConversationFactory(UniversePlugin.get())
                    .withModality(true)
                    .withPrefix(new NullConversationPrefix())
                    .withLocalEcho(false)
                    .withEscapeSequence("cancel")
                    .withTimeout(60)
                    .withFirstPrompt(new PermissionInputPrompt(s -> new TimeGrantMenu(profile, s).openAsync(player)))
                    .buildConversation(player);
            player.beginConversation(conversation);

        }
    }

    private class RankGrantButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.GREEN_WOOL).setName(ChatColor.GREEN + ("Grant a Rank."))
                    .addLoreLine(ChatColor.GRAY + "Click to grant a rank to this player.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20f, 0.1f);
            player.closeInventory();
            new RankGrantMenu(profile).openAsync(player);
        }
    }
}
