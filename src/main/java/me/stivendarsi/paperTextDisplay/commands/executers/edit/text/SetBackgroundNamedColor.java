package me.stivendarsi.paperTextDisplay.commands.executers.edit.text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class SetBackgroundNamedColor implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {;

        String id = context.getArgument("id", String.class);

        final NamedTextColor textColor = context.getArgument("color", NamedTextColor.class);
        Color color = Color.fromRGB(textColor.red(), textColor.green(), textColor.blue());
        manager().pairEditor().changeBackground(id, color);

        return SINGLE_SUCCESS;
    }

}