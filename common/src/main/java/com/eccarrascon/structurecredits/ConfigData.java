package com.eccarrascon.structurecredits;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

public class ConfigData {
    private static final File CONFIG_DATA_FILE = new File(GetConfigDir.getConfigDirectory().toFile(), "structurecredits-config.json");

    private boolean active = true;
    private boolean showOnlyOneTime = true;
    private int cooldown = 30;

    private boolean chatMessage = false;

    private List<String> dontShowAll = List.of("minecraft:", "dimdungeons:");

    private List<String> dontShow = List.of("minecraft:plains_village", "minecraft:desert_village");



    public ConfigData() {
    }

    public static ConfigData init() {
        ConfigData configuration = new ConfigData();
        if (!CONFIG_DATA_FILE.exists()) {
            save(configuration);
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_DATA_FILE.toPath())) {
            configuration = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, ConfigData.class);
        } catch (IOException e) {
            StructureCredits.LOGGER.error("Failed to load the configuration file. Possible issues with the file format or content. Using default configuration. Details: {}", e.getMessage(), e);
        }

        return configuration;
    }

    public static void save(ConfigData config) {
        try (Writer writer = Files.newBufferedWriter(CONFIG_DATA_FILE.toPath())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, writer);
        } catch (IOException e) {
            StructureCredits.LOGGER.error("Error while trying to save configuration file. Details: {}", e.getMessage(), e);
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public List<String> getDontShow() {
        return dontShow;
    }

    public List<String> getDontShowAll() {
        return dontShowAll;
    }

    public void setDontShow(List<String> dontShow) {
        this.dontShow = dontShow;
    }

    public void setDontShowAll(List<String> dontShowAll) {
        this.dontShowAll = dontShowAll;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOnlyOneTime() {
        return showOnlyOneTime;
    }

    public boolean isChatMessage() {
        return chatMessage;
    }
}