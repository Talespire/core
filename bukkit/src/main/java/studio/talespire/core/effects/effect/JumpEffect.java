package studio.talespire.core.effects.effect;

import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class JumpEffect extends Effect {

    /**
     * Power of jump. (0.5F)
     */
    public float power = 0.5F;

    public JumpEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.INSTANT;
        period = 20;
        iterations = 1;
        asynchronous = false;
    }

    @Override
    public void onRun() {
        Entity entity = getEntity();
        if (entity == null) {
            cancel();
            return;
        }
        Vector v = entity.getVelocity();
        v.setY(v.getY() + power);
        entity.setVelocity(v);
    }

}
