package studio.talespire.core.profile.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @date 4/27/2024
 * @author Moose1301
 */

@Getter @Setter
public class Punishment {
    @SerializedName("_id")
    private final UUID id;
    private final PunishmentType type;
    private final String reason;
    private final long duration;
    private final UUID punishedBy;
    private final long punishedAt;

    private Long expiresAt;

    private boolean removed;
    private UUID removedBy;
    private Long removedAt;
    private String removedReason;

    public Punishment(UUID id, PunishmentType type, String reason, long duration, UUID punishedBy, long punishedAt) {
        this.id = id;
        this.type = type;
        this.reason = reason;
        this.duration = duration;
        this.punishedBy = punishedBy;
        this.punishedAt = punishedAt;

        if(duration != -1) {
            expiresAt = System.currentTimeMillis() + duration;
        }
    }


    public boolean isActive() {
        return this.removedAt == null && (this.expiresAt == null || System.currentTimeMillis() < this.expiresAt);
    }

    public boolean isPermanent() {
        return expiresAt == null;
    }

    public long getRemaining() {

        if (this.expiresAt == null) {
            return 1L;
        }

        return this.expiresAt - System.currentTimeMillis();
    }
}
