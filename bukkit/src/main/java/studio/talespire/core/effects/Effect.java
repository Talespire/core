package studio.talespire.core.effects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.talespire.core.effects.util.DynamicLocation;
import studio.talespire.core.effects.util.ParticleOptions;
import studio.talespire.core.effects.util.RandomUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Disunion
 * @date 6/4/2024
 */

@Getter
@Setter
public abstract class Effect implements Runnable {

    protected final EffectService effectService;
    protected String subEffectClass = null;
    protected DynamicLocation origin = null;
    protected DynamicLocation target = null;
    protected int maxIterations;
    private boolean done = false;
    private boolean playing = false;
    private long startTime;

    public EffectType type = EffectType.INSTANT;
    public Particle particle;
    public Color color = null;
    public List<Color> colorList = null;
    public String colors = null;
    public Color toColor = null;
    public List<Color> toColorList = null;
    public String toColors = null;
    public int shriekDelay;
    public float sculkChargeRotation;
    public int arrivalTime;
    public ConfigurationSection subEffect = null;
    public float speed = 0;
    public float particleData = 0;
    public int delay = 0;
    public int period = 1;
    public int iterations = 0;
    public Integer duration = null;
    public double probability = 1;
    public Runnable callback = null;
    public float visibleRange = 32;
    public boolean autoOrient = false;
    public Vector offset = null;
    public Vector relativeOffset = null;
    public Vector targetOffset = null;
    public float yawOffset = 0;
    public float pitchOffset = 0;
    public Float yaw = null;
    public Float pitch = null;
    public boolean updateLocations = true;
    public boolean updateDirections = true;
    public List<Player> targetPlayers;
    public Material material;
    public byte materialData;
    public String blockData;
    public long blockDuration;
    public int particleCount = 1;
    public float particleOffsetX = 0;
    public float particleOffsetY = 0;
    public float particleOffsetZ = 0;
    public float particleSize = 1;
    public boolean asynchronous = true;
    public boolean disappearWithOriginEntity = false;
    public boolean disappearWithTargetEntity = false;

    /**
     * Type of effect being played.
     *
     * @see de.slikey.effectlib.EffectType
     */
    @Nonnull
    public EffectType getType() {
        return type;
    }

    /**
     * ParticleType of spawned particle.
     */
    @Nullable
    public Particle getParticle() {
        return particle;
    }

    /**
     * Can be used to colorize certain particles. As of 1.8, those
     * include SPELL_MOB_AMBIENT, SPELL_MOB and REDSTONE.
     */
    @Nullable
    public Color getColor() {
        return color;
    }

    /**
     * Used for dust particles in 1.17 and up, to make a color transition.
     */
    @Nullable
    public Color getToColor() {
        return toColor;
    }

    /**
     * Used only by the shriek particle in 1.19 and up.
     */
    public int getShriekDelay() {
        return shriekDelay;
    }

    /**
     * Used only by the sculk_charge particle in 1.19 and up.
     */
    public float getSculkChargeRotation() {
        return sculkChargeRotation;
    }

    /**
     * Used only by the vibration particle in 1.17 and up.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Class of subeffect to play at effect location.
     */
    @Nullable
    public String getSubEffectClass() {
        return subEffectClass;
    }

    /**
     * Config section of subeffect to play at effect location.
     */
    @Nullable
    public ConfigurationSection getSubEffect() {
        return subEffect;
    }

    /**
     * This can be used to give particles a set speed when spawned.
     * This will not work with colored particles.
     */
    @Deprecated
    public float getSpeed() {
        return speed;
    }

    /**
     * If set to false, Entity-bound directions will not update during the Effect
     */
    public boolean canUpdateDirections() {
        return updateDirections;
    }

    /**
     * A specific player who should see this effect.
     */
    public Player getTargetPlayer() {
        if (targetPlayers != null && !targetPlayers.isEmpty()) {
            return targetPlayers.get(0);
        }
        return null;
    }

