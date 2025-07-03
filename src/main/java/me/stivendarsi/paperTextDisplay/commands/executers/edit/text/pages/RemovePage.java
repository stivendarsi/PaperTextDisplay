package me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.translationsManager;

@SuppressWarnings("UnstableApiUsage")
public class RemovePage implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        String id = context.getArgument("id", String.class);

        int page = context.getArgument("page", Integer.class);

        if (page <= 0){
            source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().pageZeroDeleteError()));
            return SINGLE_SUCCESS;
        }

        displayManager().resetAudiencePages(id);
        displayManager().pairEditor().removePage(id, page);
        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().removePage()));

        return SINGLE_SUCCESS;
    }

}