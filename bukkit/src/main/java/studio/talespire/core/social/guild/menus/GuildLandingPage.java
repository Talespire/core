package studio.talespire.core.social.guild.menus;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
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
import studio.lunarlabs.universe.menus.api.MenuHandler;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.menus.conversation.InviteInputPrompt;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildMember;
import studio.talespire.profile.Profile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Disunion
 * @date 05/12/2024
 */

public class GuildLandingPage extends Menu {

    protected static Guild guild;

    public GuildLandingPage(Player player) {
        this.setBordered(true);
        guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();
    }

    @Override
    public String getTitle(Player player) {
        return "Guild";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(2, 1), new InviteButton());
        buttons.put(getSlot(3, 1), new QuickInfoButton());
        buttons.put(getSlot(4, 1), new SettingsButton());
        buttons.put(getSlot(5, 1), new DiscordButton());
        buttons.put(getSlot(6, 1), new QuickSearch());

        for (int i = 1; i < 8; i++) {
            buttons.put(getSlot(i, 2), new Button() {
                @Override
                public ItemStack getItem(Player player) {
                    return new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName(ChatColor.RESET + "").toItemStack();
                }
            });
        }

        // TODO: Pagination
        // Temporary solution until we fix pagination
        Map<UUID, GuildMember> members = guild.getMembers();
        for (int i = 0; i < 8; i++) {
            if (members.size()-1 < i) {
                break;
            }

            UUID playerUUID = members.keySet().toArray(new UUID[0])[i];

            buttons.put(getSlot(i + 1, 3), new MemberButton(Bukkit.getPlayer(playerUUID)));
        }

        return buttons;
    }

    // -- Buttons --

    private static class InviteButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.WRITABLE_BOOK)
                    .setName(ChatColor.GREEN + "Invite Player")
                    .addLoreLine(ChatColor.GRAY + "Click here to invite a player to your")
                    .addLoreLine(ChatColor.GRAY + "guild.")
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
                    .withFirstPrompt(new InviteInputPrompt(s -> new GuildLandingPage(player).openAsync(player)))
                    .buildConversation(player);
            player.beginConversation(conversation);
        }
    }

    private static class QuickInfoButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAINTING)
                    .setName(ChatColor.GREEN + "Guild Info")
                    .addLoreLine(ChatColor.GRAY +  "Name: " + ChatColor.GOLD + Universe.get(GuildService.class).getGuild(guild.getUuid()).getName())
                    .addLoreLine(ChatColor.GRAY + "Rank: " + ChatColor.GOLD + guild.getRole(player.getUniqueId()).name())
                    .addLoreLine(
                            (ChatColor.GRAY + "Members: " + ChatColor.GOLD + guild.getMembers().size()) +
                                    (ChatColor.AQUA + "/" + ChatColor.GOLD + "125")
                    )
                    .toItemStack();
        }
    }

    private static class SettingsButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.COMPARATOR)
                    .setName(ChatColor.GREEN + "Guild Settings")
                    .addLoreLine(ChatColor.GRAY + "Click here to view and edit your guild settings.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            Universe.get(MenuHandler.class).openMenuAsync(player, new GuildSettings());
        }
    }

    private static class DiscordButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner("PurityGuildBot")
                    .setName(ChatColor.GREEN + "Discord")
                    .addLoreLine(ChatColor.GRAY + "Click here to join the guild discord.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 20f, 0.1f);
            player.sendMessage(ChatColor.GREEN + guild.getDiscord());
        }
    }

    private static class QuickSearch extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.OAK_SIGN)
                    .setName(ChatColor.GREEN + "Quick Search")
                    .addLoreLine(ChatColor.GRAY + "Search for a player in your guild.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            // Start a chat conversation
        }
    }

    @RequiredArgsConstructor
    private static class MemberButton extends Button {

        private final Player desiredPlayer;

        private final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        @Override
        public ItemStack getItem(Player player) {

            Component component = BukkitProfileUtils.getFormatedName(desiredPlayer.getUniqueId());
            TextComponent nameComponent = Component.text()
                    .append(Universe.get(ProfileService.class).getProfile(desiredPlayer.getUniqueId()).getRank().getTabPrefix())
                    .append(Universe.get(ProfileService.class).getProfile(desiredPlayer.getUniqueId()).getRank() == Rank.DEFAULT ? Component.empty() : Component.space())
                    .append(component)
                    .build();

            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(desiredPlayer.getName())
                    .setName(ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(nameComponent)))
                    .addLoreLine(ChatColor.GRAY + "Level: " + ChatColor.GOLD + Profile.getInstance().getProfileHandler().getProfile(desiredPlayer.getUniqueId()).getCharacters().get(0).getLevel())
                    .addLoreLine(ChatColor.GRAY + "Rank: " + ChatColor.AQUA + guild.getRole(desiredPlayer.getUniqueId()).name().toLowerCase().replaceFirst(
                            "^[a-z]", String.valueOf(Character.toUpperCase(guild.getRole(desiredPlayer.getUniqueId()).name().charAt(0)))))
                    .addLoreLine(ChatColor.GRAY + "Member since: " + ChatColor.AQUA + dateFormat.format(guild.getMember(desiredPlayer.getUniqueId()).getJoinedAt()))
                    .toItemStack();

        }
    }
}
