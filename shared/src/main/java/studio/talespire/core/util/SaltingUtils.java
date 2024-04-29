package studio.talespire.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
public class SaltingUtils {
    private static final String KEY = "cJ104pn62hVpcY2HD";

    public static String salt(String input) {
        String saltedIP = input + "_" + KEY;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(saltedIP.getBytes());
            final StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                final String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
