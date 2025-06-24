package me.stivendarsi.paperTextDisplay.events.player;

import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.DISPLAY_ID;

public class PlayerSwitchPageEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return;
        if (!interaction.getPersistentDataContainer().has(DISPLAY_ID)) return;
        String id = interaction.getPersistentDataContainer().get(DISPLAY_ID, PersistentDataType.STRING);
        DisplayConfigManager dcm = manager().configManagers.get(id);
        manager().nextPage(id, event.getPlayer().getUniqueId(), dcm);
    }
}
