package studio.talespire.core.rank;

import me.andyreckt.raspberry.RaspberryPaper;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.rank.command.param.RankParameter;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class BukkitRankService {

    public BukkitRankService(JavaPlugin plugin) {
        Universe.get(RaspberryPaper.class).getCommandHandler().registerTypeAdapter(Rank.class, new RankParameter());
    }
}
