package studio.talespire.core.util;

import java.io.File;

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
    public static String getFileName(File file) {
        String fileName =  file.getName();
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }
}
