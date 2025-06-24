package me.stivendarsi.paperTextDisplay.utility.extra;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PageResolver {

    private PageResolver() {
    }

    public static Component resolve(List<String> strings, Player player, DisplayConfigManager configManager) {
        String s = String.join("\n", strings);
        if (player == null) return MiniMessage.miniMessage().deserialize(s).font(configManager.defaultFont());
        TagResolver tagResolver = TagResolver.builder()
                .tag("player_name", Tag.selfClosingInserting(player.name()))
                .tag("player_x", Tag.selfClosingInserting(Component.text(player.getLocation().getBlockX())))
                .tag("player_y", Tag.selfClosingInserting(Component.text(player.getLocation().getBlockY())))
                .tag("player_z", Tag.selfClosingInserting(Component.text(player.getLocation().getBlockZ())))

                .tag("placeholder", (arg, ctx) -> {
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        String placeholder = arg.popOr("No Placeholder Specified").value();
                        String text = PlaceholderAPI.setPlaceholders(player, placeholder);
                        return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(text));
                    }
                    return Tag.selfClosingInserting(Component.empty());
                })
                .build();
        return MiniMessage.miniMessage().deserialize(s, tagResolver).font(configManager.defaultFont());
    }
}
