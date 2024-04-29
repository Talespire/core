package studio.talespire.core.rank.command.param;

import me.andyreckt.raspberry.adapter.RaspberryTypeAdapter;
import me.andyreckt.raspberry.command.CommandIssuer;
import me.andyreckt.raspberry.exception.InvalidArgumentException;
import org.bukkit.ChatColor;
import studio.talespire.core.rank.Rank;
import studio.talespire.core.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class RankParameter implements RaspberryTypeAdapter<Rank> {
    @Override
    public Rank transform(CommandIssuer commandIssuer, String source, String... options) throws InvalidArgumentException {
        try {
            return Rank.valueOf(source.toUpperCase());
        } catch (Exception ex) {
            throw new InvalidArgumentException(ChatColor.YELLOW + "Unknown rank " + source);
        }
    }

    @Override
    public List<String> complete(CommandIssuer sender, String source, String... options) {
        return Arrays.stream(Rank.values()).map(rank -> StringUtils.capitalize(rank.name().toLowerCase(Locale.ROOT))).toList();
    }
}
