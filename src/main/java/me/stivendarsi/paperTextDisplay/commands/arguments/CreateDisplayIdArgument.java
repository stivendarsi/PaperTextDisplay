package me.stivendarsi.paperTextDisplay.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;
@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class CreateDisplayIdArgument implements CustomArgumentType.Converted<String, String> {

    private static final DynamicCommandExceptionType ERROR_ID_EXISTS = new DynamicCommandExceptionType(id -> {
        return MessageComponentSerializer.message().serialize(Component.text("Display with id: " +id + " already exists!", NamedTextColor.RED));
    });

    @Override
    public String convert(String id) throws CommandSyntaxException {
        if (PaperDisplaysConfig.get().contains(id)){
            throw ERROR_ID_EXISTS.create(id);
        } else {
            return id;
        }
    }


    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

}