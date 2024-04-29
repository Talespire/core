package studio.talespire.core.profile.grant;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
@Getter
@Setter
public abstract class Grant {
    @SerializedName("_id")
    private final UUID id;
    private final GrantType type;
    private final String reason;
    private final long duration;
    private final UUID grantedBy;
    private final long grantedAt;

    private Long expiresAt;

    private boolean removed;
    private UUID removedBy;
    private Long removedAt;
    private String removedReason;

    public Grant(UUID id, GrantType type, String reason, long duration, UUID grantedBy, long grantedAt) {
        this.id = id;
        this.type = type;
        this.reason = reason;
        this.duration = duration;
        this.grantedBy = grantedBy;
        this.grantedAt = grantedAt;

        if (duration != -1) {
            expiresAt = System.currentTimeMillis() + duration;
        }
    }


    public boolean isActive() {
        return this.removedAt == null && (this.expiresAt == null || System.currentTimeMillis() < this.expiresAt);
    }

    public boolean isPermanent() {
        return this.expiresAt == null;
    }

    public long getRemaining() {

        if (this.expiresAt == null) {
            return 1L;
        }

        return this.expiresAt - System.currentTimeMillis();
    }

    public void remove(UUID removedBy, long removedAt, String reason) {
        this.removed = true;
        this.removedBy = removedBy;
        this.removedAt = removedAt;
        this.removedReason = reason;

    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Grant grant) {
            return this.id.equals(grant.getId());
        }
        return false;
    }
}
