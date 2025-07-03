package me.stivendarsi.paperTextDisplay.commands.executers.edit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import org.bukkit.Location;
import org.bukkit.World;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

@SuppressWarnings("UnstableApiUsage")
public class SetLocation implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        String id = context.getArgument("id", String.class);

        final FinePositionResolver resolver = context.getArgument("xyz", FinePositionResolver.class);
        final FinePosition finePosition = resolver.resolve(context.getSource());

        final World world = source.getLocation().getWorld();
        Location location = new Location(world, finePosition.x(), finePosition.y(), finePosition.z());

        displayManager().pairEditor().changeLocation(id, location);

        return SINGLE_SUCCESS;
    }

}