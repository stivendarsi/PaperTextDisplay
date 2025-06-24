package me.stivendarsi.paperTextDisplay.utility.managers;

import io.papermc.paper.math.Rotation;
import me.stivendarsi.paperTextDisplay.utility.extra.*;
import me.stivendarsi.paperTextDisplay.utility.managers.configdata.DisplayConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.*;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.cancelTask;
import static me.stivendarsi.paperTextDisplay.utility.extra.PageSwitcherTask.tasks;

public class DisplayManager {

    private final PairEditor pairEditor;

    public DisplayManager() {
        this.pairEditor = new PairEditor();
    }


    public PairEditor pairEditor() {
        return pairEditor;
    }

    public Map<String, List<DIPAIR>> stringUUIDMap = new HashMap<>();
    public Map<String, DisplayConfigManager> configManagers = new HashMap<>();
    public static final NamespacedKey DISPLAY_ID = new NamespacedKey(plugin(), "display_id"); // private or global var
    public static final NamespacedKey INTERACTION_UUID = new NamespacedKey(plugin(), "interaction_uuid"); // private var
    public static final NamespacedKey PLAYER_UUID = new NamespacedKey(plugin(), "player_uuid"); // private var
    public static final NamespacedKey PLAYER_PAGE = new NamespacedKey(plugin(), "player_page"); // private var
//    @Deprecated(since = "Display Config Managers Implementation") public static final NamespacedKey DISPLAY_RAW_PAGES = new NamespacedKey(plugin(), "raw_pages_text"); // global var
//    @Deprecated(since = "Display Config Managers Implementation") public static final ListPersistentDataType<List<String>, List<String>> PAGES_PERSISTENT_DATA_TYPE = PersistentDataType.LIST.listTypeFrom(PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING));


    public void createDisplay(String id, Location location) {
        World world = location.getWorld();
        location.setRotation(Rotation.rotation(0, 0));
        Interaction interaction = (Interaction) world.spawnEntity(location.clone().subtract(0, 0.125, 0), EntityType.INTERACTION);
        interaction.setInteractionHeight(0.5f);
        interaction.setInteractionWidth(0.5f);

        DisplayConfigManager dcm = new DisplayConfigManager(id);

        interaction.getPersistentDataContainer().set(DISPLAY_ID, PersistentDataType.STRING, id);
        List<List<String>> pages = new ArrayList<>();
        List<String> rawLines = new ArrayList<>();
        rawLines.add("New Display");
        pages.add(rawLines);
        dcm.setPages(pages);

        UUID interactionUuid = interaction.getUniqueId();

        List<DIPAIR> pairs = new ArrayList<>();

        String locPath = id + ".location.";
        PaperDisplaysConfig.get().set(locPath + "x", location.x());
        PaperDisplaysConfig.get().set(locPath + "y", location.y());
        PaperDisplaysConfig.get().set(locPath + "z", location.z());
        PaperDisplaysConfig.get().set(locPath + "world", location.getWorld().getName());

        PaperDisplaysConfig.get().set(id + ".rotation.yaw", location.getYaw());
        PaperDisplaysConfig.get().set(id + ".rotation.pitch", location.getPitch());

        String scalePath = id + ".hit_box.scale.";
        PaperDisplaysConfig.get().set(scalePath + "width", 0.5);
        PaperDisplaysConfig.get().set(scalePath + "height", 0.5);

        PaperDisplaysConfig.get().set(id + ".text.pages", pages);

        PaperDisplaysConfig.save();

        dcm.setLocationAndRotation(location);
        dcm.setPages(pages);
        dcm.setHitBoxScale(new Vector2f(0.5f, 0.5f));


        TextDisplay textDisplay = (TextDisplay) world.spawnEntity(location, EntityType.TEXT_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            entity.setVisibleByDefault(false);
            entity.getPersistentDataContainer().set(DISPLAY_ID, PersistentDataType.STRING, id);
            entity.getPersistentDataContainer().set(INTERACTION_UUID, new UUIDDataType(), interactionUuid);
        });

        pairs.add(new DIPAIR(textDisplay.getUniqueId(), interactionUuid, null));
        stringUUIDMap.put(id, pairs);

