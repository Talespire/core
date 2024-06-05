package studio.talespire.core.npc.command;

import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.npc.CitizensNPCService;

/**
 * @author Moose1301
 * @date 6/4/2024
 */
public class DebugCommand {
    @Command(names = "test123-mark")
    public void handleMark(Player player, @Param(name = "id") int id) {
        CitizensNPCService service = Universe.get(CitizensNPCService.class);
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        if(npc == null) {
            return;
        }
        service.markAutoHide(npc);
        player.sendMessage("Trying: " + 31);
    }
    @Command(names = "test123")
    public void handle(Player player, @Param(name = "id") int id, @Param(name = "visible") boolean enabled) {
        CitizensNPCService service = Universe.get(CitizensNPCService.class);
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        if(npc == null) {
            return;
        }
        if(enabled) {
            player.sendMessage("Trying: " + 24);
            service.showNPC(npc, player);
        } else {
            player.sendMessage("Trying: " + 28);
            service.hideNPC(npc, player);
        }
        player.sendMessage("Trying: " + 31);
    }
}
