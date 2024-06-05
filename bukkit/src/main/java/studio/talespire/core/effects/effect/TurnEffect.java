package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class TurnEffect extends Effect {

    /**
     * Angular movement per iteration
     */
    public float step = 11.25F;

    public TurnEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        period = 1;
        iterations = (int) (360 * 5 / step);
        asynchronous = false;
    }

    @Override
    public void onRun() {
        Entity entity = getEntity();
        if (entity == null) {
            cancel();
            return;
        }
        Location loc = entity.getLocation();
        loc.setYaw(loc.getYaw() + step);
        entity.teleport(loc);
    }

}
