package me.stivendarsi.paperTextDisplay.commands.executers.edit.text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetTextOpacity implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final int opacity = context.getArgument("opacity", Integer.class);
        manager().pairEditor().changeOpacity(id, opacity);

        return SINGLE_SUCCESS;
    }

}