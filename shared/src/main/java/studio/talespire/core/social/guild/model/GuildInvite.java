package studio.talespire.core.social.guild.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/4/2024
 */

public record GuildInvite(UUID playerId, long invitedAt, UUID invitedBy) {
}
