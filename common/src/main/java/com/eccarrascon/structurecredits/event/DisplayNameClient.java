package com.eccarrascon.structurecredits.event;

import com.eccarrascon.structurecredits.client.StructureOverlayRenderer;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.eccarrascon.structurecredits.StructureCreditsClient.CONFIG;
import static com.eccarrascon.structurecredits.StructureCreditsClient.CONFIG_VALUES;

public class DisplayNameClient implements ClientTickEvent.Client {
    private static String lastStructure = "haley:you_found_it!";
    private static String currentStructure = null;
    private static String previousStructure = null;

    @Override
    public void tick(Minecraft instance) {
        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance();

        while (keyMapRegistry.getToggleActiveKeyMapping().consumeClick()) {
            toggleActiveState();
        }

        while (keyMapRegistry.getToggleOnlyOneTimeKeyMapping().consumeClick()) {
            toggleOnlyOneTime();
        }

        while (keyMapRegistry.getToggleChatMessageKeyMapping().consumeClick()) {
            toggleChatMessage();
        }

        while (keyMapRegistry.getToggleShowCreatorKeyMapping().consumeClick()) {
            toggleShowCreator();
        }

        while (keyMapRegistry.getToggleContinuousDisplayKeyMapping().consumeClick()) {
            toggleContinuousDisplay();
        }

        while (keyMapRegistry.getToggleRequireDifferentStructureKeyMapping().consumeClick()) {
            toggleRequireDifferentStructure();
        }

        while (keyMapRegistry.getShowAgainMsgKeyMapping().consumeClick()) {
            showLastStructureMessage();
        }

        while (keyMapRegistry.getDontShowMsgKeyMapping().consumeClick()) {
            addCurrentStructureToDontShow();
        }

        if (!CONFIG_VALUES.isChatMessage()) {
            StructureOverlayRenderer.tick();
        }
    }

    public static void updateStructureName(String structureName, boolean isPacket) {
        if (isPacket) {
            if (structureName == null || structureName.isEmpty()) {
                previousStructure = currentStructure;
                currentStructure = null;
                if (!CONFIG_VALUES.isChatMessage()) {
                    StructureOverlayRenderer.clearMessage();
                }
                return;
            }

            if (Objects.equals(currentStructure, structureName)) {
                return;
            }

            if (CONFIG_VALUES.isRequireDifferentStructure() && !CONFIG_VALUES.isOnlyOneTime()) {
                if (Objects.equals(previousStructure, structureName)) {
                    currentStructure = structureName;
                    return;
                }
            }

            previousStructure = currentStructure;
            currentStructure = structureName;
        }

        if (CONFIG_VALUES.isActive() || !isPacket) {
            lastStructure = structureName;
            displayStructureMessage(structureName, isPacket);
        }
    }

    private static void displayStructureMessage(String structureName, boolean isPacket) {
        String customName = CONFIG_VALUES.getCustomStructureName().getOrDefault(structureName, structureName);
        String[] parts = customName.split(":");
        if (parts.length == 2) {
            String modName = formatName(parts[0]);
            String structureNameFormatted = formatName(parts[1]);
            boolean showCreator = CONFIG_VALUES.isShowCreator();

            if (!isPacket || (CONFIG_VALUES.getDontShowAll().stream().noneMatch(structureName::startsWith) && !CONFIG_VALUES.getDontShow().contains(structureName))) {
                if (CONFIG_VALUES.isChatMessage()) {
                    String messageKey = showCreator ? "text.structurecredits.message" : "text.structurecredits.message_no_creator";
                    Component messageComponent = Component.translatable(messageKey, structureNameFormatted, modName);
                    Minecraft.getInstance().player.displayClientMessage(messageComponent, false);
                } else {
                    StructureOverlayRenderer.setMessage(structureNameFormatted, modName, showCreator);
                }

                if (isPacket && CONFIG_VALUES.isOnlyOneTime() && !CONFIG_VALUES.getDontShow().contains(structureName)) {
                    CONFIG_VALUES.getDontShow().add(structureName);
                    CONFIG.save();
                }
            }
        }
    }

    private static String formatName(String name) {
        return Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private void toggleActiveState() {
        boolean isActive = !CONFIG_VALUES.isActive();
        CONFIG_VALUES.setActive(isActive);
        CONFIG.save();
        String messageKey = isActive ? "text.structurecredits.activated" : "text.structurecredits.deactivated";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void toggleOnlyOneTime() {
        boolean value = !CONFIG_VALUES.isOnlyOneTime();
        CONFIG_VALUES.setShowOnlyOneTime(value);
        CONFIG.save();
        String messageKey = value ? "text.structurecredits.only_one_time_enabled" : "text.structurecredits.only_one_time_disabled";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void toggleChatMessage() {
        boolean value = !CONFIG_VALUES.isChatMessage();
        CONFIG_VALUES.setChatMessage(value);
        CONFIG.save();
        String messageKey = value ? "text.structurecredits.chat_message_enabled" : "text.structurecredits.chat_message_disabled";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void toggleShowCreator() {
        boolean value = !CONFIG_VALUES.isShowCreator();
        CONFIG_VALUES.setShowCreator(value);
        CONFIG.save();
        String messageKey = value ? "text.structurecredits.show_creator_enabled" : "text.structurecredits.show_creator_disabled";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void toggleContinuousDisplay() {
        boolean value = !CONFIG_VALUES.isContinuousDisplay();
        CONFIG_VALUES.setContinuousDisplay(value);
        CONFIG.save();
        String messageKey = value ? "text.structurecredits.continuous_display_enabled" : "text.structurecredits.continuous_display_disabled";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void toggleRequireDifferentStructure() {
        boolean value = !CONFIG_VALUES.isRequireDifferentStructure();
        CONFIG_VALUES.setRequireDifferentStructure(value);
        CONFIG.save();
        String messageKey = value ? "text.structurecredits.require_different_enabled" : "text.structurecredits.require_different_disabled";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void showLastStructureMessage() {
        if (lastStructure != null) {
            updateStructureName(lastStructure, false);
        }
    }

    private void addCurrentStructureToDontShow() {
        if (lastStructure != null) {
            if (!CONFIG_VALUES.getDontShow().contains(lastStructure)) {
                CONFIG_VALUES.getDontShow().add(lastStructure);
                CONFIG.save();
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.dont_show"), true);
            } else {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.already_dont_show"), true);
            }
        }
    }
}