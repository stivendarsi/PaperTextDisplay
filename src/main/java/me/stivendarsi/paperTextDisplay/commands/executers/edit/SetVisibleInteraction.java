package me.stivendarsi.paperTextDisplay.commands.executers.edit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetVisibleInteraction implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        String id = context.getArgument("id", String.class);

        final boolean visible = context.getArgument("boolean", Boolean.class);
        manager().pairEditor().visibleInteraction(id, visible);

        return SINGLE_SUCCESS;
    }

}