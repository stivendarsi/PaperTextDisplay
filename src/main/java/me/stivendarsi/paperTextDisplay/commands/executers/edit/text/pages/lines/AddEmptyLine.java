package me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.lines;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class AddEmptyLine implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final int page = context.getArgument("page", Integer.class);
        final int line = context.getArgument("line", Integer.class);
        manager().pairEditor().addLine(id, page, line, "");

        return SINGLE_SUCCESS;
    }

}