        configManagers.put(id, dcm);
        showToAllPlayers(id);
    }

    public void deleteDisplay(String id) {
        List<DIPAIR> dipairs = stringUUIDMap.get(id);
        if (dipairs == null) return;
        System.out.println(dipairs.getFirst().interaction());
        Interaction interaction = (Interaction) Bukkit.getEntity(dipairs.getFirst().interaction());
        if (interaction != null) interaction.remove();
        for (DIPAIR dipair : dipairs) {
            TextDisplay display = (TextDisplay) Bukkit.getEntity(dipair.textDisplay());
            if (display != null) display.remove();
        }

        PaperDisplaysConfig.get().set(id, null);
        stringUUIDMap.remove(id);
        configManagers.remove(id);
        PaperDisplaysConfig.save();
    }


    public void removeAll() {
        for (World world : plugin().getServer().getWorlds()) {
            world.getEntitiesByClasses(TextDisplay.class, Interaction.class).stream()
                    .filter(entity -> entity.getPersistentDataContainer().has(DisplayManager.DISPLAY_ID))
                    .forEach(Entity::remove);
        }
    }


    public void showToPlayer(String id, Player player, DisplayConfigManager configManager) {
        DIPAIR dipair = stringUUIDMap.get(id).getFirst();
        UUID interactionUuid = dipair.interaction();
        DIPAIR pair = loadDisplayForPlayer(configManager, player, interactionUuid);
        stringUUIDMap.get(id).add(pair);
    }

    public void showToAllPlayers(String id) {
        DisplayConfigManager configManager = configManagers.get(id);
        if (configManager == null) return;

        DIPAIR dipair = stringUUIDMap.get(id).getFirst();
        UUID interactionUuid = dipair.interaction();

        for (Player player : Bukkit.getOnlinePlayers()) {
            DIPAIR pair = loadDisplayForPlayer(configManager, player, interactionUuid);
            stringUUIDMap.get(id).add(pair);
        }
    }


    public void nextPage(String id, UUID playerUUID, DisplayConfigManager dcm) {
        if (dcm == null) return;
        List<DIPAIR> pairs = stringUUIDMap.get(id);
        Interaction interaction = (Interaction) Bukkit.getEntity(pairs.getFirst().interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");
        for (DIPAIR dipair : pairs) {
            if (dipair.player() == playerUUID) {
                TextDisplay textdisplay = (TextDisplay) Bukkit.getEntity(dipair.textDisplay());
                if (textdisplay == null) throw new RuntimeException("Null display");

                Integer currentPage = textdisplay.getPersistentDataContainer().get(PLAYER_PAGE, PersistentDataType.INTEGER);
                if (currentPage == null) break;

                List<List<String>> pages = dcm.pages();
                if (pages == null) break;
                if (pages.size() > currentPage + 1) {
                    currentPage++;
                    textdisplay.getPersistentDataContainer().set(PLAYER_PAGE, PersistentDataType.INTEGER, currentPage);
                } else if (currentPage == 0 && pages.size() == 1) {
                    break;
                } else {
                    currentPage = 0;
                    textdisplay.getPersistentDataContainer().set(PLAYER_PAGE, PersistentDataType.INTEGER, currentPage);
                }
                Player player = Bukkit.getPlayer(playerUUID);
                if (player == null) return;
                textdisplay.text(PageResolver.resolve(pages.get(currentPage), player, dcm));
                break;
            }
        }
    }

    public void resetAudiencePages(String id) {
        List<DIPAIR> pairs = stringUUIDMap.get(id);
        Interaction interaction = (Interaction) Bukkit.getEntity(pairs.getFirst().interaction());
        if (interaction == null) throw new RuntimeException("Null Interaction");
        for (DIPAIR dipair : pairs) {
            TextDisplay textdisplay = (TextDisplay) Bukkit.getEntity(dipair.textDisplay());
            Player player = Bukkit.getPlayer(dipair.player());
            if (textdisplay == null || player == null) throw new RuntimeException("Null display/interaction");
            if (!textdisplay.getPersistentDataContainer().has(PLAYER_PAGE)) continue;
            Integer currentNum = textdisplay.getPersistentDataContainer().get(PLAYER_PAGE, PersistentDataType.INTEGER);
            if (currentNum == null) continue;
            if (currentNum != 0) {
                currentNum = 0;
                textdisplay.getPersistentDataContainer().set(PLAYER_PAGE, PersistentDataType.INTEGER, currentNum);
            }
        }
    }


    public void removePlayerDisplay(String id, UUID playerUUID) {
        List<DIPAIR> pairs = stringUUIDMap.get(id);
        for (DIPAIR dipair : pairs) {
            if (dipair.player() == playerUUID) {
                TextDisplay display = (TextDisplay) Bukkit.getEntity(dipair.textDisplay());
                if (display == null) throw new RuntimeException("Cannot remove display - its null");
                display.remove();
            }
        }
        stringUUIDMap.put(id, pairs);
    }

    public void loadDisplays() {
        this.configManagers.clear();
        this.stringUUIDMap.clear();
        for (String id : PaperDisplaysConfig.get().getKeys(false)) {
            List<DIPAIR> pairs = new ArrayList<>();
            DisplayConfigManager displayConfigManager = new DisplayConfigManager(id).load();
            Location mainlocation = displayConfigManager.locationAndRotation();
            Interaction interaction = (Interaction) mainlocation.getWorld().spawnEntity(mainlocation.clone().subtract(0, 0.125, 0).setRotation(Rotation.rotation(0, 0)), EntityType.INTERACTION);

            interaction.getPersistentDataContainer().set(DISPLAY_ID, PersistentDataType.STRING, id);
            UUID interactionUuid = interaction.getUniqueId();
            interaction.setVisibleByDefault(displayConfigManager.visibleInteraction());

            TextDisplay textDisplay = (TextDisplay) mainlocation.getWorld().spawnEntity(mainlocation, EntityType.TEXT_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
                entity.setVisibleByDefault(false);
                entity.getPersistentDataContainer().set(DISPLAY_ID, PersistentDataType.STRING, id);
                entity.getPersistentDataContainer().set(INTERACTION_UUID, new UUIDDataType(), interactionUuid);
            });

            Transformation transformation = textDisplay.getTransformation();
            Vector3f newScale = transformation.getScale().set(displayConfigManager.displayScale(), 1);
            Quaternionf newRoll = new Quaternionf().rotateZ((float) Math.toRadians(displayConfigManager.roll()));

            Transformation newTransformation = new Transformation(
                    transformation.getTranslation(),
                    transformation.getLeftRotation(),
                    newScale,
                    newRoll
            );

            textDisplay.setTransformation(newTransformation);

            interaction.setInteractionWidth(displayConfigManager.hitBoxScale().x);
            interaction.setInteractionHeight(displayConfigManager.hitBoxScale().y);

            int opacity = displayConfigManager.opacity();
            byte byteOpacity;
            if (opacity > 127) byteOpacity = (byte) (opacity - 255);
            else byteOpacity = (byte) opacity;
            textDisplay.setTextOpacity(byteOpacity);

            textDisplay.setLineWidth(displayConfigManager.lineWidth());
            textDisplay.setAlignment(displayConfigManager.textAlignment());
            textDisplay.setBillboard(displayConfigManager.billboard());
            textDisplay.setShadowed(displayConfigManager.textShadow());
            textDisplay.setBackgroundColor(displayConfigManager.backgroundColor());
            textDisplay.setSeeThrough(displayConfigManager.seeThrough());

            cancelTask(id);
            manager().runPageSwitcher(displayConfigManager);

            pairs.add(new DIPAIR(textDisplay.getUniqueId(), interactionUuid, null));
            stringUUIDMap.put(id, pairs);
            configManagers.put(id, displayConfigManager);
        }
    }

    public DIPAIR loadDisplayForPlayer(DisplayConfigManager displayConfigManager, Player player, UUID interactionUuid) {
        String id = displayConfigManager.id();
        Location mainlocation = displayConfigManager.locationAndRotation();

        TextDisplay textDisplay = (TextDisplay) mainlocation.getWorld().spawnEntity(mainlocation, EntityType.TEXT_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            entity.setVisibleByDefault(false);
            entity.getPersistentDataContainer().set(DISPLAY_ID, PersistentDataType.STRING, id);
            entity.getPersistentDataContainer().set(INTERACTION_UUID, new UUIDDataType(), interactionUuid);
            entity.getPersistentDataContainer().set(PLAYER_UUID, new UUIDDataType(), player.getUniqueId());
            entity.getPersistentDataContainer().set(PLAYER_PAGE, PersistentDataType.INTEGER, 0);
        });

        textDisplay.text(PageResolver.resolve(displayConfigManager.pages().getFirst(), player, displayConfigManager));

        int opacity = displayConfigManager.opacity();
        byte byteOpacity;
        if (opacity > 127) byteOpacity = (byte) (opacity - 255);
        else byteOpacity = (byte) opacity;
        textDisplay.setTextOpacity(byteOpacity);

        textDisplay.setLineWidth(displayConfigManager.lineWidth());
        textDisplay.setAlignment(displayConfigManager.textAlignment());
        textDisplay.setBillboard(displayConfigManager.billboard());
        textDisplay.setShadowed(displayConfigManager.textShadow());
        textDisplay.setBackgroundColor(displayConfigManager.backgroundColor());
        textDisplay.setSeeThrough(displayConfigManager.seeThrough());

        Transformation transformation = textDisplay.getTransformation();
        Vector3f newScale = transformation.getScale().set(displayConfigManager.displayScale(), 1);
        Quaternionf newRoll = new Quaternionf().rotateZ((float) Math.toRadians(displayConfigManager.roll()));

        Transformation newTransformation = new Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                newScale,
                newRoll
        );

        textDisplay.setTransformation(newTransformation);
        player.showEntity(plugin(), textDisplay);
        return new DIPAIR(textDisplay.getUniqueId(), interactionUuid, player.getUniqueId());
    }

    public void runRefreshTask() {
        if (mainConfigManager().timerEnabled()) {
            long tickDelay = (long) (mainConfigManager().timerInterval() * 20L);
            plugin().getServer().getScheduler().runTaskTimer(plugin(), new RefreshTextTask(), 0, tickDelay);
        }
    }

    public void runPageSwitcher(DisplayConfigManager dcp) {
        if (dcp == null) return;
        if (dcp.pageSwitcherEnabled()) {
            long pageSwitcherTickDelay = (long) dcp.pageSwitcherInterval() * 20L;
            plugin().getServer().getScheduler().runTaskTimer(plugin(), new PageSwitcherTask(dcp.id()), 0, pageSwitcherTickDelay);
        } else {
            cancelTask(dcp.id());
        }
    }

    public static void cancelTasks() {
        plugin().getServer().getScheduler().cancelTasks(plugin());
        tasks.clear();
    }
}
