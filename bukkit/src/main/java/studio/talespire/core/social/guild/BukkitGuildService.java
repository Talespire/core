package studio.talespire.core.social.guild;

import lombok.Getter;
import me.andyreckt.raspberry.RaspberryPaper;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.ChatChannelService;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.rank.command.param.RankParameter;
import studio.talespire.core.social.guild.chat.GuildChatChannel;
import studio.talespire.core.social.guild.command.GuildCommand;
import studio.talespire.core.social.guild.command.param.GuildParameter;
import studio.talespire.core.social.guild.listener.GuildPacketListener;
import studio.talespire.core.social.guild.model.Guild;

/**
 * @author Moose1301
 * @date 5/5/2024
 */
@Getter
public class BukkitGuildService {
    private final GuildChatChannel channel = new GuildChatChannel();

    public BukkitGuildService(JavaPlugin plugin) {
        Universe.get(RaspberryPaper.class).getCommandHandler().registerTypeAdapter(Guild.class, new GuildParameter());
        CommandUtil.registerAll(new GuildCommand());
        Universe.get(RedisService.class).registerListener(new GuildPacketListener());
        Universe.get(ChatChannelService.class).registerChatChannel(channel);
    }
}
