package studio.talespire.core.util;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
public class StringUtils {
    public static String capitalize(String word) {
        if (word.isEmpty()) {
            return word;
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
