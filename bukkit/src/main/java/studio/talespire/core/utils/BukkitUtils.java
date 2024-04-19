package studio.talespire.core.utils;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class BukkitUtils {

    public static String getIp() {
        if(System.getenv().containsKey("SERVER_IP")) {
            return System.getenv("SERVER_IP");
        }
        File ipFile = new File("ip.dat");
        if(ipFile.exists()) {
            try {
                return Files.readString(ipFile.toPath());
            } catch (IOException e) {
            }
        }
        return Bukkit.getIp();
    }
}
