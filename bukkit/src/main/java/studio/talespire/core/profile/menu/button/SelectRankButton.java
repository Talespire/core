package studio.talespire.core.profile.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.utils.ColorUtils;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class SelectRankButton extends Button {
    private final Rank rank;
    private final Consumer<Rank> callback;
    public SelectRankButton(Rank rank, @Nonnull Consumer<Rank> callback) {
        this.rank = rank;
        this.callback = callback;
    }
    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = ColorUtils.toLeatherArmor(rank.getColor(), Material.LEATHER_CHESTPLATE);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(rank.getPrefix());
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        callback.accept(rank);
    }
}
