package studio.talespire.core.profile.menu.button.api;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.rank.Rank;

@RequiredArgsConstructor
public class PlayerHeadButton extends Button {

    private final String playerName;
    private final Rank rank;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal())
                .setSkullOwner(playerName)
                .setName(ChatColor.GRAY + playerName)
                .addLoreLine(ChatColor.DARK_GRAY + "Current Rank: " + ChatColor.RESET + rank.name())
                .toItemStack();
    }
}
