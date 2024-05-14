package studio.talespire.core.social.guild.model;

import lombok.Getter;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
@Getter
public enum GuildPermission {
    MOTD(GuildRole.LEADER),
    INVITE(GuildRole.CAPTAIN),
    KICK(GuildRole.CAPTAIN),
    PROMOTE(GuildRole.LEADER),
    DESCRIPTION(GuildRole.LEADER),
    DISCORD(GuildRole.CAPTAIN),
    MUTE(GuildRole.OFFICER),
    MUTE_ALL(GuildRole.CAPTAIN),
    TAG(GuildRole.CAPTAIN);

    private final GuildRole defaultRole;

    GuildPermission(GuildRole defaultRole) {
        this.defaultRole = defaultRole;
    }
}
