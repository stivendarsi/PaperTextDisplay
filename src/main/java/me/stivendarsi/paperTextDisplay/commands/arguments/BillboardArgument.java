package me.stivendarsi.paperTextDisplay.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Display;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class BillboardArgument implements CustomArgumentType.Converted<Display.Billboard, String> {

    private static final DynamicCommandExceptionType ERROR_INVALID_BILLBOARD = new DynamicCommandExceptionType(billboard -> {
        return MessageComponentSerializer.message().serialize(Component.text(billboard + " is not a billboard type!"));
    });

    @Override
    public Display.Billboard convert(String nativeType) throws CommandSyntaxException {
        try {
            return Display.Billboard.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID_BILLBOARD.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Display.Billboard billboard : Display.Billboard.values()) {
            String name = billboard.toString();

            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(billboard.toString().toLowerCase());
            }
        }

        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

}