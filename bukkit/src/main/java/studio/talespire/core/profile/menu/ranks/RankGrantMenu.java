package studio.talespire.core.profile.menu.ranks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.talespire.core.profile.Profile;

import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class RankGrantMenu extends Menu {
    private final Profile profile;

    public RankGrantMenu(Profile profile) {
        this.profile = profile;
    }
    @Override
    public String getTitle(Player player) {
        return "Rank Granting for " + profile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(
                3, new PlayerRankButton(),
                5, new StaffRankButton()
        );
    }
    private class PlayerRankButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.RESET  + "Player Rank");
            stack.setItemMeta(meta);
            return stack;
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
            ItemStack stack = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.RESET  + "Staff Rank");
            stack.setItemMeta(meta);
            return stack;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new StaffRankGrantMenu(profile).openAsync(player);
        }
    }
}
