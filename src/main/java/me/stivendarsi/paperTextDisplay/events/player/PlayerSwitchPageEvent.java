package me.stivendarsi.paperTextDisplay.events.player;

import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.constant;
import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.displayManager;


public class PlayerSwitchPageEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return;
        if (!interaction.getPersistentDataContainer().has(constant().displayId())) return;
        String id = interaction.getPersistentDataContainer().get(constant().displayId(), PersistentDataType.STRING);
        DisplayConfigManager dcm = displayManager().configManagers.get(id);
        displayManager().nextPage(id, event.getPlayer().getUniqueId(), dcm);
    }
}
