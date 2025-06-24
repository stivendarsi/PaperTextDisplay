package me.stivendarsi.paperTextDisplay.utility.managers.configdata;

import me.stivendarsi.paperTextDisplay.utility.managers.PaperDisplaysConfig;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.intellij.lang.annotations.Subst;
import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static me.stivendarsi.paperTextDisplay.PaperTextDisplay.plugin;

public class DisplayConfigManager {
    private final String id;
    private Location locationAndRotation = Location.FINE_ZERO.toLocation(Bukkit.getWorlds().getFirst());
    private Vector2f hitBoxScale = new Vector2f(0.5f, 0.5f);
    private Vector2d displayScale = new Vector2d(1, 1);
    private List<List<String>> pages = new ArrayList<>();
    private TextDisplay.TextAlignment textAlignment = TextDisplay.TextAlignment.CENTER;
    private Display.Billboard billboard = Display.Billboard.FIXED;
    private int opacity = 255;
    private boolean textShadow = false;
    private int lineWidth = 200;

    private Key defaultFont = Key.key("minecraft:default");

    private double pageSwitcherInterval = 3;
    private boolean pageSwitcherEnabled = false;

    private boolean seeThrough = false;

    private Color backgroundColor = Color.fromARGB(63, 0, 0, 0);
    private float roll = 0;

    private boolean visibleInteraction = true;


    public DisplayConfigManager(String id) {
        this.id = id;
    }

    public DisplayConfigManager load() {
        double x = PaperDisplaysConfig.get().getDouble(id + ".location.x", 0);
        double y = PaperDisplaysConfig.get().getDouble(id + ".location.y", 0);
        double z = PaperDisplaysConfig.get().getDouble(id + ".location.z", 0);
        String worldName = PaperDisplaysConfig.get().getString(id + ".location.world", plugin().getServer().getWorlds().getFirst().getName());
        World world = plugin().getServer().getWorld(worldName);
        if (world == null) throw new RuntimeException("Null world");

        float pitch = (float) PaperDisplaysConfig.get().getDouble(id + ".rotation.pitch", 0f);
        float yaw = (float) PaperDisplaysConfig.get().getDouble(id + ".rotation.yaw", 0f);

        Location location = new Location(world, x, y, z, yaw, pitch);
        setLocationAndRotation(location);

        float roll = (float) PaperDisplaysConfig.get().getDouble(id + ".rotation.roll", 0f);
        this.roll = roll;

        this.lineWidth = PaperDisplaysConfig.get().getInt(id + ".text.width", 200);


        this.pages = (List<List<String>>) PaperDisplaysConfig.get().getList(id + ".text.pages", new ArrayList<>());

        String alignmentName = PaperDisplaysConfig.get().getString(id + ".text.alignment", TextDisplay.TextAlignment.CENTER.name());

        try {
            this.textAlignment = TextDisplay.TextAlignment.valueOf(alignmentName);
        } catch (IllegalArgumentException e) {
            this.textAlignment = TextDisplay.TextAlignment.CENTER;
        }

        String billboardName = PaperDisplaysConfig.get().getString(id + ".text.billboard", Display.Billboard.FIXED.name());
        try {
            this.billboard = Display.Billboard.valueOf(billboardName);
        } catch (IllegalArgumentException e) {
            this.billboard = Display.Billboard.FIXED;
        }

        this.textShadow = PaperDisplaysConfig.get().getBoolean(id + ".text.shadow", false);

        this.seeThrough = PaperDisplaysConfig.get().getBoolean(id + ".see_through", false);


        int alpha = PaperDisplaysConfig.get().getInt(id + ".background.alpha", 63);
        int red = PaperDisplaysConfig.get().getInt(id + ".background.red", 0);
        int green = PaperDisplaysConfig.get().getInt(id + ".background.green", 0);
        int blue = PaperDisplaysConfig.get().getInt(id + ".background.blue", 0);
        this.backgroundColor = Color.fromARGB(alpha, red, green, blue);

        float hitBoxWidth = (float) PaperDisplaysConfig.get().getDouble(id + ".hit_box.scale.width", 0.5f);
        float hitBoxHeight = (float) PaperDisplaysConfig.get().getDouble(id + ".hit_box.scale.height", 0.5f);
        this.hitBoxScale = new Vector2f(hitBoxWidth, hitBoxHeight);

        double displayWidth = PaperDisplaysConfig.get().getDouble(id + ".scale.width", 1);
        double displayHeight = PaperDisplaysConfig.get().getDouble(id + ".scale.height", 1);
        this.displayScale = new Vector2d(displayWidth, displayHeight);

        this.pageSwitcherEnabled = PaperDisplaysConfig.get().getBoolean(id + ".page_switcher.enabled", false);
        this.pageSwitcherInterval = PaperDisplaysConfig.get().getDouble(id + ".page_switcher.interval", 3);

        this.visibleInteraction = PaperDisplaysConfig.get().getBoolean(id + ".hit_box.visible");

        @Subst("minecraft:default") String fontName = PaperDisplaysConfig.get().getString(id + ".text.font", "minecraft:default");
        if (Key.parseable(fontName)){
            this.defaultFont = Key.key(fontName);
        }


        return this;
    }

