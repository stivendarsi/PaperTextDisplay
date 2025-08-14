package me.stivendarsi.paperTextDisplay.utility.managers;

import org.bukkit.NamespacedKey;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;

public class Constant {

    private NamespacedKey displayId = new NamespacedKey(plugin(), "display_id"); // private or global var
    private NamespacedKey interactionUuid = new NamespacedKey(plugin(), "interaction_uuid"); // private var
    private NamespacedKey playerUuid = new NamespacedKey(plugin(), "player_uuid"); // private var
    private NamespacedKey playerPage = new NamespacedKey(plugin(), "player_page"); // private var

    public NamespacedKey displayId() {
        return displayId;
    }

    public NamespacedKey interactionUuid() {
        return interactionUuid;
    }

    public NamespacedKey playerUuid() {
        return playerUuid;
    }

    public NamespacedKey playerPage() {
        return playerPage;
    }
}
