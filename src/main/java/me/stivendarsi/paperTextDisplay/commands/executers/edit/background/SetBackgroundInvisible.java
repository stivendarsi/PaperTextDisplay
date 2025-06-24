package me.stivendarsi.paperTextDisplay.commands.executers.edit.background;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Color;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetBackgroundInvisible implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String id = context.getArgument("id", String.class);

        Color color = Color.fromARGB(24,0,0,0);
        manager().pairEditor().changeBackground(id, color);

        return SINGLE_SUCCESS;
    }

}