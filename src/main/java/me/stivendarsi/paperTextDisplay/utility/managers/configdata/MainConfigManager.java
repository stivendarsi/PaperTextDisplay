package me.stivendarsi.paperTextDisplay.utility.managers.configdata;

import me.stivendarsi.paperTextDisplay.utility.managers.ConfigFile;

import java.io.File;

public class MainConfigManager extends ConfigFile {

    private double timerInterval;
    private boolean timerEnabled;

    public MainConfigManager(File groupFile) {
        super(groupFile);
    }


    public MainConfigManager load() {
        timerEnabled = get().getBoolean("timer.enabled");
        timerInterval = get().getDouble("timer.interval");

        return this;
    }

    public double timerInterval() {
        return timerInterval;
    }

    public boolean timerEnabled() {
        return timerEnabled;
    }

    public void setTimerInterval(double timerInterval) {
        this.timerInterval = timerInterval;
    }

    public void setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
    }
}
