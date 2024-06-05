package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.MathUtils;
import studio.talespire.core.effects.util.RandomUtils;
import studio.talespire.core.effects.util.VectorUtils;

public class StarEffect extends Effect {

    /**
     * Particles per spike
     */
    public int particles = 50;

    /**
     * Height of the spikes in blocks
     */
    public float spikeHeight = 3.5F;

    /**
     * Half amount of spikes. Creation is only done half and then mirrored.
     */
    public int spikesHalf = 3;

    /**
     * Inner radius of the star. (0.5)
     */
    public float innerRadius = 0.5F;

    public StarEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.FLAME;
        period = 4;
        iterations = 50;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        float radius = 3 * innerRadius / MathUtils.SQRT_3;

        double xRotation;
        double angle;
        float height;
        Vector v;

        for (int i = 0; i < spikesHalf * 2; i++) {
            xRotation = i * Math.PI / spikesHalf;
            for (int x = 0; x < particles; x++) {
                angle = 2 * Math.PI * x / particles;
                height = RandomUtils.random.nextFloat() * spikeHeight;
                v = new Vector(Math.cos(angle), 0, Math.sin(angle));
                v.multiply((spikeHeight - height) * radius / spikeHeight);
                v.setY(innerRadius + height);

                VectorUtils.rotateAroundAxisX(v, xRotation);
                location.add(v);
                display(particle, location);
                location.subtract(v);

                VectorUtils.rotateAroundAxisX(v, Math.PI);
                VectorUtils.rotateAroundAxisY(v, Math.PI / 2);

                location.add(v);
                display(particle, location);
                location.subtract(v);
            }
        }
    }

}
