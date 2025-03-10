package com.eccarrascon.structurecredits.event;

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

        if (CONFIG_VALUES.isActive() || !isPacket) {
            lastStructure = structureName;
            String customName = CONFIG_VALUES.getCustomStructureName().getOrDefault(structureName, structureName);
            String[] parts = customName.split(":");
            if (parts.length == 2) {
                String modName = formatName(parts[0]);
                String structureNameFormatted = formatName(parts[1]);

                if (!isPacket || (CONFIG_VALUES.getDontShowAll().stream().noneMatch(structureName::startsWith) && !CONFIG_VALUES.getDontShow().contains(structureName))) {
                    String messageKey = CONFIG_VALUES.isShowCreator() ? "text.structurecredits.message" : "text.structurecredits.message_no_creator";
                    Minecraft.getInstance().player.displayClientMessage(
                            Component.translatable(messageKey, structureNameFormatted, modName),
                            !CONFIG_VALUES.isChatMessage()
                    );

                    if (isPacket && CONFIG_VALUES.isOnlyOneTime() && !CONFIG_VALUES.getDontShow().contains(structureName)) {
                        CONFIG_VALUES.getDontShow().add(structureName);
                        CONFIG.save();
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

        boolean isActive = !CONFIG_VALUES.isActive();
        CONFIG_VALUES.setActive(isActive);
        CONFIG.save();
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
