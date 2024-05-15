package studio.talespire.core.social.guild.menus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
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
import studio.lunarlabs.universe.menus.api.MenuHandler;
import studio.lunarlabs.universe.menus.api.button.BackButton;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.menus.conversation.DiscordInputPrompt;
import studio.talespire.core.social.guild.menus.conversation.InviteInputPrompt;
import studio.talespire.core.social.guild.menus.conversation.MOTDChat;
import studio.talespire.core.social.guild.menus.conversation.TagInputPrompt;
import studio.talespire.core.social.guild.model.Guild;
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

        buttons.put(getSlot(2, 1), new GuildTagButton());
        buttons.put(getSlot(3, 1), new TagColorButton());
        buttons.put(getSlot(4, 1), new DescriptionButton());
        buttons.put(getSlot(5, 1), new MOTDButton());
        buttons.put(getSlot(6, 1), new DiscordButton());

        buttons.put(getSlot(4, 2), new BackButton(new GuildLandingPage(player), true, true));

        return buttons;
    }

    private static class GuildTagButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            return new ItemBuilder(Material.NAME_TAG)
                    .setName(ChatColor.GREEN + "Guild Tag")
                    .addLoreLine(ChatColor.GRAY + "Changes the tag next to your guild")
                    .addLoreLine(ChatColor.GRAY + "member's names.")
                    .addLoreLine("")
                    .addLoreLine(!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG) ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild tag.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {

            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            if (!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG)) {
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
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            return new ItemBuilder(Material.CYAN_DYE)
                    .setName(ChatColor.GREEN + "Guild Tag Color")
                    .addLoreLine(ChatColor.GRAY + "Changes the color of the tag next to your guild")
                    .addLoreLine(ChatColor.GRAY + "member's names.")
                    .addLoreLine("")
                    .addLoreLine(!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG) ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild tag.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            if (!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG)) {
                player.sendMessage(ChatColor.RED + "You do not have permission to change this!");
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 20f, 0.1f);
                player.closeInventory();

                Universe.get(MenuHandler.class).openMenuAsync(new GuildTagColorMenu(), player);
            }
        }
    }

    private static class DescriptionButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            return new ItemBuilder(Material.WRITABLE_BOOK)
                    .setName(ChatColor.GREEN + "Guild Description")
                    .addLoreLine(ChatColor.GRAY + "Changes the description of your guild.")
                    .addLoreLine("")
                    .addLoreLine(!guild.hasPermission(player.getUniqueId(), GuildPermission.DESCRIPTION) ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild description.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            //TODO: Open a conversation to set the guild description
        }
    }

    private static class MOTDButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            return new ItemBuilder(Material.WRITABLE_BOOK)
                    .setName(ChatColor.GREEN + "Guild MOTD")
                    .addLoreLine(ChatColor.GRAY + "Changes the message of the day for your guild.")
                    .addLoreLine("")
                    .addLoreLine(!guild.hasPermission(player.getUniqueId(), GuildPermission.MOTD) ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the MOTD")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 20f, 0.1f);
            player.closeInventory();

            MOTDChat motdChat = new MOTDChat(Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild(), player);
            motdChat.displayCurrentMOTD();
        }
    }

    private static class DiscordButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();

            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner("PurityGuildBot")
                    .setName(ChatColor.GREEN + "Guild Discord")
                    .addLoreLine(ChatColor.GRAY + "Changes the Discord URL for your guild.")
                    .addLoreLine("")
                    .addLoreLine(!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG) ? ChatColor.RED + "You do not have permission to change this!" : ChatColor.YELLOW + "Click to change the guild tag.")
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
                    .withFirstPrompt(new DiscordInputPrompt(s -> new GuildSettings().openAsync(player)))
                    .buildConversation(player);
            player.beginConversation(conversation);
        }
    }
}
