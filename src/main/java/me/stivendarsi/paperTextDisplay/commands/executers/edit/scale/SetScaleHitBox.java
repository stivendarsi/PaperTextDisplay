package me.stivendarsi.paperTextDisplay.commands.executers.edit.scale;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetScaleHitBox implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final float width = context.getArgument("width", Float.class);
        final float height = context.getArgument("height", Float.class);
        manager().pairEditor().changeScaleHitBox(id, width, height);

        return SINGLE_SUCCESS;
    }

}