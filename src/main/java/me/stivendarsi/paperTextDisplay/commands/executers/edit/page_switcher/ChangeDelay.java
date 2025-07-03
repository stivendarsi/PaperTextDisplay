package me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class ChangeDelay implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final String id = context.getArgument("id", String.class);
        final double interval = context.getArgument("interval", Double.class);

        DisplayConfigManager displayConfigManager = displayManager().configManagers.get(id);

        displayManager().get().set(id + ".page_switcher.interval", interval);
        displayManager().save();
        displayConfigManager.setPageSwitcherInterval(interval);

        cancelTask(id);
        displayManager().runPageSwitcher(displayConfigManager);

        return SINGLE_SUCCESS;
    }

}