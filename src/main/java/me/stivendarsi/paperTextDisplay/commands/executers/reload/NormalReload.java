package me.stivendarsi.paperTextDisplay.commands.executers.reload;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.*;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class NormalReload implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        mainConfigManager().reload();
        displayManager().reload();
        translationsManager().reload();

        mainConfigManager().load();
        translationsManager().load();

        plugin().getServer().getScheduler().cancelTasks(plugin());
        displayManager().runRefreshTask();

        for (String id : displayManager().get().getKeys(false)) {
            cancelTask(id);
            DisplayConfigManager displayConfigManager = displayManager().configManagers.get(id);
            displayManager().runPageSwitcher(displayConfigManager);
        }

        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().normalReload()));

        return SINGLE_SUCCESS;
    }

}