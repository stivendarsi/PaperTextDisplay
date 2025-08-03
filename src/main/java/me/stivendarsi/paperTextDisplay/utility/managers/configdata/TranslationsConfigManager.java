package me.stivendarsi.paperTextDisplay.utility.managers.configdata;

import me.stivendarsi.paperTextDisplay.utility.managers.ConfigFile;

import java.io.File;

public class TranslationsConfigManager extends ConfigFile {

    private String createDisplay;
    private String deleteDisplay;
    private String pageZeroDeleteError;
    private String createPage;
    private String removePage;
    private String advancedReload;
    private String normalReload;

    public TranslationsConfigManager(File groupFile) {
        super(groupFile);
    }


    public TranslationsConfigManager load(){
        createDisplay = get().getString("create_display", "<red>not string provided");
        deleteDisplay = get().getString("remove_display", "<red>not string provided");

        pageZeroDeleteError = get().getString("delete_page_zero_error", "<red>not string provided");
        createPage = get().getString("create_page", "<red>not string provided");
        removePage = get().getString("remove_page", "<red>not string provided");

        advancedReload = get().getString("advanced_reload_success", "<red>not string provided");
        normalReload = get().getString("normal_reload_success", "<red>not string provided");

        return this;
    }

    public String createDisplay() {
        return createDisplay;
    }


    public String deleteDisplay() {
        return deleteDisplay;
    }

    public String pageZeroDeleteError() {
        return pageZeroDeleteError;
    }

    public String createPage() {
        return createPage;
    }

    public String removePage() {
        return removePage;
    }

    public String advancedReload() {
        return advancedReload;
    }

    public String normalReload() {
        return normalReload;
    }
}
