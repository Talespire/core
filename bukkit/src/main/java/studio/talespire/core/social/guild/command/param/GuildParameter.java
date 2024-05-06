package studio.talespire.core.social.guild.command.param;

import me.andyreckt.raspberry.adapter.RaspberryTypeAdapter;
import me.andyreckt.raspberry.command.CommandIssuer;
import me.andyreckt.raspberry.exception.InvalidArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
public class GuildParameter implements RaspberryTypeAdapter<Guild> {
    public static final String DEFAULT_VALUE_SELF = "fb8f1450-b739-4199-88ce-23ee73eb2bbc";
    @Override
    public Guild transform(CommandIssuer commandIssuer, String source, String... options) throws InvalidArgumentException {
        if(!commandIssuer.isPlayer()) {
            return null;
        }
        Player player = (Player) commandIssuer.getIssuer();

        if (source.equals(DEFAULT_VALUE_SELF)) {
            Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
            Guild guild = Universe.get(GuildService.class).getGuild(profile.getGuildId());
            if(guild == null) {
                throw new InvalidArgumentException("You aren't in a guild");
            }
            return guild;
        }
        Guild guild = Universe.get(GuildService.class).getGuild(source);
        if(guild == null) {
            throw new InvalidArgumentException("Could not find a guild with the name " + source);
        }
        return guild;
    }

}
