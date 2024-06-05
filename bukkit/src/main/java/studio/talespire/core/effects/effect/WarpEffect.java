package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class WarpEffect extends Effect {

    /**
     * Radius of the spawned circles
     */
    public float radius = 1;

    /**
     * Particles per circle
     */
    public int particles = 20;

    /**
     * Interval of the circles
     */
    public float grow = 0.2F;

    /**
     * Circles to display
     */
    public int rings = 12;

    /**
     * Internal counter
     */
    protected int step = 0;

    public WarpEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.FIREWORKS_SPARK;
        period = 2;
        iterations = rings;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        if (step > rings) step = 0;

        double x, y, z;
        double angle;

        y = step * grow;
        location.add(0, y, 0);

        for (int i = 0; i < particles; i++) {
            angle = (double) 2 * Math.PI * i / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            location.add(x, 0, z);
            display(particle, location);
            location.subtract(x, 0, z);
        }
        location.subtract(0, y, 0);
        step++;
    }

}
