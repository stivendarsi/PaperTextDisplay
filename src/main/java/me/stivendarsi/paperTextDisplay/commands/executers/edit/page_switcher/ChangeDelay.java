package me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class ChangeDelay implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        final String id = context.getArgument("id", String.class);
        final double interval = context.getArgument("interval", Double.class);

        DisplayConfigManager displayConfigManager = manager().configManagers.get(id);

        PaperDisplaysConfig.get().set(id + ".page_switcher.interval", interval);
        PaperDisplaysConfig.save();


        cancelTask(id);
        manager().runPageSwitcher(displayConfigManager);

        return SINGLE_SUCCESS;
    }

}