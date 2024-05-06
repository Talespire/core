package studio.talespire.core.social.guild.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
@Getter
@Setter
public class Guild {


    @SerializedName("_id")
    private final UUID uuid;
    private final long createdAt;
    private final Map<UUID, GuildMember> members = new HashMap<>();
    private final Map<UUID, GuildInvite> invites = new HashMap<>();
    private final List<String> motd = new ArrayList<>();


    private UUID leader;
    private String name;
    private String description;


    public Guild(UUID uuid, UUID leader, String name) {
        this.uuid = uuid;
        this.leader = leader;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.description = "";
        this.members.put(leader, new GuildMember(leader, this.createdAt, GuildRole.LEADER));
    }

    public void setRole(UUID playerId, GuildRole role) {
        if (!members.containsKey(playerId)) {
            return;
        }
        this.members.get(playerId).setRole(role);
    }

    public void transferLeader(UUID newLeader) {
        setRole(this.leader, GuildRole.CAPTAIN);
        this.leader = newLeader;
        setRole(this.leader, GuildRole.LEADER);
    }

    public boolean hasInvite(UUID invite) {
        return this.invites.containsKey(invite);
    }

    public void removeInvite(UUID invite) {
        this.invites.remove(invite);
    }

    public GuildInvite getInvite(UUID invite) {
        return this.invites.get(invite);
    }
}
