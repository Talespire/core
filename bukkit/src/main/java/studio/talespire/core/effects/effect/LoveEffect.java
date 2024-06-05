package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class LoveEffect extends Effect {

    public LoveEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.HEART;
        period = 2;
        iterations = 600;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
        location.add(0, RandomUtils.random.nextFloat() * 2, 0);
        display(particle, location);
    }

}
