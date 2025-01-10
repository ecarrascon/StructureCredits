package com.eccarrascon.structurecredits.event;

import com.eccarrascon.structurecredits.ConfigData;
import com.eccarrascon.structurecredits.StructureCreditsClient;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisplayNameClient implements ClientTickEvent.Client {
    private static String lastStructure = "haley:you_found_it!";

    @Override
    public void tick(Minecraft instance) {
        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance();

        while (keyMapRegistry.getDeactivateMsgKeyMapping().consumeClick()) {
            toggleActiveState();
        }

        while (keyMapRegistry.getShowAgainMsgKeyMapping().consumeClick()) {
            showLastStructureMessage();
        }

        while (keyMapRegistry.getDontShowMsgKeyMapping().consumeClick()) {
            addCurrentStructureToDontShow();
        }
    }

    public static void updateStructureName(String structureName, boolean isPacket) {
        if (Objects.equals(lastStructure, structureName) && isPacket) {
            return;
        }

        ConfigData config = StructureCreditsClient.CONFIG_VALUES;
        if (config.isActive() || !isPacket) {
            lastStructure = structureName;
            String customName = config.getCustomStructureName().getOrDefault(structureName, structureName);
            String[] parts = customName.split(":");
            if (parts.length == 2) {
                String modName = formatName(parts[0]);
                String structureNameFormatted = formatName(parts[1]);

                if (!isPacket || (config.getDontShowAll().stream().noneMatch(structureName::startsWith) && !config.getDontShow().contains(structureName))) {
                    String messageKey = config.isShowCreator() ? "text.structurecredits.message" : "text.structurecredits.message_no_creator";
                    Minecraft.getInstance().player.displayClientMessage(
                            Component.translatable(messageKey, structureNameFormatted, modName),
                            !config.isChatMessage()
                    );

                    if (isPacket && config.isOnlyOneTime() && !config.getDontShow().contains(structureName)) {
                        config.getDontShow().add(structureName);
                        ConfigData.save(config);
                    }
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
        ConfigData config = StructureCreditsClient.CONFIG_VALUES;
        boolean isActive = !config.isActive();
        config.setActive(isActive);
        ConfigData.save(config);
        String messageKey = isActive ? "text.structurecredits.activated" : "text.structurecredits.deactivated";
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void showLastStructureMessage() {
        if (lastStructure != null) {
            updateStructureName(lastStructure, false);
        }
    }

    private void addCurrentStructureToDontShow() {
        if (lastStructure != null) {
            ConfigData config = StructureCreditsClient.CONFIG_VALUES;

            if (!config.getDontShow().contains(lastStructure)) {
                config.getDontShow().add(lastStructure);
                ConfigData.save(config);
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.dont_show"), true);
            } else {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.already_dont_show"), true);
            }
        }
    }
}
