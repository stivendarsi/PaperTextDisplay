package me.stivendarsi.paperTextDisplay.utility.managers.configdata;

import me.stivendarsi.paperTextDisplay.utility.managers.MainConfig;

public class MainConfigManager {

    private double timerInterval;
    private boolean timerEnabled;


    public MainConfigManager load() {
        timerEnabled = MainConfig.get().getBoolean("timer.enabled");
        timerInterval = MainConfig.get().getDouble("timer.interval");

        return this;
    }

    public double timerInterval() {
        return timerInterval;
    }

    public boolean timerEnabled() {
        return timerEnabled;
    }

    public MainConfigManager setTimerInterval(double timerInterval) {
        this.timerInterval = timerInterval;
        return this;
    }

    public MainConfigManager setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
        return this;
    }
}
