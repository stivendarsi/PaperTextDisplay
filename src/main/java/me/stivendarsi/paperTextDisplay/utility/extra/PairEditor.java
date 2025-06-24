package me.stivendarsi.paperTextDisplay.utility.extra;

import io.papermc.paper.math.Rotation;
import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.manager;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.*;

public class PairEditor {
    public void changeLocation(String id, Location newLocation) {


        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        newLocation.setRotation(configManager.locationAndRotation().getYaw(), configManager.locationAndRotation().getPitch());
        configManager.setLocationAndRotation(newLocation);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.teleport(newLocation);
            interaction.teleport(newLocation.clone().subtract(0, 0.125, 0).setRotation(Rotation.rotation(0, 0)));
        }


        String locPath = id + ".location.";
        PaperDisplaysConfig.get().set(locPath + "x", newLocation.x());
        PaperDisplaysConfig.get().set(locPath + "y", newLocation.y());
        PaperDisplaysConfig.get().set(locPath + "z", newLocation.z());
        PaperDisplaysConfig.get().set(locPath + "world", newLocation.getWorld().getName());

        String rotationPath = id + ".rotation.";
        PaperDisplaysConfig.get().set(rotationPath + "pitch", newLocation.getPitch());
        PaperDisplaysConfig.get().set(rotationPath + "yaw", newLocation.getYaw());
        PaperDisplaysConfig.save();
    }

    public void changePitch(String id, float pitch) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        Location loc = configManager.locationAndRotation();
        loc.setPitch(pitch);
        configManager.setLocationAndRotation(loc);
        manager().configManagers.put(id, configManager);


        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            float yaw = textDisplay.getYaw();
            textDisplay.setRotation(yaw, pitch);
        }


        PaperDisplaysConfig.get().set(id + ".rotation.pitch", pitch);
        PaperDisplaysConfig.save();
    }

    public void changeYaw(String id, float yaw) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        Location loc = configManager.locationAndRotation();
        loc.setPitch(yaw);
        configManager.setLocationAndRotation(loc);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            float pitch = textDisplay.getPitch();
            textDisplay.setRotation(yaw, pitch);
        }

        PaperDisplaysConfig.get().set(id + ".rotation.yaw", yaw);
        PaperDisplaysConfig.save();
    }

    public void changeRoll(String id, float roll) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setRoll(roll);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            Transformation transformation = textDisplay.getTransformation();
            Quaternionf newRoll = new Quaternionf().rotateZ((float) Math.toRadians(roll));

            Transformation rolled = new Transformation(
                    transformation.getTranslation(),
                    transformation.getLeftRotation(),
                    transformation.getScale(),
                    newRoll
            );

            textDisplay.setTransformation(rolled);
        }

        PaperDisplaysConfig.get().set(id + ".rotation.roll", roll);
        PaperDisplaysConfig.save();
    }

    public void changeScaleHitBox(String id, float width, float height) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setHitBoxScale(new Vector2f(width, height));
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display or interaction");

            interaction.setInteractionWidth(width);
            interaction.setInteractionHeight(height);
        }

        PaperDisplaysConfig.get().set(id + ".hit_box.scale.width", width);
        PaperDisplaysConfig.get().set(id + ".hit_box.scale.height", height);
        PaperDisplaysConfig.save();
    }

    public void visibleInteraction(String id, boolean visible) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setVisibleInteraction(visible);
        manager().configManagers.put(id, configManager);

        DIPAIR pair = pairs.getFirst();
        Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");

        interaction.setVisibleByDefault(visible);
        PaperDisplaysConfig.get().set(id + ".hit_box.visible", visible);
    }

    public void changeScaleDisplay(String id, float width, float height) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setDisplayScale(new Vector2d(width, height));
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            Transformation transformation = textDisplay.getTransformation();
            Vector3f newScale = transformation.getScale().set(width, height, 1);

            Transformation scaled = new Transformation(
                    transformation.getTranslation(),
                    transformation.getLeftRotation(),
                    newScale,
                    transformation.getRightRotation()
            );
            textDisplay.setTransformation(scaled);
        }

        PaperDisplaysConfig.get().set(id + ".scale.width", width);
        PaperDisplaysConfig.get().set(id + ".scale.height", height);
        PaperDisplaysConfig.save();
    }

    public void changeBillboard(String id, Display.Billboard billboard) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setBillboard(billboard);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setBillboard(billboard);
        }

        PaperDisplaysConfig.get().set(id + ".text.billboard", billboard.name().toLowerCase());
        PaperDisplaysConfig.save();
    }

    public void changeAlignment(String id, TextDisplay.TextAlignment alignment) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setTextAlignment(alignment);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setAlignment(alignment);
        }

        PaperDisplaysConfig.get().set(id + ".text.alignment", alignment.name().toLowerCase());
        PaperDisplaysConfig.save();
    }

    public void changeSeeThrough(String id, boolean seeThrough) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setSeeThrough(seeThrough);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setSeeThrough(seeThrough);
        }

        PaperDisplaysConfig.get().set(id + ".see_through", seeThrough);
        PaperDisplaysConfig.save();
    }

    public void changeBackground(String id, Color color) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setBackgroundColor(color);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setBackgroundColor(color);
        }
        final String backPath = id + ".background.";
        PaperDisplaysConfig.get().set(backPath + "red", color.getRed());
        PaperDisplaysConfig.get().set(backPath + "green", color.getGreen());
        PaperDisplaysConfig.get().set(backPath + "blue", color.getBlue());
        PaperDisplaysConfig.get().set(backPath + "alpha", color.getAlpha());
        PaperDisplaysConfig.save();
    }


    public void changeOpacity(String id, int opacity) {
        byte byteOpacity;
        if (opacity > 127) byteOpacity = (byte) (opacity - 255);
        else byteOpacity = (byte) opacity;

        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setOpacity(opacity);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setTextOpacity(byteOpacity);
        }

        PaperDisplaysConfig.get().set(id + ".text.opacity", opacity);
        PaperDisplaysConfig.save();
    }

    public void changeWidth(String id, int width) {

        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setLineWidth(width);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setLineWidth(width);
        }

        PaperDisplaysConfig.get().set(id + ".text.width", width);
        PaperDisplaysConfig.save();
    }

    public void changeTextShadow(String id, boolean textShadow) {

        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setTextShadow(textShadow);
        manager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setShadowed(textShadow);
        }

        PaperDisplaysConfig.get().set(id + ".text.shadow", textShadow);
        PaperDisplaysConfig.save();
    }

    /**
     * A method to add a line in a page, assuming the page index exists
     */
    public void addLine(String id, int pageNumber, int line, String text) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DIPAIR a = pairs.getFirst();
        Interaction interaction = (Interaction) Bukkit.getEntity(a.interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");

        DisplayConfigManager dcm = manager().configManagers.get(id);
        if (dcm == null) return;
        List<List<String>> pages = dcm.pages();

        if (!(pages.size() > pageNumber)) return;


        List<String> page = pages.get(pageNumber);

        //edit and set page
        addLine(page, line, text);
        pages.set(pageNumber, page);
        dcm.setPages(pages);

        //refresh to players
        refreshText(id, dcm);

        dcm.setPages(pages);
        manager().configManagers.put(id, dcm);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);
        PaperDisplaysConfig.save();
    }


    /**
     * A method to set a lines in a page, assuming the page index exists
     */
    public void setLine(String id, int pageNumber, int line, String text) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager dcm = manager().configManagers.get(id);
        if (dcm == null) return;