    public Location locationAndRotation() {
        return locationAndRotation;
    }

    public String id() {
        return id;
    }

    public DisplayConfigManager setLocationAndRotation(Location locationAndRotation) {
        this.locationAndRotation = locationAndRotation;
        return this;
    }

    public Vector2f hitBoxScale() {
        return hitBoxScale;
    }

    public DisplayConfigManager setHitBoxScale(Vector2f hitBoxScale) {
        this.hitBoxScale = hitBoxScale;
        return this;
    }

    public Key defaultFont() {
        return defaultFont;
    }

    public Vector2d displayScale() {
        return displayScale;
    }

    public boolean visibleInteraction() {
        return visibleInteraction;
    }

    public DisplayConfigManager setVisibleInteraction(boolean visibleInteraction) {
        this.visibleInteraction = visibleInteraction;
        return this;
    }

    public DisplayConfigManager setDefaultFont(Key defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    public DisplayConfigManager setDisplayScale(Vector2d displayScale) {
        this.displayScale = displayScale;
        return this;
    }

    public DisplayConfigManager setPageSwitcherInterval(double pageSwitcherInterval) {
        this.pageSwitcherInterval = pageSwitcherInterval;
        return this;
    }

    public boolean seeThrough() {
        return seeThrough;
    }

    public DisplayConfigManager setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    public List<List<String>> pages() {
        return pages;
    }

    public DisplayConfigManager setPages(List<List<String>> pages) {
        this.pages = pages;
        return this;
    }

    public TextDisplay.TextAlignment textAlignment() {
        return textAlignment;
    }

    public DisplayConfigManager setTextAlignment(TextDisplay.TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    public Display.Billboard billboard() {
        return billboard;
    }

    public DisplayConfigManager setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public int opacity() {
        return opacity;
    }

    public DisplayConfigManager setOpacity(int opacity) {
        this.opacity = opacity;
        return this;
    }

    public boolean textShadow() {
        return textShadow;
    }

    public DisplayConfigManager setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public int lineWidth() {
        return lineWidth;
    }

    public DisplayConfigManager setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public double pageSwitcherInterval() {
        return pageSwitcherInterval;
    }

    public boolean pageSwitcherEnabled() {
        return pageSwitcherEnabled;
    }

    public DisplayConfigManager setPageSwitcherEnabled(boolean pageSwitcherEnabled) {
        this.pageSwitcherEnabled = pageSwitcherEnabled;
        return this;
    }

    public Color backgroundColor() {
        return backgroundColor;
    }

    public DisplayConfigManager setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public float roll() {
        return roll;
    }

    public DisplayConfigManager setRoll(float roll) {
        this.roll = roll;
        return this;
    }
}
