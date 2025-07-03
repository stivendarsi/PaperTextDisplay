package me.stivendarsi.paperTextDisplay.commands.executers.edit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

@SuppressWarnings("UnstableApiUsage")
public class SetSeeThrough implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String id = context.getArgument("id", String.class);

        final boolean seeThrough = context.getArgument("boolean", Boolean.class);
        displayManager().pairEditor().changeSeeThrough(id, seeThrough);

        return SINGLE_SUCCESS;
    }

}