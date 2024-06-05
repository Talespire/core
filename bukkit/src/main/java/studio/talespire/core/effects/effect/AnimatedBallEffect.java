package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.MathUtils;
import studio.talespire.core.effects.util.VectorUtils;

public class AnimatedBallEffect extends Effect {

    /**
     * Ball particles total (150)
     */
    public int particles = 150;

    /**
     * The amount of particles, displayed in one iteration (10)
     */
    public int particlesPerIteration = 10;

    /**
     * Size of this ball (1)
     */
    public float size = 1F;

    /**
     * Factors (1, 2, 1)
     */
    public float xFactor = 1F, yFactor = 2F, zFactor = 1F;

    /**
     * Offsets (0, 0.8, 0)
     */
    public float xOffset, yOffset = 0.8F, zOffset;

    /**
     * Rotation of the ball.
     */
    public double xRotation, yRotation, zRotation = 0;

    /**
     * Internal Counter
     */
    protected int step = 0;

    public AnimatedBallEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.SPELL_WITCH;
        iterations = 500;
        period = 1;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Vector vector = new Vector();
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        float t;
        float r;
        float s;

        for (int i = 0; i < particlesPerIteration; i++) {
            step++;

            t = (MathUtils.PI / particles) * step;
            r = MathUtils.sin(t) * size;
            s = 2 * MathUtils.PI * t;

            vector.setX(xFactor * r * MathUtils.cos(s) + xOffset);
            vector.setZ(zFactor * r * MathUtils.sin(s) + zOffset);
            vector.setY(yFactor * size * MathUtils.cos(t) + yOffset);

            VectorUtils.rotateVector(vector, xRotation, yRotation, zRotation);

            display(particle, location.add(vector));
            location.subtract(vector);
        }
    }

}
