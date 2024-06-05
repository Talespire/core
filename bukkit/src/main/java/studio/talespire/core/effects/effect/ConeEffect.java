package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.MathUtils;
import studio.talespire.core.effects.util.RandomUtils;
import studio.talespire.core.effects.util.VectorUtils;

public class ConeEffect extends Effect {

    /**
     * Growing per iteration in the length (0.05)
     */
    public float lengthGrow = 0.05F;

    /**
     * Radials per iteration to spawn the next particle (PI / 16)
     */
    public double angularVelocity = Math.PI / 16;

    /**
     * Cone-particles per interation (10)
     */
    public int particles = 10;

    /**
     * Growth in blocks per iteration on the radius (0.006)
     */
    public float radiusGrow = 0.006F;

    /**
     * Conesize in particles per cone
     */
    public int particlesCone = 180;

    /**
     * Start-angle or rotation of the cone
     */
    public double rotation = 0;

    /**
     * Randomize every cone on creation (false)
     */
    public boolean randomize = false;

    /**
     * Solid cone
     */
    public boolean solid = false;

    /**
     * Amount of strands
     */
    public int strands = 1;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    public ConeEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.FLAME;
        period = 1;
        iterations = 200;
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

        double angle;
        float radius;
        float length;

        Vector v;

        for (int x = 0; x < particles; x++) {

            if (step > particlesCone) step = 0;
            if (randomize && step == 0) rotation = RandomUtils.getRandomAngle();
            for (int y = 0; y < strands; y++) {
                angle = step * angularVelocity + rotation + (2 * Math.PI * y / strands);
                radius = step * radiusGrow;
                if (solid) {
                    radius *= RandomUtils.random.nextFloat();
                }
                length = step * lengthGrow;

                v = new Vector(Math.cos(angle) * radius, length, Math.sin(angle) * radius);
                VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
                VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

                location.add(v);
                display(particle, location);
                location.subtract(v);
            }
            step++;
        }
    }

}
