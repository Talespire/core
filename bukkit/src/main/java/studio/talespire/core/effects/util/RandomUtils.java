package studio.talespire.core.effects.util;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public final class RandomUtils {

    public static final Random random = ThreadLocalRandom.current();

    private RandomUtils() {
        // No instance allowed
    }

    public static Vector getRandomVector() {
        double u = random.nextDouble();
        double v = random.nextDouble();

        double theta = u * 2 * Math.PI;
        double phi = Math.acos(2 * v - 1);

        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);

        double x = sinPhi * cosTheta;
        double y = sinPhi * sinTheta;
        double z = cosPhi;

        // Going to take it on faith from the math gods that
        // this is always a normal vector
        return new Vector(x, y, z);
    }

    public static Vector getRandomFlatVector() {
        double x, z;
        x = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, 0, z);
    }

    public static Vector getRandomCircleVector() {
        double rnd, x, z;
        rnd = random.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);

        return new Vector(x, 0, z);
    }

    public static Material getRandomMaterial(Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2 * Math.PI;
    }

    public static boolean checkProbability(double probability) {
        return probability >= 1 || random.nextDouble() < probability;
    }

}

