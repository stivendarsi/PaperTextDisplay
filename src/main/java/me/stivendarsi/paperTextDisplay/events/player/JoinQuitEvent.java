package me.stivendarsi.paperTextDisplay.events.player;

import io.papermc.paper.event.player.PlayerClientLoadedWorldEvent;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;

public class JoinQuitEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerClientLoadedWorldEvent event) {
        Player player = event.getPlayer();
        for (String key : displayManager().get().getKeys(false)) {
            DisplayConfigManager configManager = displayManager().configManagers.get(key);
            displayManager().showToPlayer(key, player, configManager);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        for (String key : displayManager().get().getKeys(false)) {
            displayManager().removePlayerDisplay(key, uuid);
        }
    }
}
