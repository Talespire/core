package studio.talespire.core.tablist.util;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Disunion
 * @date 6/7/2024
 */
public class BaseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
    }
}
