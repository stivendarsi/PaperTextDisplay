package me.stivendarsi.paperTextDisplay.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.stivendarsi.paperTextDisplay.commands.arguments.CreateDisplayIdArgument;
import me.stivendarsi.paperTextDisplay.commands.executers.CreateDisplay;
import me.stivendarsi.paperTextDisplay.commands.executers.DeleteDisplay;
import me.stivendarsi.paperTextDisplay.commands.executers.config.EnableTimer;
import me.stivendarsi.paperTextDisplay.commands.executers.config.SetTimerDelay;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.*;
import me.stivendarsi.paperTextDisplay.commands.executers.reload.AdvancedReload;
import me.stivendarsi.paperTextDisplay.commands.executers.reload.NormalReload;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher.ChangeDelay;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.page_switcher.EnablePageSwitcher;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.CreatePage;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.RemovePage;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.lines.AddEmptyLine;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.lines.AddLine;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.lines.RemoveLine;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.commands.arguments.AlignmentArgument;
import me.stivendarsi.paperTextDisplay.commands.arguments.BillboardArgument;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.background.SetBackgroundARGB;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.*;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.background.SetBackgroundDefault;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.background.SetBackgroundInvisible;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.background.SetBackgroundRGB;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.rotation.SetRotationPitch;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.rotation.SetRotationRoll;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.rotation.SetRotationYaw;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.scale.SetScaleDisplay;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.scale.SetScaleHitBox;
import me.stivendarsi.paperTextDisplay.commands.executers.edit.text.pages.lines.SetLine;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.key;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.namedColor;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

