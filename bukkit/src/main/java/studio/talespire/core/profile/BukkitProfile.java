package studio.talespire.core.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import studio.talespire.core.CoreBukkit;
import studio.talespire.core.CorePlugin;
import studio.talespire.core.profile.utils.BukkitProfileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class BukkitProfile extends Profile{
    private transient PermissionAttachment attachment = null;
    private transient boolean applying = false;
    public BukkitProfile(UUID uuid, String username) {
        super(uuid, username);
    }

    @Override
    public void apply() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Cannot apply permissions on the main thread (" + getUuid().toString() + ")");
        }
        synchronized (this) {
            if (this.applying) {
                return;
            }
            Player player = getPlayer();
            this.applying = true;
            try {
                JavaPlugin plugin = CorePlugin.getInstance();
                if (this.attachment == null) {
                    this.attachment = player.addAttachment(plugin);
                } else {
                    List<String> permissions = new ArrayList<>(this.attachment.getPermissions().keySet());
                    for (String permission : permissions) {
                        this.attachment.unsetPermission(permission);
                    }
                }
                for (Map.Entry<String, Boolean> entry : getPermissions().entrySet()) {
                    this.attachment.setPermission(entry.getKey(), entry.getValue());
                }
                BukkitProfileUtils.updatePlayerDisplay(player, this);
            } catch (Exception ex) {
                CorePlugin.getInstance().getLogger().severe("Failed to apply permissions to player " + getUsername());
                ex.printStackTrace();
            } finally {

                this.applying = false;
            }

        }
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
