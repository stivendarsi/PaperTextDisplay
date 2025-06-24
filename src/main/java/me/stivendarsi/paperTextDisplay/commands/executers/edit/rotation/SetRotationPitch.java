package me.stivendarsi.paperTextDisplay.commands.executers.edit.rotation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetRotationPitch implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        String id = context.getArgument("id", String.class);

        final float angle = context.getArgument("pitch", Float.class);
        manager().pairEditor().changePitch(id, angle);

        return SINGLE_SUCCESS;
    }

}