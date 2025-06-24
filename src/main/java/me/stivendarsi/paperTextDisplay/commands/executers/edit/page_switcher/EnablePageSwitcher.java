package me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.paperTextDisplay.events.player.PlayerSwitchPageEvent;
import me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;

@SuppressWarnings("UnstableApiUsage")
public class EnablePageSwitcher implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        final String id = context.getArgument("id", String.class);
        final boolean enabled = context.getArgument("enabled", Boolean.class);

        if (PageSwitcherTask.tasks.containsKey(id)) {
            int taskId = PageSwitcherTask.tasks.get(id);
            plugin().getServer().getScheduler().cancelTask(taskId);
        }

        DisplayConfigManager dcm = manager().configManagers.get(id);
        dcm.setPageSwitcherEnabled(enabled);

        PaperDisplaysConfig.get().set(id + ".page_switcher.enabled", enabled);
        PaperDisplaysConfig.get().set(id + ".page_switcher.interval", 3);
        PaperDisplaysConfig.save();

        cancelTask(id);
        manager().runPageSwitcher(dcm);




        return SINGLE_SUCCESS;
    }

}