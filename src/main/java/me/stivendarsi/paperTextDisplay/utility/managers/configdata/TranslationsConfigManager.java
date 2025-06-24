package me.stivendarsi.paperTextDisplay.utility.managers.configdata;

import me.stivendarsi.paperTextDisplay.utility.managers.TranslationsConfig;

public class TranslationsConfigManager {

    private String createDisplay;
    private String deleteDisplay;
    private String pageZeroDeleteError;
    private String createPage;
    private String removePage;
    private String advancedReload;
    private String normalReload;


    public TranslationsConfigManager load(){
        createDisplay = TranslationsConfig.get().getString("create_display");
        deleteDisplay = TranslationsConfig.get().getString("delete_display");

        pageZeroDeleteError = TranslationsConfig.get().getString("delete_page_zero_error");
        createPage = TranslationsConfig.get().getString("create_page");
        removePage = TranslationsConfig.get().getString("remove_page");

        advancedReload = TranslationsConfig.get().getString("advanced_reload_success");
        normalReload = TranslationsConfig.get().getString("normal_reload_success");

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
