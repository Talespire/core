package studio.talespire.core.social.guild.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.packet.GuildKickPacket;

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
    private final List<UUID> requests = new ArrayList<>();
    private final Map<GuildPermission, GuildRole> permissions = new HashMap<>();
    private final List<String> motd = new ArrayList<>();


    private UUID leader;
    private String name;
    private String description;
    private String discord = "";
    private boolean mutechat;
    private Set<UUID> mutedPlayers;
    private String tag;
    private TextColor color;

    public Guild(UUID leader, String name) {
        this.uuid = UUID.randomUUID();
        this.leader = leader;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.description = "";
        this.members.put(leader, new GuildMember(leader, this.createdAt, GuildRole.LEADER));
        this.mutedPlayers = new HashSet<>();
        for (GuildPermission value : GuildPermission.values()) {
            this.permissions.put(value, value.getDefaultRole());
        }
        this.tag = null;
        this.color = NamedTextColor.GRAY;
    }

    public boolean isMuted(UUID playerId) {
        if(this.mutechat) {
            return true;
        }
        return this.mutedPlayers.contains(playerId);
    }
    public GuildMember getMember(UUID playerId) {
        return this.members.get(playerId);
    }

    public void addMember(UUID playerId) {
        this.members.put(playerId, new GuildMember(playerId, System.currentTimeMillis(), GuildRole.MEMBER));
    }

    public void removeMember(UUID playerId) {
        this.members.remove(playerId);
    }

    public void setRole(UUID playerId, GuildRole role) {
        if (!members.containsKey(playerId)) {
            return;
        }
        this.members.get(playerId).setRole(role);
    }

    public GuildRole getRole(UUID playerId) {
        if (!members.containsKey(playerId)) {
            return GuildRole.MEMBER;
        }
        return this.members.get(playerId).getRole();
    }

    public boolean isMember(UUID playerId) {
        return this.members.containsKey(playerId);
    }
    public boolean hasPermission(UUID playerId, GuildPermission permission) {
        GuildRole requiredRole = this.permissions.getOrDefault(permission, permission.getDefaultRole());
        return requiredRole.ordinal() <= getRole(playerId).ordinal();
    }
    public boolean hasPermission(UUID playerId, GuildRole requiredRole) {
        return requiredRole.ordinal() >= getRole(playerId).ordinal();
    }

    public void transferLeader(UUID newLeader) {
        setRole(this.leader, GuildRole.CAPTAIN);
        this.leader = newLeader;
        setRole(this.leader, GuildRole.LEADER);
    }

    public boolean hasInvite(UUID invite) {
        return this.invites.containsKey(invite);
    }

    public boolean hasRequest(UUID request) {
        return this.requests.contains(request);
    }

    public void removeInvite(UUID invite) {
        this.invites.remove(invite);
    }

    public void removeRequest(UUID request) {
        this.requests.remove(request);
    }

    public GuildInvite getInvite(UUID invite) {
        return this.invites.get(invite);
    }

    public void createInvite(UUID inviter, UUID targetInvite) {
        this.invites.put(targetInvite, new GuildInvite(inviter, System.currentTimeMillis(), targetInvite));
    }

    public void kickMember(UUID kicker, UUID kicked) {
        Universe.get(GuildService.class).saveGuild(this);
        Universe.get(RedisService.class).publish(new GuildKickPacket(this.uuid, kicker, kicked));


        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(kicked);
        if(profile != null) {
            profile.setGuildId(null);
            Universe.get(ProfileService.class).saveProfile(profile);
        }
    }
}
