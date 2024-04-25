package studio.talespire.core.placeholder;

import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.placeholder.command.PlaceholderCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 4/24/2024
 * @author Moose1301
 */
public class PlaceholderService {
    private final Map<String, PlaceholderResolver> placeholderResolvers;
    private final Pattern placeholderPattern;


    public PlaceholderService() {
        this.placeholderResolvers = new HashMap<>();
        this.placeholderPattern = Pattern.compile("%(.*?)%");

        CommandUtil.registerAll(new PlaceholderCommand());
    }

    public String resolvePlaceholders(Player player, String input) {
        Matcher matcher = placeholderPattern.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            int index = placeholder.indexOf('_');

            if (index != -1) {
                String identifier = placeholder.substring(0, index);
                String key = placeholder.substring(index + 1);
                PlaceholderResolver resolver = placeholderResolvers.get(identifier);

                if (resolver != null) {
                    String value = resolver.resolve(player, key);
                    if (value != null) {
                        matcher.appendReplacement(result, Matcher.quoteReplacement(value));
                    }
                }
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }
    public void registerPlaceholder(String identifier, PlaceholderResolver resolver) {
        placeholderResolvers.put(identifier, resolver);
    }
}
