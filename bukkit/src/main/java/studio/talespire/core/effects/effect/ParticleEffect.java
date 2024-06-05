package studio.talespire.core.effects.effect;

import org.bukkit.Particle;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class ParticleEffect extends Effect {

    public ParticleEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.VILLAGER_ANGRY;
        period = 1;
        iterations = 1;
    }

    @Override
    public void onRun() {
        display(particle, getLocation());
    }
}
