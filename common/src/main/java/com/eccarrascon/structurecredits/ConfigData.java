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

    // Require visiting different structure before showing same structure again
    private boolean requireDifferentStructure = true;
    private boolean continuousDisplay = false;
    private boolean autoCalculateDuration = true;

    private float textSize = 1.0f;
    private int labelColor = 0xAAAAAA;
    private int nameColor = 0xFFFFFF;
    private int displayDuration = 60;

    private String displayPosition = "BOTTOM_CENTER"; // TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    private int displayOffsetX = 0;
    private int displayOffsetY = 40;

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

    public boolean isRequireDifferentStructure() {
        return requireDifferentStructure;
    }

    public void setRequireDifferentStructure(boolean requireDifferentStructure) {
        this.requireDifferentStructure = requireDifferentStructure;
    }

    public boolean isContinuousDisplay() {
        return continuousDisplay;
    }

    public void setContinuousDisplay(boolean continuousDisplay) {
        this.continuousDisplay = continuousDisplay;
    }

    public boolean isAutoCalculateDuration() {
        return autoCalculateDuration;
    }

    public void setAutoCalculateDuration(boolean autoCalculateDuration) {
        this.autoCalculateDuration = autoCalculateDuration;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getNameColor() {
        return nameColor;
    }

    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }

    public int getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(int displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(String displayPosition) {
        this.displayPosition = displayPosition;
    }

    public int getDisplayOffsetX() {
        return displayOffsetX;
    }

    public void setDisplayOffsetX(int displayOffsetX) {
        this.displayOffsetX = displayOffsetX;
    }

    public int getDisplayOffsetY() {
        return displayOffsetY;
    }

    public void setDisplayOffsetY(int displayOffsetY) {
        this.displayOffsetY = displayOffsetY;
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