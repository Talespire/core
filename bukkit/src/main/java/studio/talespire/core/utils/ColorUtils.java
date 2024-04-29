package studio.talespire.core.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class ColorUtils {
    public static ItemStack toLeatherArmor(TextColor color, Material type) {
        ItemStack stack = new ItemStack(type);
        if(!(stack.getItemMeta() instanceof LeatherArmorMeta meta)) {
            throw new RuntimeException(type.name() + " is not leather armor");
        }
        meta.setColor(Color.fromRGB(color.red(), color.green(), color.blue()));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ITEM_SPECIFICS);
        stack.setItemMeta(meta);
        return stack;
    }
}
