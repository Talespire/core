package studio.talespire.core.effects.effect;

import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.Particle;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class ExplodeEffect extends Effect {

    public Particle particle1 = Particle.EXPLOSION_NORMAL;
    public Particle particle2 = Particle.EXPLOSION_HUGE;

    /**
     * Amount of spawned smoke-sparks
     */
    public int amount = 25;
    public Sound sound = Sound.ENTITY_GENERIC_EXPLODE;

    public ExplodeEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.INSTANT;
        speed = 0.5F;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null || location.getWorld() == null) {
            cancel();
            return;
        }

        location.getWorld().playSound(location, sound, 4.0F, (1.0F + (RandomUtils.random.nextFloat() - RandomUtils.random.nextFloat()) * 0.2F) * 0.7F);
        display(particle1, location);
        display(particle2, location);
    }

}
