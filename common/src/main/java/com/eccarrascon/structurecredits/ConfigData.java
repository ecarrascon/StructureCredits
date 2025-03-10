package com.eccarrascon.structurecredits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigData {
    private boolean active = true;
    private boolean showOnlyOneTime = true;
    private boolean chatMessage = false;
    private boolean showCreator = true;

    private Map<String, String> customStructureName = new HashMap<>() {{
        put("minecraft:swamp_hut", "a_cat:cat_hut");
    }};

    private List<String> dontShowAll = new ArrayList<>(List.of("minecraft:", "dimdungeons:"));
    private List<String> dontShow = new ArrayList<>(List.of("minecraft:plains_village", "minecraft:desert_village"));

    public ConfigData() {
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

    public void setShowOnlyOneTime(boolean showOnlyOneTime) {
        this.showOnlyOneTime = showOnlyOneTime;
    }

    public boolean isChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(boolean chatMessage) {
        this.chatMessage = chatMessage;
    }

    public boolean isShowCreator() {
        return showCreator;
    }

    public void setShowCreator(boolean showCreator) {
        this.showCreator = showCreator;
    }

    public Map<String, String> getCustomStructureName() {
        return customStructureName;
    }

    public void setCustomStructureName(Map<String, String> customStructureName) {
        this.customStructureName = customStructureName;
    }

    public List<String> getDontShowAll() {
        return dontShowAll;
    }

    public void setDontShowAll(List<String> dontShowAll) {
        this.dontShowAll = dontShowAll;
    }

    public List<String> getDontShow() {
        return dontShow;
    }

    public void setDontShow(List<String> dontShow) {
        this.dontShow = dontShow;
    }
}
