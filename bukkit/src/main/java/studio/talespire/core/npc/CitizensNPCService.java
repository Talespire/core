package studio.talespire.core.npc;

import net.citizensnpcs.api.npc.AbstractNPC;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.npc.command.DebugCommand;

/**
 * @author Moose1301
 * @date 6/4/2024
 */
public class CitizensNPCService {
    public CitizensNPCService(JavaPlugin plugin) {
        CommandUtil.registerAll(new DebugCommand());
    }
    public void hideNPC(NPC npc, Player player) {
        PlayerFilter filter = npc.getOrAddTrait(PlayerFilter.class);

        filter.removePlayer(player.getUniqueId());
    }
    public void showNPC(NPC npc, Player player) {
        PlayerFilter filter = npc.getOrAddTrait(PlayerFilter.class);

        filter.addPlayer(player.getUniqueId());
    }
    public void markAutoHide(NPC npc) {
        PlayerFilter filter = npc.getOrAddTrait(PlayerFilter.class);

        filter.setAllowlist();
        npc.addTrait(filter);

    }

}
