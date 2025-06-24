package me.stivendarsi.paperTextDisplay.commands.executers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.translationsManager;

@SuppressWarnings("UnstableApiUsage")
public class DeleteDisplay implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        final String id = context.getArgument("id", String.class);

        manager().deleteDisplay(id);
        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().deleteDisplay()));

        return SINGLE_SUCCESS;
    }

}