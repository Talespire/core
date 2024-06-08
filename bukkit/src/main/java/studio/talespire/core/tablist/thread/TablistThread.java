package studio.talespire.core.tablist.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.TabListService;
import studio.talespire.core.tablist.setup.TabLayout;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@Log4j2
@RequiredArgsConstructor
public class TablistThread extends Thread {

    private final TabListService service = Universe.get(TabListService.class);
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            if (!service.getPlugin().isEnabled()) {
                this.terminate();
                return;
            }
            this.tick();

            try {
                Thread.sleep(service.getTicks() * 50L);
            } catch (InterruptedException e) {
                log.error("An error occurred while sleeping the thread", e);
            }
        }
    }

    public void terminate() {
        running = false;
    }

    public void tick() {
        if (!service.getPlugin().isEnabled()) return;

        for (TabLayout layout : service.getLayoutMap().values()) {
            layout.refresh();
        }
    }


}
