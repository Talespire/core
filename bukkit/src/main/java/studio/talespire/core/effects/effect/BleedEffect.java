package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class BleedEffect extends Effect {

    /**
     * Play the Hurt Effect for the Player
     */
    public boolean hurt = true;

    /**
     * Height of the blood spurt
     */
    public double height = 1.75;

    /**
     * Color of blood. Default is red (152)
     */
    public Material material = Material.REDSTONE_BLOCK;

    public BleedEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 25;
    }

    @Override
    public void onRun() {
        // Location to spawn the blood-item.
        Location location = getLocation();

        if (location == null || location.getWorld() == null) {
            cancel();
            return;
        }

        location.add(0, RandomUtils.random.nextFloat() * height, 0);
        location.getWorld().playEffect(location, org.bukkit.Effect.STEP_SOUND, material);

        Entity entity = getEntity();
        if (hurt && entity != null) entity.playEffect(org.bukkit.EntityEffect.HURT);
    }

}
