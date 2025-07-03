package me.stivendarsi.paperTextDisplay.utility.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public  abstract class ConfigFile {
    private YamlConfiguration config;
    private final File configFile;
    private final String cleanName;

    public ConfigFile(File groupFile) {
        this.config = YamlConfiguration.loadConfiguration(groupFile);
        this.configFile = groupFile;
        this.cleanName = groupFile.getName().substring(0, groupFile.getName().lastIndexOf("."));
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        if (this.configFile.exists()) {
            this.config = YamlConfiguration.loadConfiguration(this.configFile);
        }

    }

    public YamlConfiguration get() {
        return this.config;
    }

    public String getCleanName() {
        return this.cleanName;
    }
}
