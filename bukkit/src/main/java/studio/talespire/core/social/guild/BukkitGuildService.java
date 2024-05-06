package studio.talespire.core.social.guild;

import me.andyreckt.raspberry.RaspberryPaper;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.rank.command.param.RankParameter;
import studio.talespire.core.social.guild.command.GuildCommand;
import studio.talespire.core.social.guild.command.param.GuildParameter;
import studio.talespire.core.social.guild.model.Guild;

/**
 * @author Moose1301
 * @date 5/5/2024
 */
public class BukkitGuildService {


    public BukkitGuildService(JavaPlugin plugin) {
        Universe.get(RaspberryPaper.class).getCommandHandler().registerTypeAdapter(Guild.class, new GuildParameter());
        CommandUtil.registerAll(new GuildCommand());
    }
}
