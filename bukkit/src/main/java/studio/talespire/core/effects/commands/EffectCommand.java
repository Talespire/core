package studio.talespire.core.effects.commands;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.effect.*;

/**
 * @author Disunion
 * @date 6/4/2024
 */
@Command(names = "effect", description = "Effect command.")
public class EffectCommand {

    @Children(names = {"dna"}, description = "Start DNA effect.")
    public void startDNAEffect(Player player) {
        DnaEffect dnaEffect = new DnaEffect(Universe.get(EffectService.class));
        dnaEffect.setLocation(player.getLocation());
        dnaEffect.radius = 1;
        dnaEffect.length = 2;
        dnaEffect.iterations = 100;
        dnaEffect.start();
    }

    @Children(names = {"music"} , description = "Start music effect.")
    public void startMusicEffect(Player player) {
        MusicEffect musicEffect = new MusicEffect(Universe.get(EffectService.class));
        musicEffect.setLocation(player.getLocation());
        musicEffect.iterations = 100;
        musicEffect.start();
    }

    @Children(names = "tornado", description = "Start tornado effect.")
    public void startTornadoEffect(Player player) {
        TornadoEffect tornadoEffect = new TornadoEffect(Universe.get(EffectService.class));
        tornadoEffect.setLocation(player.getLocation());
        tornadoEffect.tornadoHeight = 2;
        tornadoEffect.maxTornadoRadius = 1;
        tornadoEffect.iterations = 100;
        tornadoEffect.start();
    }

    @Children(names = "heart", description = "Start heart effect.")
    public void startHeartEffect(Player player) {
        HeartEffect heartEffect = new HeartEffect(Universe.get(EffectService.class));
        heartEffect.setLocation(player.getLocation());
        heartEffect.iterations = 100;
        heartEffect.start();
    }

    @Children(names = "circle", description = "Start a circle effect.")
    public void startCircleEffect(Player player) {

        Location location = player.getLocation();
        location.setY(location.getY() - 1);

        CircleEffect circleEffect = new CircleEffect(Universe.get(EffectService.class));
        circleEffect.setLocation(location);
        circleEffect.particleCount = 15;
        circleEffect.enableRotation = false;
        circleEffect.particle = Particle.HEART;
        circleEffect.radius = 3;
        circleEffect.iterations = 100;
        circleEffect.start();
    }

    @Children(names = "wave", description = "Start a wave effect.")
    public void startWaveEffect(Player player, @Param(name = "particle name") String particle) {
        WaveEffect waveEffect = new WaveEffect(Universe.get(EffectService.class));
        waveEffect.setLocation(player.getLocation());
        waveEffect.start();

    }

    @Children(names = "ripple", description = "Start a ripple effect.")
    public void startRippleEffect(Player player) {
        RippleEffect rippleEffect = new RippleEffect(Universe.get(EffectService.class));
        rippleEffect.setLocation(player.getLocation());
        rippleEffect.start();
    }
}
