package me.stivendarsi.paperTextDisplay.commands.executers.edit.text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.key.Key;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetDefaultFont implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        String id = context.getArgument("id", String.class);

        final Key page = context.getArgument("key", Key.class);
        manager().pairEditor().setDefaultFont(id, page);

        return SINGLE_SUCCESS;
    }

}