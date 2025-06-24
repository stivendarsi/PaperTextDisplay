package me.stivendarsi.paperTextDisplay.utility.extra;

import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.mainConfigManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

public class RefreshTextTask implements Consumer<BukkitTask> {

    @Override
    public void accept(BukkitTask task) {
        if (!mainConfigManager().timerEnabled()) {
            task.cancel();
            return;
        }
        for (String id : PaperDisplaysConfig.get().getKeys(false)){
            DisplayConfigManager dcm = manager().configManagers.get(id);
            if (dcm != null) manager().pairEditor().refreshText(id, dcm);
        }
    }
}