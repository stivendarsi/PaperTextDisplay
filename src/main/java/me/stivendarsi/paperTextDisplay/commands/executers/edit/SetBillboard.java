package me.stivendarsi.paperTextDisplay.commands.executers.edit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Display;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetBillboard implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final Display.Billboard billboard = context.getArgument("type", Display.Billboard.class);
        manager().pairEditor().changeBillboard(id, billboard);

        return SINGLE_SUCCESS;
    }

}