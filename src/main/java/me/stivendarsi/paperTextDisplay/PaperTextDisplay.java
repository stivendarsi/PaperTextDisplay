package me.stivendarsi.paperTextDisplay;

import me.stivendarsi.paperTextDisplay.commands.CoreCommandsManager;
import me.stivendarsi.paperTextDisplay.events.player.JoinQuitEvent;
import me.stivendarsi.paperTextDisplay.events.player.PlayerSwitchPageEvent;
import me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.TranslationsConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.MainConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.TranslationsConfigManager;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")
public final class PaperTextDisplay extends JavaPlugin {
    private static PaperTextDisplay plugin;

    public static PaperTextDisplay plugin() {
        return plugin;
    }

    private static DisplayManager manager;

    public static DisplayManager manager() {
        return manager;
    }

    private static TranslationsConfigManager translationsManager;

    public static TranslationsConfigManager translationsManager() {
        return translationsManager;
    }

    private static MainConfigManager mainConfigManager;

    public static MainConfigManager mainConfigManager() {
        return mainConfigManager;
    }

    @Override
    public void onEnable() {
        plugin = this;
        manager = new DisplayManager();

        PaperDisplaysConfig.setup();
        MainConfig.setup();
        TranslationsConfig.setup();

        MainConfig.get().addDefault("timer.enabled", false);
        MainConfig.get().addDefault("timer.interval", 3);

        TranslationsConfig.get().addDefault("create_display", "<green>Created a new text display!");
        TranslationsConfig.get().addDefault("remove_display", "<green>Successfully deleted text display!");

        TranslationsConfig.get().addDefault("delete_page_zero_error", "<red>Cannot delete page 0");
        TranslationsConfig.get().addDefault("create_page", "<green>Added a new page");
        TranslationsConfig.get().addDefault("delete_page", "<green>Page Deleted");

        TranslationsConfig.get().addDefault("advanced_reload_success", "<green>Successfully completed advanced reload!");
        TranslationsConfig.get().addDefault("normal_reload_success", "<green>Successfully completed normal reload!");

        MainConfig.get().options().copyDefaults(true);
        TranslationsConfig.get().options().copyDefaults(true);

        MainConfig.save();
        PaperDisplaysConfig.save();
        TranslationsConfig.save();


        manager().removeAll();
        manager().loadDisplays();
        new CoreCommandsManager(this.getLifecycleManager());


        translationsManager = new TranslationsConfigManager().load();
        mainConfigManager = new MainConfigManager().load();

        getServer().getPluginManager().registerEvents(new PlayerSwitchPageEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);

        manager().runRefreshTask();

        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            DisplayConfigManager dcp = manager().configManagers.get(id);
            manager().runPageSwitcher(dcp);
        }

        Permission permission = new Permission("textdisplay.admin", PermissionDefault.OP);
        plugin().getServer().getPluginManager().addPermission(permission);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
