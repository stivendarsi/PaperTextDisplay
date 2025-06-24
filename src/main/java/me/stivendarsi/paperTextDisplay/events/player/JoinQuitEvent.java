package me.stivendarsi.paperTextDisplay.events.player;

import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;

public class JoinQuitEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (String key : PaperDisplaysConfig.get().getKeys(false)) {
            DisplayConfigManager configManager = manager().configManagers.get(key);
            manager().showToPlayer(key, player, configManager);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        for (String key : PaperDisplaysConfig.get().getKeys(false)) {
            manager().removePlayerDisplay(key, uuid);
        }
    }
}
