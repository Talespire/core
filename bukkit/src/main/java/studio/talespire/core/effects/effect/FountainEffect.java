package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class FountainEffect extends Effect {

    /**
     * Amount of strands (10)
     */
    public int strands = 10;

    /**
     * Particles per iteration per strand (100)
     */
    public int particlesStrand = 150;

    /**
     * Particles per iteration in the spout
     */
    public int particlesSpout = 200;

    /**
     * Radius of strands in blocks
     */
    public float radius = 5;

    /**
     * Radius of spout as a fraction of the fountain (0.1)
     */
    public float radiusSpout = 0.1F;

    /**
     * Height of the fountain
     */
    public float height = 3;

    /**
     * Height of the spout in blocks
     */
    public float heightSpout = 7;

    /**
     * Rotation of the fountain on the Y-Axis (Fraction of PI)
     */
    public double rotation = Math.PI / 4;

    public FountainEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.WATER_SPLASH;
        period = 2;
        iterations = 100;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        double angle;
        float ratio;
        double x, y, z;
        Vector v;

        for (int i = 1; i <= strands; i++) {
            angle = 2 * i * Math.PI / strands + rotation;
            for (int j = 1; j <= particlesStrand; j++) {

                ratio = (float) j / particlesStrand;

                x = Math.cos(angle) * radius * ratio;
                y = Math.sin(Math.PI * j / particlesStrand) * height;
                z = Math.sin(angle) * radius * ratio;

                location.add(x, y, z);
                display(particle, location);
                location.subtract(x, y, z);
            }
        }

        for (int i = 0; i < particlesSpout; i++) {
            v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextFloat() * radius * radiusSpout);
            v.setY(RandomUtils.random.nextFloat() * heightSpout);
            location.add(v);
            display(particle, location);
            location.subtract(v);
        }
    }

}
