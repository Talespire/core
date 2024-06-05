package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class FlameEffect extends Effect {

    public int particles = 10;

    public FlameEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.FLAME;
        period = 1;
        iterations = 600;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Vector v;

        if (location == null) {
            cancel();
            return;
        }

        for (int i = 0; i < particles; i++) {
            v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6D);
            v.setY(RandomUtils.random.nextFloat() * 1.8);
            location.add(v);
            display(particle, location);
            location.subtract(v);
        }
    }

}
