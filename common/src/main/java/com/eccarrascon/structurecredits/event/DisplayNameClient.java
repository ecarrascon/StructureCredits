package com.eccarrascon.structurecredits.event;

import com.eccarrascon.structurecredits.ConfigData;
import com.eccarrascon.structurecredits.StructureCreditsClient;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayNameClient implements ClientTickEvent.Client {
    private static final Set<String> displayedStructures = new HashSet<>();
    private static String lastStructureMessage;

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

    public static void updateStructureName(String structureName) {
        if (!displayedStructures.contains(structureName)) {
            ConfigData config = StructureCreditsClient.CONFIG_VALUES;

            String customName = config.getCustomStructureName().getOrDefault(structureName, structureName);
            String[] parts = customName.split(":");
            if (parts.length == 2) {
                String modName = formatName(parts[0]);
                String structureNameFormatted = formatName(parts[1]);

                if (!config.getDontShowAll().stream().anyMatch(structureName::startsWith) && !config.getDontShow().contains(structureName)) {
                    String messageKey = config.isShowCreator() ? "text.structurecredits.message" : "text.structurecredits.message_no_creator";
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable(messageKey, structureNameFormatted, modName), !config.isChatMessage());

                    lastStructureMessage = Component.translatable(messageKey, structureNameFormatted, modName).getString();

                    if (config.isOnlyOneTime()) {
                        config.getDontShow().add(structureName);
                        ConfigData.save(config);
                    }

                    displayedStructures.add(structureName);
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
        if (lastStructureMessage != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(lastStructureMessage), true);
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.no_last_message"), true);
        }
    }

    private void addCurrentStructureToDontShow() {
        if (lastStructureMessage != null) {
            ConfigData config = StructureCreditsClient.CONFIG_VALUES;

            String structureName = lastStructureMessage.split(",")[1].trim(); // Extract structure name (adjust logic if needed)
            if (!config.getDontShow().contains(structureName)) {
                config.getDontShow().add(structureName);
                ConfigData.save(config);
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.added_to_dont_show", structureName), true);
            } else {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.already_in_dont_show", structureName), true);
            }
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.structurecredits.no_last_message"), true);
        }
    }
}
