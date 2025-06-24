package me.stivendarsi.paperTextDisplay.commands.executers.reload;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.TranslationsConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.*;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class NormalReload implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        MainConfig.reload();
        PaperDisplaysConfig.reload();
        TranslationsConfig.reload();

        mainConfigManager().load();
        translationsManager().load();

        plugin().getServer().getScheduler().cancelTasks(plugin());
        manager().runRefreshTask();

        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            cancelTask(id);
            DisplayConfigManager displayConfigManager = manager().configManagers.get(id);
            manager().runPageSwitcher(displayConfigManager);
        }

        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().normalReload()));

        return SINGLE_SUCCESS;
    }

}