package me.stivendarsi.paperTextDisplay.commands.executers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.translationsManager;

@SuppressWarnings("UnstableApiUsage")
public class CreateDisplay implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        final String id = context.getArgument("id", String.class);

        Location location = source.getLocation();

        displayManager().createDisplay(id, location);
        source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(translationsManager().createDisplay()));

        return SINGLE_SUCCESS;
    }

}