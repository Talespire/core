package studio.talespire.core.effects.effect;

import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.EntityType;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.inventory.meta.FireworkMeta;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class BigBangEffect extends Effect {

    public FireworkEffect.Type fireworkType = FireworkEffect.Type.BURST;
    public Color color2 = Color.ORANGE;
    public Color color3 = Color.BLACK;
    public Color fadeColor = Color.BLACK;
    public int intensity = 2;
    public float radius = 2;
    public int explosions = 10;
    public int soundInterval = 5;
    public Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
    public float soundVolume = 100;
    public float soundPitch = 1;
    protected int step = 0;

    protected FireworkEffect firework;

    public BigBangEffect(EffectService effectService) {
        super(effectService);
        color = Color.RED;
        type = EffectType.REPEATING;
        period = 2;
        iterations = 400;
        asynchronous = false;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Vector v;

        if (location == null || location.getWorld() == null) {
            cancel();
            return;
        }

        if (firework == null) {
            Builder b = FireworkEffect.builder().with(fireworkType);
            b.withColor(color).withColor(color2).withColor(color3);
            b.withFade(fadeColor);
            b.trail(true);
            firework = b.build();
        }

        for (int i = 0; i < explosions; i++) {
            v = RandomUtils.getRandomVector().multiply(radius);
            detonate(location, v);
            if (soundInterval != 0 && step % soundInterval == 0) {
                location.getWorld().playSound(location, sound, soundVolume, soundPitch);
            }
        }
        step++;
    }

    protected void detonate(Location location, Vector v) {
        if (location != null && location.getWorld() != null) {
            final Firework firework = (Firework) location.getWorld().spawnEntity(location.add(v), EntityType.FIREWORK);
            location.subtract(v);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(0);
            for (int i = 0; i < intensity; i++) {
                meta.addEffect(this.firework);
            }
            firework.setFireworkMeta(meta);
            firework.detonate();
        }
    }

}
