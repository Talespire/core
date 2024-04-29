package studio.talespire.core.profile.setting;

import lombok.Data;
import lombok.Getter;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public record SettingOption<T>(String name, T value) {
}
