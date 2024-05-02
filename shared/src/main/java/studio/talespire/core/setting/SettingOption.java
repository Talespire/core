package studio.talespire.core.setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public record SettingOption<T>(Component name, T value) {
    public static <T> SettingOption<T> of(String name, T value) {
        return new SettingOption<>(Component.text(name, NamedTextColor.WHITE), value);
    }
}
