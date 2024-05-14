package studio.talespire.core.social.guild.menus;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.menus.api.button.BackButton;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildTagColorMenu extends Menu {

    public GuildTagColorMenu() {
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Guild Tag Color";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(1, 1), new GrayColorButton());
        buttons.put(getSlot(2, 1), new GoldColorButton());
        buttons.put(getSlot(3, 1), new DarkAquaColorButton());
        buttons.put(getSlot(4, 1), new DarkGreenColorButton());
        buttons.put(getSlot(5, 1), new YellowColorButton());

        buttons.put(getSlot(4, 2), new BackButton(new GuildSettings(), true, true));

        return buttons;
    }

    private static class GrayColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {

            Guild guild = GetGuild(player);

            return new ItemBuilder(Material.GRAY_DYE)
                    .setName(ChatColor.GRAY + "Gray")
                    .addLoreLine(ChatColor.GRAY + "Preview: "
                            + ChatColor.GRAY + "["
                            + ChatColor.GRAY + guild.getTag()
                            + ChatColor.GRAY + "]")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.YELLOW + "Click to set your guild tag color to " + ChatColor.GRAY + "Gray")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = GetGuild(player);
            guild.setColor(NamedTextColor.GRAY);
            Universe.get(GuildService.class).saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "Guild tag color set to " + ChatColor.GRAY + "Gray");
        }
    }

    private static class GoldColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            Guild guild = GetGuild(player);

            return new ItemBuilder(Material.ORANGE_DYE)
                    .setName(ChatColor.GOLD + "Gold")
                    .addLoreLine(ChatColor.GRAY + "Preview: "
                            + ChatColor.GOLD + "["
                            + ChatColor.GOLD + guild.getTag()
                            + ChatColor.GOLD + "]")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.YELLOW + "Click to set your guild tag color to " + ChatColor.GOLD + "Gold")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = GetGuild(player);
            guild.setColor(NamedTextColor.GOLD);
            Universe.get(GuildService.class).saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "Guild tag color set to " + ChatColor.GOLD + "Gold");
        }
    }

    private static class DarkAquaColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {

            Guild guild = GetGuild(player);

            return new ItemBuilder(Material.CYAN_DYE)
                    .setName(ChatColor.DARK_AQUA + "Dark Aqua")
                    .addLoreLine(ChatColor.GRAY + "Preview: "
                            + ChatColor.DARK_AQUA + "["
                            + ChatColor.DARK_AQUA + guild.getTag()
                            + ChatColor.DARK_AQUA + "]")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.YELLOW + "Click to set your guild tag color to " + ChatColor.DARK_AQUA + "Dark Aqua")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = GetGuild(player);
            guild.setColor(NamedTextColor.DARK_AQUA);
            Universe.get(GuildService.class).saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "Guild tag color set to " + ChatColor.DARK_AQUA + "Dark Aqua");
        }
    }

    private static class DarkGreenColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {

            Guild guild = GetGuild(player);

            return new ItemBuilder(Material.GREEN_DYE)
                    .setName(ChatColor.DARK_GREEN + "Dark Green")
                    .addLoreLine(
                            ChatColor.GRAY + "Preview: "
                                    + ChatColor.DARK_GREEN + "["
                                    + ChatColor.DARK_GREEN + guild.getTag()
                                    + ChatColor.DARK_GREEN + "]")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.YELLOW + "Click to set your guild tag color to " + ChatColor.DARK_GREEN + "Dark Green")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = GetGuild(player);
            guild.setColor(NamedTextColor.DARK_GREEN);
            Universe.get(GuildService.class).saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "Guild tag color set to " + ChatColor.DARK_GREEN + "Dark Green");
        }
    }

    private static class YellowColorButton extends Button {

        @Override
        public ItemStack getItem(Player player) {

            Guild guild = GetGuild(player);

            return new ItemBuilder(Material.YELLOW_DYE)
                    .setName(ChatColor.YELLOW + "Yellow")
                    .addLoreLine(
                            ChatColor.GRAY + "Preview: "
                                    + ChatColor.YELLOW + "["
                                    + ChatColor.YELLOW + guild.getTag()
                                    + ChatColor.YELLOW + "]")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.YELLOW + "Click to set your guild tag color to " + ChatColor.YELLOW + "Yellow")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Guild guild = GetGuild(player);
            guild.setColor(NamedTextColor.YELLOW);
            Universe.get(GuildService.class).saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "Guild tag color set to " + ChatColor.YELLOW + "Yellow");
        }
    }

    private static Guild GetGuild (Player player) {
        return Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();
    }
}