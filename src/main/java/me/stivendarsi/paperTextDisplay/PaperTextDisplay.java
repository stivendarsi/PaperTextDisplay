package me.stivendarsi.paperTextDisplay;

import me.stivendarsi.paperTextDisplay.commands.CoreCommandsManager;
import me.stivendarsi.paperTextDisplay.events.player.JoinQuitEvent;
import me.stivendarsi.paperTextDisplay.events.player.PlayerSwitchPageEvent;
import me.stivendarsi.paperTextDisplay.utility.managers.Constant;
import me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.MainConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.TranslationsConfigManager;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@SuppressWarnings("UnstableApiUsage")
public final class PaperTextDisplay extends JavaPlugin {
    private static PaperTextDisplay plugin;

    public static PaperTextDisplay plugin() {
        return plugin;
    }

    private static DisplayManager manager;

    public static DisplayManager displayManager() {
        return manager;
    }

    private static Constant constant;

    public static Constant constant() {
        return constant;
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

        constant = new Constant();


        String[] configFilesNames = new String[]{"config","displays","translations"};
        for (String name : configFilesNames) saveDefaultYamlFile(name);

        manager = new DisplayManager();
        translationsManager = new TranslationsConfigManager(new File(plugin().getDataFolder(), "translations.yml")).load();
        mainConfigManager = new MainConfigManager(new File(plugin().getDataFolder(), "config.yml")).load();


        displayManager().removeAll();
        displayManager().loadDisplays();
        new CoreCommandsManager(this.getLifecycleManager());



        getServer().getPluginManager().registerEvents(new PlayerSwitchPageEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);

        displayManager().runRefreshTask();

        for (String id : displayManager().get().getKeys(false)) {
            DisplayConfigManager dcp = displayManager().configManagers.get(id);
            displayManager().runPageSwitcher(dcp);
        }

        Permission permission = new Permission("textdisplay.admin", PermissionDefault.OP);
        plugin().getServer().getPluginManager().addPermission(permission);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void saveDefaultYamlFile(String name) {
        File file = new File(this.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            saveResource(name + ".yml", false);
        }
    }
}
