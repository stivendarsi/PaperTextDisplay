package me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class EnablePageSwitcher implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final String id = context.getArgument("id", String.class);
        final boolean enabled = context.getArgument("enabled", Boolean.class);

        if (PageSwitcherTask.tasks.containsKey(id)) {
            int taskId = PageSwitcherTask.tasks.get(id);
            plugin().getServer().getScheduler().cancelTask(taskId);
        }

        DisplayConfigManager dcm = displayManager().configManagers.get(id);
        dcm.setPageSwitcherEnabled(enabled);

        displayManager().get().set(id + ".page_switcher.enabled", enabled);
        displayManager().get().set(id + ".page_switcher.interval", 3);
        displayManager().save();

        cancelTask(id);
        displayManager().runPageSwitcher(dcm);
        return SINGLE_SUCCESS;
    }

}