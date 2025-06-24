package me.stivendarsi.paperTextDisplay.commands.executers.reload;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.TranslationsConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.*;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.cancelTasks;

@SuppressWarnings("UnstableApiUsage")
public class AdvancedReload implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        source.getSender().sendMessage(Component.text("Advanced Reload started!", NamedTextColor.GREEN));
        source.getSender().sendMessage(Component.text("It may take a couple of seconds!", NamedTextColor.YELLOW));


        MainConfig.reload();
        PaperDisplaysConfig.reload();
        TranslationsConfig.reload();

        mainConfigManager().load();
        translationsManager().load();

        plugin().getServer().getScheduler().cancelTasks(plugin());

        cancelTasks();

        manager().removeAll();
        manager().loadDisplays();
        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            manager().showToAllPlayers(id);
        }

        manager().runRefreshTask();

        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            DisplayConfigManager displayConfigManager = manager().configManagers.get(id);
            manager().runPageSwitcher(displayConfigManager);
        }

        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().advancedReload()));

        return SINGLE_SUCCESS;
    }

}