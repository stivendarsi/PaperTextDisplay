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
import org.bukkit.entity.TextDisplay;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class AlignmentArgument implements CustomArgumentType.Converted<TextDisplay.TextAlignment, String> {

    private static final DynamicCommandExceptionType ERROR_INVALID_ALIGNMENT = new DynamicCommandExceptionType(alignment -> {
        return MessageComponentSerializer.message().serialize(Component.text(alignment + " is not a alignment type!"));
    });

    @Override
    public TextDisplay.TextAlignment convert(String nativeType) throws CommandSyntaxException {
        try {
            return TextDisplay.TextAlignment.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID_ALIGNMENT.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (TextDisplay.TextAlignment alignment : TextDisplay.TextAlignment.values()) {
            String name = alignment.toString();

            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(alignment.toString().toLowerCase());
            }
        }

        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

}