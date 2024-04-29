package studio.talespire.core.profile.model;

import lombok.Getter;

/**
 * @date 4/27/2024
 * @author Moose1301
 */
@Getter
public enum PunishmentType {
    WARN,
    MUTE,
    BAN,
    BLACKLIST;

    private final String context;

    PunishmentType(){
        context = name().toLowerCase() + "ed";
    }
}
