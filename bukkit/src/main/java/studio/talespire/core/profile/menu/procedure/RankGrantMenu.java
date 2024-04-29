package studio.talespire.core.profile.menu.procedure;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.menus.api.button.BackButton;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.GrantMenu;
import studio.talespire.core.profile.menu.button.api.ExitButton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class RankGrantMenu extends Menu {
    private final Profile profile;

    public RankGrantMenu(Profile profile) {
        this.profile = profile;
        this.setBordered(true);
    }
    @Override
    public String getTitle(Player player) {
        return "Which Rank?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(2, 1), new PlayerRankButton());
        buttons.put(getSlot(3, 1), new PlayerRankDescriptionButton());
        buttons.put(getSlot(5, 1), new StaffRankButton());
        buttons.put(getSlot(6, 1), new StaffRankDescriptionButton());

        buttons.put(getSlot(4, 2), new ExitButton());

        return buttons;

    }
    private class PlayerRankButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.GOLD_BLOCK)
                    .setName(ChatColor.RED + "Grant a Player Rank.")
                    .addLoreLine(ChatColor.GRAY + "Click to grant a player rank to this player.")
                    .toItemStack();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new PlayerRankGrantMenu(profile).openAsync(player);
        }
    }
    private class StaffRankButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.DIAMOND_BLOCK)
                    .setName(ChatColor.RED + "Grant a Staff Rank.")
                    .addLoreLine(ChatColor.GRAY + "Click to grant a staff rank to this player.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new StaffRankGrantMenu(profile).openAsync(player);
        }
    }

    private class PlayerRankDescriptionButton extends Button {
        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.OAK_SIGN)
                    .setName(ChatColor.WHITE + "Player Ranks")
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&7&m               "))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&a&lAdventurer"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&b&lHero"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&d&lChampion"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&1&lDivine"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&6&lImmortal"))
                    .toItemStack();
        }
    }

    private class StaffRankDescriptionButton extends Button {
        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.OAK_SIGN)
                    .setName(ChatColor.WHITE + "Staff Ranks")
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&7&m               "))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&2&lBuildTeam"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&b&lMod"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&c&lAdmin"))
                    .addLoreLine(ChatColor.translateAlternateColorCodes('&', "&c&lOwner"))
                    .toItemStack();
        }
    }
}
