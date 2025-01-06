package com.eccarrascon.structurecredits.event;

import com.catastrophe573.dimdungeons.dimension.DungeonData;
import com.catastrophe573.dimdungeons.structure.DungeonRoom;
import com.catastrophe573.dimdungeons.utils.DungeonUtils;
import com.eccarrascon.structurecredits.ConfigData;
import com.eccarrascon.structurecredits.StructureCredits;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Arrays;
import java.util.stream.Collectors;

import static dev.architectury.utils.GameInstance.getServer;

public class DetectStructure implements TickEvent.Player {

    private static final int LONG_MESSAGE_THRESHOLD = 22;
    private static final int MEDIUM_MESSAGE_THRESHOLD = 15;
    private static final int TICKS_PER_SECOND = 20;

    private ResourceKey<Structure> currentStructure;
    private String currentDimensionalStructure;

    private boolean messageRecentlyDisplayed = false;
    private int tickCounter = 0;
    private int displayTickCounter = 0;
    private boolean shouldShowAgain = false;
    private int messageWordCount = 0;

    private boolean isInDontShowAllList = false;

    public static ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel;
        }
        MinecraftServer server = getServer();
        return (server == null) ? null : server.getLevel(level.dimension());
    }

    @Override
    public void tick(net.minecraft.world.entity.player.Player player) {
        if (player.level().isClientSide()) {
            handleClientSideInput(player);
            return;
        }

        if (!StructureCredits.CONFIG_VALUES.isActive()) {
            return;
        }

        if (StructureCredits.DIMD_COMPAT && DungeonUtils.isDimensionDungeon(player.level())) {
            handleDimensionalDungeon(player);
        } else {
            checkPlayerStructurePresence(player);
        }
    }

    private void handleClientSideInput(net.minecraft.world.entity.player.Player player) {
        while (KeyMapRegistry.DEACTIVATE_MSG_KEYMAPPING.consumeClick()) {
            toggleActivation(player);
        }
        while (KeyMapRegistry.SHOW_AGAIN_MSG_KEYMAPPING.consumeClick()) {
            if (currentStructure != null) {
                shouldShowAgain = true;
                displayStructureMessage(player, currentStructure);
            }
        }
        while (KeyMapRegistry.DONT_SHOW_MSG_KEYMAPPING.consumeClick()) {
            addToDontShowList(player, currentStructure);
        }
    }

    private void toggleActivation(net.minecraft.world.entity.player.Player player) {
        boolean isActive = !StructureCredits.CONFIG_VALUES.isActive();
        StructureCredits.CONFIG_VALUES.setActive(isActive);
        ConfigData.save(StructureCredits.CONFIG_VALUES);
        String messageKey = isActive ? "text.structurecredits.activated" : "text.structurecredits.deactivated";
        player.displayClientMessage(Component.translatable(messageKey), true);
    }

    private void addToDontShowList(net.minecraft.world.entity.player.Player player, ResourceKey<Structure> structure) {
        if (structure != null) {
            String structureLocation = structure.location().toString();
            if (!StructureCredits.CONFIG_VALUES.getDontShow().contains(structureLocation)) {
                player.displayClientMessage(Component.translatable("text.structurecredits.dont_show"), true);
                StructureCredits.CONFIG_VALUES.getDontShow().add(structureLocation);
                ConfigData.save(StructureCredits.CONFIG_VALUES);
            } else {
                player.displayClientMessage(Component.translatable("text.structurecredits.already_dont_show"), true);
            }
        }
    }

    private void handleDimensionalDungeon(net.minecraft.world.entity.player.Player player) {
        DungeonRoom room = DungeonData.get(player.level()).getRoomAtPos(player.chunkPosition());
        if (room != null && !room.structure.equals(currentDimensionalStructure)) {
            currentDimensionalStructure = room.structure;
            displayDimensionalDungeonMessage(player, currentDimensionalStructure);
        }
    }

    private void checkPlayerStructurePresence(net.minecraft.world.entity.player.Player player) {
        ServerLevel level = getServerLevel(player.level());
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (currentStructure != null && isPlayerInStructure(currentStructure, level, x, y, z)) {
            handleRepeatedMessageDisplay(player);
            return;
        }

        if (currentStructure != null && !isPlayerInStructure(currentStructure, level, x, y, z)) {
            currentStructure = null;
            tickCounter = 1;
        }

        if (tickCounter > 0) {
            handleCooldown();
            return;
        }

        checkNewStructurePresence(player, level, x, y, z);
    }

    private boolean isPlayerInStructure(ResourceKey<Structure> structure, ServerLevel level, double x, double y, double z) {
        return LocationPredicate.inStructure(structure).matches(level, x, y, z);
    }

    private void handleRepeatedMessageDisplay(net.minecraft.world.entity.player.Player player) {
        if (messageRecentlyDisplayed && shouldExtendMessageDisplay()) {
            displayTickCounter++;
        } else {
            displayStructureMessage(player, currentStructure);
            messageRecentlyDisplayed = false;
            displayTickCounter = 0;
        }
    }

    private boolean shouldExtendMessageDisplay() {
        return (messageWordCount > LONG_MESSAGE_THRESHOLD && displayTickCounter <= 2 * TICKS_PER_SECOND) ||
                (messageWordCount > MEDIUM_MESSAGE_THRESHOLD && displayTickCounter <= TICKS_PER_SECOND);
    }

    private void handleCooldown() {
        if (tickCounter <= StructureCredits.CONFIG_VALUES.getCooldown() * TICKS_PER_SECOND) {
            tickCounter++;
        } else {
            tickCounter = 0;
        }
    }

    private void checkNewStructurePresence(net.minecraft.world.entity.player.Player player, ServerLevel level, double x, double y, double z) {
        for (ResourceKey<Structure> structureKey : ObtainAllStructuresEvent.allStructures) {
            if (LocationPredicate.inStructure(structureKey).matches(level, x, y, z)) {
                displayStructureMessage(player, structureKey);
                messageRecentlyDisplayed = true;
                break;
            }
        }
    }

    private void displayStructureMessage(net.minecraft.world.entity.player.Player player, ResourceKey<Structure> structureKey) {
        String fullLocation = structureKey.location().toString();
        String customName = StructureCredits.CONFIG_VALUES.getCustomStructureName().getOrDefault(fullLocation, fullLocation);
        String[] parts = customName.split(":");

        if (parts.length == 2) {
            String modName = formatName(parts[0]);
            String structureName = formatName(parts[1]);

            messageWordCount = modName.replace(" ", "").length() + structureName.replace(" ", "").length();
            updateDontShowAllListStatus(fullLocation);

            if (shouldDisplayMessageAgain(fullLocation)) {
                String messageKey = StructureCredits.CONFIG_VALUES.isShowCreator() ?
                        "text.structurecredits.message" :
                        "text.structurecredits.message_no_creator";

                player.displayClientMessage(Component.translatable(messageKey, structureName, modName), !StructureCredits.CONFIG_VALUES.isChatMessage());

                if (StructureCredits.CONFIG_VALUES.isOnlyOneTime()) {
                    StructureCredits.CONFIG_VALUES.getDontShow().add(fullLocation);
                    ConfigData.save(StructureCredits.CONFIG_VALUES);
                }

                shouldShowAgain = false;
            }

            currentStructure = structureKey;
        }
    }

    private String formatName(String namePart) {
        return Arrays.stream(namePart.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private boolean shouldDisplayMessageAgain(String structureLocation) {
        return shouldShowAgain || (!isInDontShowAllList && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureLocation));
    }

    private void displayDimensionalDungeonMessage(net.minecraft.world.entity.player.Player player, String structureKey) {
        String customName = StructureCredits.CONFIG_VALUES.getCustomStructureName().getOrDefault(structureKey, structureKey);
        String[] parts = customName.split(":");

        if (parts.length == 2) {
            String modName = formatName(parts[0]);
            String structureName = formatName(parts[1]);

            updateDontShowAllListStatus(structureKey);

            if (!isInDontShowAllList && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureKey)) {
                String messageKey = StructureCredits.CONFIG_VALUES.isShowCreator() ?
                        "text.structurecredits.message_dimensional_dungeon" :
                        "text.structurecredits.message_no_creator";

                player.displayClientMessage(Component.translatable(messageKey, structureName, modName), !StructureCredits.CONFIG_VALUES.isChatMessage());

                if (StructureCredits.CONFIG_VALUES.isOnlyOneTime()) {
                    StructureCredits.CONFIG_VALUES.getDontShow().add(structureKey);
                    ConfigData.save(StructureCredits.CONFIG_VALUES);
                }
            }
        }
    }

    private void updateDontShowAllListStatus(String structureKey) {
        isInDontShowAllList = StructureCredits.CONFIG_VALUES.getDontShowAll().stream()
                .anyMatch(structureKey::startsWith);
    }
}
