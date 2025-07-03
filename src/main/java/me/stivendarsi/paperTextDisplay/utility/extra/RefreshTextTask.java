package me.stivendarsi.paperTextDisplay.utility.extra;


import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.mainConfigManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

public class RefreshTextTask implements Consumer<BukkitTask> {

    @Override
    public void accept(BukkitTask task) {
        if (!mainConfigManager().timerEnabled()) {
            task.cancel();
            return;
        }
        for (String id : displayManager().get().getKeys(false)){
            DisplayConfigManager dcm = displayManager().configManagers.get(id);
            if (dcm != null) displayManager().pairEditor().refreshText(id, dcm);
        }
    }
}