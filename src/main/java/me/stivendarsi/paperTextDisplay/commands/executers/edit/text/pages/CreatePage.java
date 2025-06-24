package me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.translationsManager;

@SuppressWarnings("UnstableApiUsage")
public class CreatePage implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        String id = context.getArgument("id", String.class);

        manager().pairEditor().createPage(id);

        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().createPage()));

        return SINGLE_SUCCESS;
    }

}