@SuppressWarnings("UnstableApiUsage")
public class CoreCommandsManager {
    public CoreCommandsManager(LifecycleEventManager<Plugin> manager) {
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            LiteralCommandNode<CommandSourceStack> edit = Commands.literal("edit").then(Commands.argument("id", word()).suggests(this::getIDS)
                    .then(Commands.literal("location").then(Commands.argument("xyz", ArgumentTypes.finePosition()).executes(new SetLocation())))

                    .then(Commands.literal("rotation")
                            .then(Commands.literal("pitch").then(Commands.argument("pitch", floatArg(-90, 90)).executes(new SetRotationPitch())))
                            .then(Commands.literal("yaw").then(Commands.argument("yaw", floatArg(-180, 180)).executes(new SetRotationYaw())))
                            .then(Commands.literal("roll").then(Commands.argument("roll", floatArg(0, 360)).executes(new SetRotationRoll())))
                    )
                    .then(Commands.literal("scale")
                            .then(Commands.literal("display").then(Commands.argument("width", floatArg()).then(Commands.argument("height", floatArg()).executes(new SetScaleDisplay()))))
                            .then(Commands.literal("hitbox").then(Commands.argument("width", floatArg()).then(Commands.argument("height", floatArg()).executes(new SetScaleHitBox()))))
                    )

                    .then(Commands.literal("billboard").then(Commands.argument("type", new BillboardArgument()).executes(new SetBillboard())))
                    .then(Commands.literal("alignment").then(Commands.argument("type", new AlignmentArgument()).executes(new SetAlignment())))

                    .then(Commands.literal("background")
                            .then(Commands.literal("argb")
                                    .then(Commands.argument("alpha", integer(0, 255))
                                            .then(Commands.argument("red", integer(0, 255))
                                                    .then(Commands.argument("green", integer(0, 255))
                                                            .then(Commands.argument("blue", integer(0, 255))
                                                                    .executes(new SetBackgroundARGB())

                                                            )
                                                    )
                                            ))
                            )
                            .then(Commands.literal("rgb")
                                    .then(Commands.argument("red", integer(0, 255))
                                            .then(Commands.argument("green", integer(0, 255))
                                                    .then(Commands.argument("blue", integer(0, 255))
                                                            .executes(new SetBackgroundRGB())
                                                    )
                                            )
                                    )
                            )
                            .then(Commands.literal("default").executes(new SetBackgroundDefault()))
                            .then(Commands.literal("invisible").executes(new SetBackgroundInvisible()))
                            .then(Commands.literal("color").then(Commands.argument("color", namedColor()).executes(new SetBackgroundNamedColor())))
                    )

                    .then(Commands.literal("text")
                            .then(Commands.literal("opacity").then(Commands.argument("opacity", integer(0, 255)).executes(new SetTextOpacity())))
                            .then(Commands.literal("width").then(Commands.argument("width", integer()).executes(new SetTextWidth())))
                            .then(Commands.literal("font").then(Commands.argument("key", key()).suggests(this::defaultFonts).executes(new SetDefaultFont())))
                            .then(Commands.literal("shadow").then(Commands.argument("boolean", bool()).executes(new SetTextShadow())))
                            .then(Commands.literal("page")
                                    .then(Commands.literal("edit")
                                            .then(Commands.argument("page", integer(0)).suggests(this::pageNum)
                                                    .then(Commands.argument("line", integer(0)).suggests(this::linesOfPage)
                                                            .then(Commands.literal("add").executes(new AddEmptyLine())
                                                                    .then(Commands.argument("text", greedyString()).executes(new AddLine())))
                                                            .then(Commands.literal("set").then(Commands.argument("text", greedyString()).suggests(this::currentLine).executes(new SetLine())))
                                                            .then(Commands.literal("remove").executes(new RemoveLine()))
                                                    )
                                            )
                                    )
                                    .then(Commands.literal("create").executes(new CreatePage()))
                                    .then(Commands.literal("remove").then(Commands.argument("page", integer(0)).suggests(this::pageNum).executes(new RemovePage())))
                            )
                    )
                    .then(Commands.literal("auto_page_switch")
                            .then(Commands.literal("enabled").then(Commands.argument("enabled", bool()).executes(new EnablePageSwitcher())))
                            .then(Commands.literal("interval").then(Commands.argument("interval", doubleArg(0)).executes(new ChangeDelay())))
                    )
                    .then(Commands.literal("visible_hitbox").then(Commands.argument("boolean", bool()).executes(new SetVisibleInteraction())))
                    .then(Commands.literal("see_through").then(Commands.argument("boolean", bool()).executes(new SetSeeThrough())))

            ).build();

            commands.register(
                    Commands.literal("textdisplay").requires(commandSourceStack -> commandSourceStack.getSender().hasPermission("textdisplay.admin"))
                            .then(Commands.literal("create").then(Commands.argument("id", new CreateDisplayIdArgument()).executes(new CreateDisplay())))
                            .then(Commands.literal("delete").then(Commands.argument("id", word()).suggests(this::getIDS).executes(new DeleteDisplay())))
                            .then(Commands.literal("reload")
                                    .then(Commands.literal("normal").executes(new NormalReload()))
                                    .then(Commands.literal("advanced").executes(new AdvancedReload()))
                            )
                            .then(edit)
                            .then(Commands.literal("config")
                                    .then(Commands.literal("timer")
                                            .then(Commands.literal("enabled").then(Commands.argument("boolean", bool()).executes(new EnableTimer())))
                                            .then(Commands.literal("interval").then(Commands.argument("interval", doubleArg(0)).executes(new SetTimerDelay())))
                                    )
                            )
                            .build());
        });


    }

    public CompletableFuture<Suggestions> currentLine(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String id = context.getArgument("id", String.class);
        int page = context.getArgument("page", Integer.class);
        int line = context.getArgument("line", Integer.class);

        DisplayConfigManager configManager = manager().configManagers.get(id);
        if (configManager != null){
            List<List<String>> pages = configManager.pages();
            if (page < pages.size()){
                List<String> aPage = pages.get(page);
                if (line < aPage.size()) builder.suggest(aPage.get(line));
            }
        }
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> linesOfPage(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String id = context.getArgument("id", String.class);
        int page = context.getArgument("page", Integer.class);

        DisplayConfigManager configManager = manager().configManagers.get(id);
        if (configManager != null){
            List<List<String>> pages = configManager.pages();
            if (page < pages.size()){
                List<String> aPage = pages.get(page);
                for (int i = 0; i < aPage.size(); i++) {
                    builder.suggest(i);
                }
            }
        }
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> getIDS(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        PaperDisplaysConfig.get().getKeys(false).forEach(builder::suggest);
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> pageNum(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String id = context.getArgument("id", String.class);

        DisplayConfigManager configManager = manager().configManagers.get(id);
        if (configManager != null){
            List<List<String>> pages = configManager.pages();
            for (int i = 0; i < pages.size(); i++) {
                builder.suggest(i);
            }
        }
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> defaultFonts(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("minecraft:default");
        builder.suggest("minecraft:alt");
        builder.suggest("minecraft:uniform");
        builder.suggest("minecraft:illageralt");

        return builder.buildFuture();
    }
}
