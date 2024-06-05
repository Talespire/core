package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class IconEffect extends Effect {

    public int yOffset = 2;

    public IconEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.VILLAGER_ANGRY;
        period = 4;
        iterations = 25;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        location.add(0, yOffset, 0);
        display(particle, location);
    }

}