//
//        DIPAIR a = pairs.getFirst();
//        Interaction interaction = (Interaction) Bukkit.getEntity(a.interaction());
//        if (interaction == null) throw new RuntimeException("Null Interaction");

        List<List<String>> pages = dcm.pages();
        if (!(pages.size() > pageNumber)) return;

        List<String> page = pages.get(pageNumber);
        //edit and set page
        setLine(page, line, text);
        pages.set(pageNumber, page);
        //interaction.getPersistentDataContainer().set(DISPLAY_RAW_PAGES, PAGES_PERSISTENT_DATA_TYPE, pages);

        //refresh to players
        refreshText(id, dcm);


        dcm.setPages(pages);
        manager().configManagers.put(id, dcm);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);
        PaperDisplaysConfig.save();
    }

    public void removeLine(String id, int pageNumber, int line) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = manager().configManagers.get(id);
        if (dcm == null) return;

        DIPAIR a = pairs.getFirst();
        Interaction interaction = (Interaction) Bukkit.getEntity(a.interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");

        List<List<String>> pages = dcm.pages();
        if (!(pages.size() > pageNumber)) return;


        List<String> page = pages.get(pageNumber);

        //edit and set page
        if (!(page.size() > line)) return;
        if (page.size() - 1 >= 0) {
            page.remove(line);
        }
        pages.set(pageNumber, page);
        //interaction.getPersistentDataContainer().set(DISPLAY_RAW_PAGES, PAGES_PERSISTENT_DATA_TYPE, pages);

        //refresh to players
        refreshText(id, dcm);


        dcm.setPages(pages);
        manager().configManagers.put(id, dcm);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);
        PaperDisplaysConfig.save();
    }

    public void refreshText(String id, DisplayConfigManager dcm) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        List<List<String>> pages = dcm.pages();
        if (pages == null) return;

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            if (textDisplay == null) throw new RuntimeException("Null display");
            if (!textDisplay.getPersistentDataContainer().has(PLAYER_PAGE, PersistentDataType.INTEGER)) continue;
            Integer playerPage = textDisplay.getPersistentDataContainer().get(PLAYER_PAGE, PersistentDataType.INTEGER);
            if (playerPage == null) return;
            Player player = Bukkit.getPlayer(pair.player());

            if (pages.size() > playerPage) {
                textDisplay.text(PageResolver.resolve(pages.get(playerPage), player, dcm));
            } else {
                textDisplay.getPersistentDataContainer().set(PLAYER_PAGE, PersistentDataType.INTEGER, 0);
                textDisplay.text(PageResolver.resolve(pages.getFirst(), player, dcm));
            }
        }
    }


    private List<String> addLine(List<String> lines, int line, String text) {
        if (line > lines.size()) line = lines.size();
        lines.add(line, text);
        return lines;
    }

    private List<String> setLine(List<String> lines, int line, String text) {
        while (!(lines.size() > line)) {
            lines.add("");
        }
        lines.set(line, text);
        return lines;
    }

    public void createPage(String id) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = manager().configManagers.get(id);
        if (dcm == null) return;

        List<List<String>> pages = dcm.pages();

        List<String> page = new ArrayList<>();
        page.add("Blank Page");

        pages.add(page);

        //refresh to players
        refreshText(id, dcm);

        dcm.setPages(pages);
        manager().configManagers.put(id, dcm);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);
        PaperDisplaysConfig.save();
    }

    public void removePage(String id, int page) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = manager().configManagers.get(id);
        if (dcm == null) return;
        List<List<String>> pages = dcm.pages();
        if (pages.size() > page) pages.remove(page);

        //refresh to players
        refreshText(id, dcm);

        dcm.setPages(pages);
        manager().configManagers.put(id, dcm);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);
        PaperDisplaysConfig.save();
    }

    public void setDefaultFont(String id, Key font) {
        List<DIPAIR> pairs = manager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager configManager = manager().configManagers.get(id);
        configManager.setDefaultFont(font);
        MainConfig.get().set(id + ".text.font", font.asString());
        refreshText(id, configManager);

    }

}
