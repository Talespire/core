package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class SmokeEffect extends Effect {

    /**
     * Number of particles to display
     */
    public int particles = 20;

    public SmokeEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.SMOKE_NORMAL;
        period = 1;
        iterations = 300;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        for (int i = 0; i < particles; i++) {
            location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
            location.add(0, RandomUtils.random.nextFloat() * 2, 0);
            display(particle, location);
        }
    }

}
