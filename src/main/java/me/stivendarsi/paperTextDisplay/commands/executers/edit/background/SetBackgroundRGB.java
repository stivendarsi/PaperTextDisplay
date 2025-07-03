package me.stivendarsi.paperTextDisplay.commands.executers.edit.background;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Color;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

@SuppressWarnings("UnstableApiUsage")
public class SetBackgroundRGB implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {


        String id = context.getArgument("id", String.class);

        final int red = context.getArgument("red", Integer.class);
        final int green = context.getArgument("green", Integer.class);
        final int blue = context.getArgument("blue", Integer.class);
        Color color = Color.fromRGB(red, green, blue);
        displayManager().pairEditor().changeBackground(id, color);

        return SINGLE_SUCCESS;
    }

}