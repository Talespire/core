package studio.talespire.core.profile.menu.button;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.util.Constants;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.utils.ColorUtils;
import studio.talespire.core.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
@AllArgsConstructor
public class RankGrantButton extends Button {
    private GrantRank grant;
    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = ColorUtils.toLeatherArmor(grant.getRank().getColor(), Material.LEATHER_CHESTPLATE);
        ItemMeta meta = stack.getItemMeta();
        if(grant.isActive()) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        meta.displayName(Component.text()
                .append(grant.getRank().getPrefix())
                .append(Component.text("(" + grant.getId().toString() + ")", NamedTextColor.DARK_GRAY))
                .build()
        );
        List<Component> lore = new ArrayList<>();
        lore.add(MenuUtils.menuSeparator());


        meta.lore(lore);
        stack.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }
}
