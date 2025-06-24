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
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class RotationArgument implements CustomArgumentType.Converted<RotationArgument.RotationType, String> {

    private static final DynamicCommandExceptionType ERROR_INVALID_ROTATION = new DynamicCommandExceptionType(rotation -> {
        return MessageComponentSerializer.message().serialize(Component.text(rotation + " is not a rotation type!"));
    });

    @Override
    public RotationType convert(String nativeType) throws CommandSyntaxException {
        try {
            return RotationType.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID_ROTATION.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (RotationType rotationType : RotationType.values()) {
            String name = rotationType.toString();

            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(rotationType.toString().toLowerCase());
            }
        }

        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    public enum RotationType {
        PITCH,
        YAW,
        ROLL
    }
}