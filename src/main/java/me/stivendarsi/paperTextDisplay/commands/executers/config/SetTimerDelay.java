package me.stivendarsi.paperTextDisplay.commands.executers.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.mainConfigManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.cancelTasks;

@SuppressWarnings("UnstableApiUsage")
public class SetTimerDelay implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        double interval = context.getArgument("interval", Double.class);
        mainConfigManager().setTimerInterval(interval);
        MainConfig.get().set("timer.interval", interval);
        MainConfig.save();

        cancelTasks();
        manager().runRefreshTask();

        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            DisplayConfigManager displayConfigManager = manager().configManagers.get(id);
            manager().runPageSwitcher(displayConfigManager);
        }

        return SINGLE_SUCCESS;
    }

}