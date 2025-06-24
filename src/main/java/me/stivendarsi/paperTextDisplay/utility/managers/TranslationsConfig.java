package me.stivendarsi.paperTextDisplay.utility.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.intellij.lang.annotations.Subst;

import java.io.File;
import java.io.IOException;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;

public class TranslationsConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new File(plugin().getDataFolder(), "translations.yml");
        if (!(file.exists())){
            try {
                file.createNewFile();
            }catch (IOException e){
                //
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }
    @Subst("")
    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("cant save file");
        }
    }
    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }


}
