package me.stivendarsi.paperTextDisplay.utility.extra;

import io.papermc.paper.math.Rotation;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
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
import org.joml.Quaternionf;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.*;
import static me.stivendarsi.paperTextDisplay.utility.managers.DisplayManager.PLAYER_PAGE;

public class PairEditor {
    public void changeLocation(String id, Location newLocation) {


        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        newLocation.setRotation(configManager.locationAndRotation().getYaw(), configManager.locationAndRotation().getPitch());
        configManager.setLocationAndRotation(newLocation);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.teleport(newLocation);
            interaction.teleport(newLocation.clone().subtract(0, 0.125, 0).setRotation(Rotation.rotation(0, 0)));
        }


        String locPath = id + ".location.";
        displayManager().get().set(locPath + "x", newLocation.x());
        displayManager().get().set(locPath + "y", newLocation.y());
        displayManager().get().set(locPath + "z", newLocation.z());
        displayManager().get().set(locPath + "world", newLocation.getWorld().getName());

        String rotationPath = id + ".rotation.";
        displayManager().get().set(rotationPath + "pitch", newLocation.getPitch());
        displayManager().get().set(rotationPath + "yaw", newLocation.getYaw());
        displayManager().save();
    }

    public void changePitch(String id, float pitch) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        Location loc = configManager.locationAndRotation();
        loc.setPitch(pitch);
        configManager.setLocationAndRotation(loc);
        displayManager().configManagers.put(id, configManager);


        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            float yaw = textDisplay.getYaw();
            textDisplay.setRotation(yaw, pitch);
        }


        displayManager().get().set(id + ".rotation.pitch", pitch);
        displayManager().save();
    }

    public void changeYaw(String id, float yaw) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        Location loc = configManager.locationAndRotation();
        loc.setPitch(yaw);
        configManager.setLocationAndRotation(loc);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            float pitch = textDisplay.getPitch();
            textDisplay.setRotation(yaw, pitch);
        }

        displayManager().get().set(id + ".rotation.yaw", yaw);
        displayManager().save();
    }

    public void changeRoll(String id, float roll) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setRoll(roll);
        displayManager().configManagers.put(id, configManager);

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

        displayManager().get().set(id + ".rotation.roll", roll);
        displayManager().save();
    }

    public void changeScaleHitBox(String id, float width, float height) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setHitBoxScale(new Vector2f(width, height));
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display or interaction");

            interaction.setInteractionWidth(width);
            interaction.setInteractionHeight(height);
        }

        displayManager().get().set(id + ".hit_box.scale.width", width);
        displayManager().get().set(id + ".hit_box.scale.height", height);
        displayManager().save();
    }

    public void visibleInteraction(String id, boolean visible) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setVisibleInteraction(visible);
        displayManager().configManagers.put(id, configManager);

        DIPAIR pair = pairs.getFirst();
        Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");

        interaction.setVisibleByDefault(visible);
        displayManager().get().set(id + ".hit_box.visible", visible);
    }

    public void changeScaleDisplay(String id, float width, float height) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setDisplayScale(new Vector2d(width, height));
        displayManager().configManagers.put(id, configManager);

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

        displayManager().get().set(id + ".scale.width", width);
        displayManager().get().set(id + ".scale.height", height);
        displayManager().save();
    }

    public void changeBillboard(String id, Display.Billboard billboard) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setBillboard(billboard);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setBillboard(billboard);
        }

        displayManager().get().set(id + ".text.billboard", billboard.name().toLowerCase());
        displayManager().save();
    }

    public void changeAlignment(String id, TextDisplay.TextAlignment alignment) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setTextAlignment(alignment);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setAlignment(alignment);
        }

        displayManager().get().set(id + ".text.alignment", alignment.name().toLowerCase());
        displayManager().save();
    }

    public void changeSeeThrough(String id, boolean seeThrough) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setSeeThrough(seeThrough);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setSeeThrough(seeThrough);
        }

        displayManager().get().set(id + ".see_through", seeThrough);
        displayManager().save();
    }

    public void changeBackground(String id, Color color) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setBackgroundColor(color);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setBackgroundColor(color);
        }
        final String backPath = id + ".background.";
        displayManager().get().set(backPath + "red", color.getRed());
        displayManager().get().set(backPath + "green", color.getGreen());
        displayManager().get().set(backPath + "blue", color.getBlue());
        displayManager().get().set(backPath + "alpha", color.getAlpha());
        displayManager().save();
    }


    public void changeOpacity(String id, int opacity) {

        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setOpacity((byte) opacity);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setTextOpacity((byte) opacity);
        }

        displayManager().get().set(id + ".text.opacity", opacity);
        displayManager().save();
    }

    public void changeWidth(String id, int width) {

        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setLineWidth(width);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setLineWidth(width);
        }

        displayManager().get().set(id + ".text.width", width);
        displayManager().save();
    }

    public void changeTextShadow(String id, boolean textShadow) {

        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setTextShadow(textShadow);
        displayManager().configManagers.put(id, configManager);

        for (DIPAIR pair : pairs) {
            TextDisplay textDisplay = (TextDisplay) Bukkit.getEntity(pair.textDisplay());
            Interaction interaction = (Interaction) Bukkit.getEntity(pair.interaction());
            if (textDisplay == null || interaction == null) throw new RuntimeException("Null display");

            textDisplay.setShadowed(textShadow);
        }

        displayManager().get().set(id + ".text.shadow", textShadow);
        displayManager().save();
    }

    /**
     * A method to add a line in a page, assuming the page index exists
     */
    public void addLine(String id, int pageNumber, int line, String text) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DIPAIR a = pairs.getFirst();
        Interaction interaction = (Interaction) Bukkit.getEntity(a.interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");

        DisplayConfigManager dcm = displayManager().configManagers.get(id);
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
        displayManager().configManagers.put(id, dcm);

        displayManager().get().set(id + ".text.pages", pages);
        displayManager().save();
    }


    /**
     * A method to set a lines in a page, assuming the page index exists
     */
    public void setLine(String id, int pageNumber, int line, String text) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;

        DisplayConfigManager dcm = displayManager().configManagers.get(id);
        if (dcm == null) return;

        List<List<String>> pages = dcm.pages();
        if (!(pages.size() > pageNumber)) return;

        List<String> page = pages.get(pageNumber);
        //edit and set page
        setLine(page, line, text);
        pages.set(pageNumber, page);

        refreshText(id, dcm);


        dcm.setPages(pages);
        displayManager().configManagers.put(id, dcm);

        displayManager().get().set(id + ".text.pages", pages);
        displayManager().save();
    }

    public void removeLine(String id, int pageNumber, int line) {
        // Get pair and interaction entity
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = displayManager().configManagers.get(id);
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
        displayManager().configManagers.put(id, dcm);

        displayManager().get().set(id + ".text.pages", pages);
        displayManager().save();
    }

    public void refreshText(String id, DisplayConfigManager dcm) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
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
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = displayManager().configManagers.get(id);
        if (dcm == null) return;

        List<List<String>> pages = dcm.pages();

        List<String> page = new ArrayList<>();
        page.add("Blank Page");

        pages.add(page);

        //refresh to players
        refreshText(id, dcm);

        dcm.setPages(pages);
        displayManager().configManagers.put(id, dcm);

        displayManager().get().set(id + ".text.pages", pages);
        displayManager().save();
    }

    public void removePage(String id, int page) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager dcm = displayManager().configManagers.get(id);
        if (dcm == null) return;
        List<List<String>> pages = dcm.pages();
        if (pages.size() > page) pages.remove(page);

        //refresh to players
        refreshText(id, dcm);

        dcm.setPages(pages);
        displayManager().configManagers.put(id, dcm);

        displayManager().get().set(id + ".text.pages", pages);
        displayManager().save();
    }

    public void setDefaultFont(String id, Key font) {
        List<DIPAIR> pairs = displayManager().stringUUIDMap.get(id);
        if (pairs == null) return;
        DisplayConfigManager configManager = displayManager().configManagers.get(id);
        configManager.setDefaultFont(font);
        mainConfigManager().get().set(id + ".text.font", font.asString());
        refreshText(id, configManager);

    }

}
