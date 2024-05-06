package studio.talespire.core.social.guild.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.setting.types.privacy.GuildInviteSetting;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.command.param.GuildParameter;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.packet.GuildChatPacket;
import studio.talespire.core.social.guild.packet.GuildInvitePacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/5/2024
 */

@Command(names = "guild", description = "Guild Commands")
public class GuildCommand {
    @Children(names = "create", permission = "hero", async = true)
    public void handleCreate(Player player, @Param(name = "Name", wildcard = true) String name) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() != null) {
            player.sendMessage(Component.text("You are already in a guild", NamedTextColor.RED));
            return;
        }
        if (!GuildService.NAME_REGEX.matcher(name).matches()) {
            player.sendMessage(Component.text("That is not a valid guild name", NamedTextColor.RED));
            return;
        }

        Guild guild = new Guild(player.getUniqueId(), name);
        Universe.get(GuildService.class).registerGuild(guild);
        profile.setGuildId(guild.getUuid());
        Universe.get(ProfileService.class).saveProfile(profile);
    }

    @Children(names = "info", async = true)
    public void handleInfo(Player player, @Param(name = "Guild", baseValue = GuildParameter.DEFAULT_VALUE_SELF) Guild guild) {
        player.sendMessage(Statics.gson().toJson(guild));
    }

    @Children(names = "invite", async = true)
    public void handleInvite(Player player, @Param(name = "Target") UUID targetId) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        //TODO Perm check
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.INVITE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permissions to do this", NamedTextColor.RED))
            );
            return;
        }
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(targetId);
        if(guild.getInvites().containsKey(targetProfile.getUuid())) {
            player.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" has already been invited to your guild", NamedTextColor.RED))
            );
            return;
        }
        if(!targetProfile.getSetting(GuildInviteSetting.class).value().doesMatch(profile, targetProfile)) {
            player.sendMessage(Component.text("You cannot invite ", NamedTextColor.RED)
                    .append(Component.text(targetProfile.getUsername(), NamedTextColor.YELLOW))
            );
            return;
        }

        guild.createInvite(profile.getUuid(), targetProfile.getUuid());
        Universe.get(RedisService.class).publish(new GuildInvitePacket(guild.getUuid(), profile.getUuid(), targetProfile.getUuid()));
    }
}
