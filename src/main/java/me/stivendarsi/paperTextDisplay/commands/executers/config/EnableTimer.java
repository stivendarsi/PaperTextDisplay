package me.stivendarsi.paperTextDisplay.commands.executers.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.mainConfigManager;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.cancelTasks;

@SuppressWarnings("UnstableApiUsage")
public class EnableTimer implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean enabled = context.getArgument("boolean", Boolean.class);
        mainConfigManager().setTimerEnabled(enabled);
        mainConfigManager().get().set("timer.enabled", enabled);
        mainConfigManager().save();


        cancelTasks();
        displayManager().runRefreshTask();

        for (String id : displayManager().get().getKeys(false)) {
            DisplayConfigManager displayConfigManager = displayManager().configManagers.get(id);
            displayManager().runPageSwitcher(displayConfigManager);
        }

        return SINGLE_SUCCESS;
    }

}