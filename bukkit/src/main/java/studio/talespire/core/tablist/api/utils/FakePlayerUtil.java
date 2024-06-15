package studio.talespire.core.tablist.api.utils;

import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Disunion
 * @date 6/14/2024
 */
@NoArgsConstructor
public class FakePlayerUtil {
    public static String randomName() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 8;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String generatedString = "zz";
        generatedString += random.ints(leftLimit, rightLimit +1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
