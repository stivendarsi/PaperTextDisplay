package me.stivendarsi.paperTextDisplay.utility.extra;

import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;

public class PageSwitcherTask implements Consumer<BukkitTask> {
    public String id;

    public static Map<String, Integer> tasks = new HashMap<>();
    private DisplayConfigManager dcm;

    public PageSwitcherTask(String id) {
        this.id = id;
        this.dcm = manager().configManagers.get(id);
    }

    public static void cancelTask(String id ){
        Integer taskNum = tasks.get(id);
        if (taskNum != null) plugin().getServer().getScheduler().cancelTask(taskNum);
    }


    @Override
    public void accept(BukkitTask task) {
        tasks.putIfAbsent(id,task.getTaskId());
        if (dcm == null) {
            task.cancel();
            tasks.remove(id);
            return;
        }
        if (!dcm.pageSwitcherEnabled()) {
            task.cancel();
            tasks.remove(id);
            return;
        }
        List<DIPAIR> paris = manager().stringUUIDMap.get(id);
        if (paris == null) {
            task.cancel();
            tasks.remove(id);
            return;
        }
        for (DIPAIR dipair : paris) {
            manager().nextPage(id, dipair.player(), dcm);
        }
    }
}