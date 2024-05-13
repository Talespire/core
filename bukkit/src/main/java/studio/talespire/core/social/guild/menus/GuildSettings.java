package studio.talespire.core.social.guild.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.UniversePlugin;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.menus.conversation.TagInputPrompt;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.model.GuildRole;

import java.util.HashMap;
import java.util.Map;

public class GuildSettings extends Menu {

    public GuildSettings() {
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Guild Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(1, 1), new GuildTagButton());
        buttons.put(getSlot(2, 1), new TagColorButton());

        return buttons;
    }

    private static class GuildTagButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            GuildRole role = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild().getRole(player.getUniqueId());

            return new ItemBuilder(Material.NAME_TAG)
                    .setName(ChatColor.GREEN + "Guild Tag")
                    .addLoreLine(ChatColor.GRAY + "Changes the tag next to your guild")
                    .addLoreLine(ChatColor.GRAY + "member's names.")
                    .addLoreLine("")
                    .addLoreLine(GuildPermission.TAG.getDefaultRole() != role ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild tag.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (GuildPermission.TAG.getDefaultRole() != Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild().getRole(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You do not have permission to change this!");
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 20f, 0.1f);
                player.closeInventory();

                Conversation conversation = new ConversationFactory(UniversePlugin.get())
                        .withModality(true)
                        .withPrefix(new NullConversationPrefix())
                        .withLocalEcho(false)
                        .withEscapeSequence("cancel")
                        .withTimeout(60)
                        .withFirstPrompt(new TagInputPrompt(s -> new GuildSettings().openAsync(player)))
                        .buildConversation(player);
                player.beginConversation(conversation);
            }
        }
    }

    private static class TagColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            GuildRole role = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild().getRole(player.getUniqueId());

            return new ItemBuilder(Material.CYAN_DYE)
                    .setName(ChatColor.GREEN + "Guild Tag Color")
                    .addLoreLine(ChatColor.GRAY + "Changes the color of the tag next to your guild")
                    .addLoreLine(ChatColor.GRAY + "member's names.")
                    .addLoreLine("")
                    .addLoreLine(GuildPermission.TAG.getDefaultRole() != role ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild tag color.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            // Opens tag color gui
        }
    }

    private static class DescriptionButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            GuildRole role = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild().getRole(player.getUniqueId());

            return new ItemBuilder(Material.WRITABLE_BOOK)
                    .setName(ChatColor.GREEN + "Guild Description")
                    .addLoreLine(ChatColor.GRAY + "Changes the description of your guild.")
                    .addLoreLine("")
                    .addLoreLine(GuildPermission.DESCRIPTION.getDefaultRole() != role ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild description.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            // Open a conversation to set the guild description
        }
    }
}
