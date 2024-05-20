package studio.talespire.core.profile;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.model.Punishment;
import studio.talespire.core.profile.model.PunishmentType;
import studio.talespire.core.profile.packet.friend.ProfileFriendAcceptPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendDenyPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendRequestCancelPacket;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;
import studio.talespire.core.setting.SettingService;
import studio.talespire.core.setting.adapter.SettingMapAdapter;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
@Getter
@Setter
public abstract class Profile {
    @SerializedName("_id")
    protected final UUID uuid;
    protected String username;
    protected final Set<String> ipAddresses;
    protected final Set<Punishment> punishments;
    protected final Set<Grant> grants;

    protected Set<UUID> friends;
    protected Set<UUID> friendRequests;
    protected Set<UUID> ignored;


    protected long firstSeen;
    protected long lastSeen;

    @JsonAdapter(SettingMapAdapter.class)
    protected ConcurrentHashMap<Setting<?>, SettingOption<?>> settings;

    @Nullable
    protected UUID guildId;


    protected transient Rank rank;

    protected transient Map<String, Boolean> permissions;



    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
        this.ipAddresses = new HashSet<>();
        this.punishments = new HashSet<>();
        this.friends = new HashSet<>();
        this.friendRequests = new HashSet<>();
        this.ignored = new HashSet<>();
        this.grants = new HashSet<>();
        this.settings = new ConcurrentHashMap<>();
        for (Setting<?> setting : Universe.get(SettingService.class).getSettings()) {
            this.settings.put(setting, setting.getDefaultValue());
        }
        this.load();
    }
    public void load() {
        for (Setting<?> setting : Universe.get(SettingService.class).getSettings()) {
            if (this.settings.containsKey(setting)) continue;
            this.settings.put(setting, setting.getDefaultValue());
        }
        //Profile from d801e2d and before won't have these
        if(this.friends == null) {
            this.friends = new HashSet<>();
            this.friendRequests = new HashSet<>();
            this.ignored = new HashSet<>();
        }

        this.rank = calculateRank();
        this.refreshPermissions();
    }

    public abstract void apply();


    public Component getFormattedName() {
        return Component.text(this.username, this.rank.getColor());
    }

    public List<Grant> getGrantsByType(GrantType type) {
        return grants.stream().filter(grant -> grant.getType() == type).toList();
    }

    public Grant getGrantById(UUID id) {
        for (Grant grant : this.grants) {
            if (grant.getId().equals(id)) {
                return grant;
            }
        }
        return null;
    }

    public Punishment getPunishmentById(UUID uuid) {
        for (Punishment punishment : this.punishments) {
            if (punishment.getId().equals(uuid)) {
                return punishment;
            }
        }
        return null;
    }

    public List<Punishment> getPunishmentsByType(PunishmentType type) {
        List<Punishment> punishments = new ArrayList<>();
        for (Punishment punishment : this.punishments) {
            if (punishment.getType().equals(type)) {
                punishments.add(punishment);
            }
        }
        return punishments;
    }
    public Punishment  getActivePunishmentByType(PunishmentType type) {
        List<Punishment> punishments = new ArrayList<>();
        for (Punishment punishment : this.punishments) {
            if (punishment.getType().equals(type) && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }


    public Grant getActiveGrant() {
        return this.getGrantsByType(GrantType.RANK).stream()
                .filter(grant -> ((GrantRank) grant).getRank() == rank)
                .max((o1, o2) -> Long.compare(o2.getGrantedAt(), o1.getGrantedAt())).orElse(null);
    }

    public Rank calculateRank() {
        return this.grants.stream()
                .filter(grant -> grant instanceof GrantRank)
                .filter(Grant::isActive)
                .map(grant -> ((GrantRank) grant).getRank())
                .max((o1, o2) -> Integer.compare(o2.ordinal(), o1.ordinal()))
                .orElse(Rank.DEFAULT);
    }

    public Map<String, Boolean> getPermissions() {

        if (this.permissions != null) {
            return this.permissions;
        }

        this.refreshPermissions();

        return this.permissions;
    }

    public void refreshPermissions() {
        List<Rank> ranks = new ArrayList<>();
        List<String> individualPermissions = new ArrayList<>();

        for (Grant grant : this.grants) {
            if (!grant.isActive()) {
                continue;
            }
            if (grant instanceof GrantRank) {
                ranks.add(((GrantRank) grant).getRank());
            } else if (grant instanceof GrantPermission) {
                individualPermissions.add(((GrantPermission) grant).getPermission());
            }
        }
        Map<String, Boolean> permissions = new HashMap<>(Rank.DEFAULT.getCompoundedPermissions());
        ranks.sort((o1, o2) -> Integer.compare(o2.ordinal(), o1.ordinal()));
        for (Rank rank : ranks) {
            permissions.putAll(rank.getCompoundedPermissions());
        }
        for (String individualPermission : individualPermissions) {
            boolean negative = individualPermission.charAt(0) == '-';
            if(negative) {
                permissions.put(individualPermission.substring(0, 1), false);
            } else {
                permissions.put(individualPermission, true);
            }
        }
        this.permissions = permissions;
    }


    @SuppressWarnings("unchecked")
    public <T> SettingOption<T> getSetting(Setting<T> setting) {
        if (!settings.containsKey(setting)) {
            settings.put(setting, setting.getDefaultValue());
        }
        return (SettingOption<T>) settings.get(setting);
    }

    @SuppressWarnings("unchecked")
    public <T> SettingOption<T> getSetting(Class<? extends Setting<T>> setting) {
        Setting<?> settingObject = Universe.get(SettingService.class).getSetting(setting);
        if (!settings.containsKey(settingObject)) {
            settings.put(settingObject, settingObject.getDefaultValue());
        }
        return (SettingOption<T>) settings.get(settingObject);
    }

    public Guild getGuild(){
        if (this.guildId == null) {
            return null;
        }
        return Universe.get(GuildService.class).getGuild(this.guildId);
    }


    public void acceptRequest(UUID senderId) {
        this.friends.add(senderId);
        this.friendRequests.remove(senderId);
    }
    public void denyRequest(UUID senderId) {
        this.friendRequests.remove(senderId);
    }

    public void removeFriend(UUID receiverId) {
        this.friends.remove(receiverId);
    }
    public void addRequest(UUID senderId) {
        this.friendRequests.add(senderId);
    }
}
