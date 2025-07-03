package me.stivendarsi.paperTextDisplay.commands.executers.edit.rotation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

@SuppressWarnings("UnstableApiUsage")
public class SetRotationRoll implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final float angle = context.getArgument("roll", Float.class);
        displayManager().pairEditor().changeRoll(id, angle);
        
        return SINGLE_SUCCESS;
    }

}