    public void setTargetPlayer(Player targetPlayer) {
        if (targetPlayer == null) {
            this.targetPlayers = null;
        } else {
            List<Player> list = new ArrayList<>();
            list.add(targetPlayer);
            setTargetPlayers(list);
        }
    }


    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is centered upon.
     *
     * This may return null, even for an Effect that was set with an Entity,
     * if the Entity gets GC'd.
     */
    public Entity getEntity() {
        return origin == null ? null : origin.getEntity();
    }
    public void setEntity(Entity entity) {
        setDynamicOrigin(new DynamicLocation(entity));
    }

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is targeted upon. This is probably a very rare case, such as
     * an Effect that "links" two Entities together somehow. (Idea!)
     *
     * This may return null, even for an Effect that was set with a target Entity,
     * if the Entity gets GC'd.
     */
    public Entity getTargetEntity() {
        return target == null ? null : target.getEntity();
    }

    public void setTargetEntity(Entity entity) {
        target = new DynamicLocation(entity);
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "root" Location of the effect.
     *
     * This method will not return null when called from onRun. Effects
     * with invalid locations will be cancelled.
     */
    public final Location getLocation() {
        return origin == null ? null : origin.getLocation();
    }

    public void setLocation(Location location) {
        setDynamicOrigin(new DynamicLocation(location));
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "target" Location of the effect.
     *
     * Unlike getLocation, this may return null.
     */
    public final Location getTarget() {
        return target == null ? null : target.getLocation();
    }

    public void setTarget(Location location) {
        target = new DynamicLocation(location);
    }

    /**
     * @deprecated Use {@link #setTarget(Location)}
     */
    public void setTargetLocation(Location location) {
        target = new DynamicLocation(location);
    }

    public DynamicLocation getDynamicOrigin() {
        return origin;
    }

    /**
     * Set the Location this Effect is centered on.
     */
    public void setDynamicOrigin(DynamicLocation location) {
        if (location == null) throw new IllegalArgumentException("Origin Location cannot be null!");
        origin = location;

        if (offset != null) origin.addOffset(offset);
        if (relativeOffset != null) origin.addRelativeOffset(relativeOffset);

        origin.setDirectionOffset(yawOffset, pitchOffset);
        origin.setYaw(yaw);
        origin.setPitch(pitch);
        origin.setUpdateLocation(updateLocations);
        origin.setUpdateDirection(updateDirections);
        origin.updateDirection();
    }

    public DynamicLocation getDynamicTarget() {
        return target;
    }

    /**
     * Set the Location this Effect is targeting.
     */
    public void setDynamicTarget(DynamicLocation location) {
        target = location;
        if (target != null && targetOffset != null) target.addOffset(targetOffset);
        if (target == null) return;
        target.setUpdateLocation(updateLocations);
        target.setUpdateDirection(updateDirections);
    }

    /**
     * Should this effect stop playing if the origin entity becomes invalid?
     */
    public boolean canDisappearWithOriginEntity() {
        return disappearWithOriginEntity;
    }

    /**
     * Should this effect stop playing if the target entity becomes invalid?
     */
    public boolean canDisappearWithTargetEntity() {
        return disappearWithTargetEntity;
    }

    public final boolean isDone() {
        return done;
    }

    public Effect(EffectService effectService) {
        if (effectService == null) throw new IllegalArgumentException("EffectManager cannot be null!");

        this.effectService = effectService;
        visibleRange = effectService.getParticleRange();
    }

    protected List<Color> parseColorList(String colors) {
        List<Color> colorList = new ArrayList<>();
        String[] args = colors.split(",");
        if (args.length >= 1) {
            for (String str : args) {
                try {
                    int rgb = Integer.parseInt(str.trim().replace("#", ""), 16);
                    colorList.add(Color.fromRGB(rgb));
                } catch (NumberFormatException ignored) {}
            }
        }
        return colorList;
    }

    protected void initialize() {
        if (period < 1) period = 1;

        if (colors != null) {
            colorList = parseColorList(colors);
        }
        if (toColors != null) {
            toColorList = parseColorList(toColors);
        }

        if (subEffect != null) {
            subEffectClass = subEffect.getString("subEffectClass");
        }
    }

    public final void cancel() {
        cancel(true);
    }

    public final void cancel(boolean callback) {
        if (callback) done();
        else done = true;
    }

    public abstract void onRun();

    /**
     * Called when this effect is done playing (when {@link #done()} is called).
     */
    public void onDone() { }

    @Override
    public final void run() {
        if (!validate()) {
            cancel();
            return;
        }

        if (done) {
            effectService.removeEffect(this);
            return;
        }

        try {
            if (RandomUtils.checkProbability(probability)) {
                onRun();
            }
        } catch (Exception ex) {
            done();
            effectService.onError(ex);
        }

        if (type == EffectType.REPEATING) {
            if (iterations == -1) return;
            iterations--;
            if (iterations < 1) done();
        } else {
            done();
        }
    }

    /**
     * Effects should override this if they want to be reusable, this is called prior to starting so
     * state can be reset.
     */
    protected void reset() {
        done = false;
    }

    public void prepare() {
        reset();
        updateDuration();
    }

    public final void start() {
        prepare();
        effectService.start(this);
        playing = true;
    }

    public final void infinite() {
        type = EffectType.REPEATING;
        iterations = -1;
    }

    protected final boolean validate() {
        // Check if the origin and target entities are present
        if (disappearWithOriginEntity && (origin != null && !origin.hasValidEntity())) return false;
        if (disappearWithTargetEntity && (target != null && !target.hasValidEntity())) return false;

        // Check for a valid Location
        updateLocation();
        updateTarget();
        Location location = getLocation();
        if (location == null) return false;
        if (autoOrient) {
            Location targetLocation = target == null ? null : target.getLocation();
            if (targetLocation != null) {
                Vector direction = targetLocation.toVector().subtract(location.toVector());
                location.setDirection(direction);
                targetLocation.setDirection(direction.multiply(-1));
            }
        }

        return true;
    }

    protected void updateDuration() {
        if (duration != null) {
            if (period < 1) period = 1;
            iterations = duration / period / 50;
        }
        maxIterations = iterations;
    }

    protected void updateLocation() {
        if (origin != null) origin.update();
    }

    protected void updateTarget() {
        if (target != null) target.update();
    }

    protected void display(Particle effect, Location location) {
        display(effect, location, color);
    }

    protected void display(Particle particle, Location location, Color color) {
        display(particle, location, color, particleData != 0 ? particleData : speed, particleCount);
    }

    protected void display(Particle particle, Location location, float speed, int amount) {
        display(particle, location, color, speed, amount);
    }

    protected void display(Particle particle, Location location, Color color, float speed, int amount) {
        display(particle, location, color, toColor, speed, amount);
    }

    protected void display(Particle particle, Location location, Color color, Color toColor, float speed, int amount) {
        // display particles only when particleCount is equal or more than 0
        if (particleCount >= 0) {
            Color currentColor = color;
            if (colorList != null && !colorList.isEmpty()) {
                currentColor = colorList.get(ThreadLocalRandom.current().nextInt(colorList.size()));
            }

            Color currentToColor = toColor;
            if (toColorList != null && !toColorList.isEmpty()) {
                currentToColor = toColorList.get(ThreadLocalRandom.current().nextInt(colorList.size()));
            }

            ParticleOptions options = new ParticleOptions(particleOffsetX, particleOffsetY, particleOffsetZ, speed, amount, particleSize, currentColor, currentToColor, arrivalTime, material, materialData, blockData, blockDuration, shriekDelay, sculkChargeRotation);
            options.target = target;

            effectService.display(particle, options, location, visibleRange, targetPlayers);
        }

        if (subEffectClass != null) effectService.start(subEffectClass, subEffect, location);
    }

    private void done() {
        playing = false;
        done = true;
        effectService.done(this);
        targetPlayers = null;
        onDone();
    }

    public void reloadParameters() {
        // This can be implemented by any effect that needs to reset state when in an inner loop of a Modified effect
    }

}
