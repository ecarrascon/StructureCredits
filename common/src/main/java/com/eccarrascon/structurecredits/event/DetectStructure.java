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

    private ResourceKey<Structure> actualStructure;
    private String actualDimensionalStructure;

    private boolean justDisplayed = false;
    private int tickCounter = 0;
    private int tickCounterForDisplay = 0;
    private boolean showAgain = false;
    private int namesWordCount = 0;

    private boolean isInDontShowAll = false;

    public static ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel serverLevel)
            return serverLevel;
        MinecraftServer server = getServer();
        return server == null ? null : server.getLevel(level.dimension());
    }

    @Override
    public void tick(net.minecraft.world.entity.player.Player player) {
        boolean isActive = StructureCredits.CONFIG_VALUES.isActive();
        if (player.level().isClientSide()) {
            handleClientSideActions(player, isActive);
            return;
        }
        if (!isActive) {
            return;
        }
        if (StructureCredits.DIMD_COMPAT && DungeonUtils.isDimensionDungeon(player.level())) {
            handleDimensionalDungeonDetection(player);
        } else {
            isPlayerInAnyStructure(player, getServerLevel(player.level()), player.getX(), player.getY(), player.getZ());
        }
    }

    private void handleClientSideActions(net.minecraft.world.entity.player.Player player, boolean isActive) {
        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance(); // Access the singleton instance

        while (keyMapRegistry.getDeactivateMsgKeyMapping().consumeClick()) {
            toggleActiveState(player, isActive);
        }
        while (keyMapRegistry.getShowAgainMsgKeyMapping().consumeClick()) {
            if (actualStructure != null) {
                showAgain = true;
                displayStructureMessage(player, actualStructure);
            }
        }
        while (keyMapRegistry.getDontShowMsgKeyMapping().consumeClick()) {
            if (actualStructure != null) {
                manageDontShowList(player, actualStructure);
            }
        }
    }

    private void toggleActiveState(net.minecraft.world.entity.player.Player player, boolean isActive) {
        isActive = !isActive;
        String messageKey = isActive ? "text.structurecredits.activated" : "text.structurecredits.deactivated";
        player.displayClientMessage(Component.translatable(messageKey), true);
        StructureCredits.CONFIG_VALUES.setActive(isActive);
        ConfigData.save(StructureCredits.CONFIG_VALUES);
    }

    private void manageDontShowList(net.minecraft.world.entity.player.Player player,  ResourceKey<Structure> structure) {
        String structureLocation = structure.location().toString();
        if (!StructureCredits.CONFIG_VALUES.getDontShow().contains(structureLocation)) {
            player.displayClientMessage(Component.translatable("text.structurecredits.dont_show"), true);
            StructureCredits.CONFIG_VALUES.getDontShow().add(structureLocation);
            ConfigData.save(StructureCredits.CONFIG_VALUES);
        } else {
            player.displayClientMessage(Component.translatable("text.structurecredits.already_dont_show"), true);
        }
    }

    private void handleDimensionalDungeonDetection(net.minecraft.world.entity.player.Player player) {
        DungeonRoom room = DungeonData.get(player.level()).getRoomAtPos(player.chunkPosition());
        if (room != null && (actualDimensionalStructure == null || !actualDimensionalStructure.equals(room.structure))) {
            actualDimensionalStructure = room.structure;
            displayDimensionalDungeonMessage(player, actualDimensionalStructure);
        }
    }

    public void isPlayerInAnyStructure(net.minecraft.world.entity.player.Player player, ServerLevel level, double x, double y, double z) {
        if (actualStructure != null && LocationPredicate.inStructure(actualStructure).matches(level, x, y, z)) {
            if (!StructureCredits.CONFIG_VALUES.isChatMessage() && justDisplayed) {
                manageMessageDisplayCooldown();
            }
            return;
        }

        actualStructure = null;
        tickCounter = (tickCounter > 0) ? tickCounter + 1 : 0;

        if (tickCounter > StructureCredits.CONFIG_VALUES.getCooldown() * 20) {
            tickCounter = 0;
        }

        if (tickCounter != 0) {
            return;
        }

        for (ResourceKey<Structure> structureKey : ObtainAllStructuresEvent.allStructures) {
            if (LocationPredicate.inStructure(structureKey).matches(level, x, y, z)) {
                displayStructureMessage(player, structureKey);
                justDisplayed = true;
                break;
            }
        }
    }

    private void manageMessageDisplayCooldown() {
        if ((namesWordCount > 22 && tickCounterForDisplay <= 2 * 20) || (namesWordCount > 15 && tickCounterForDisplay <= 20)) {
            tickCounterForDisplay++;
        } else {
            tickCounterForDisplay = 0;
            justDisplayed = false;
        }
    }

    private void displayStructureMessage(net.minecraft.world.entity.player.Player player, ResourceKey<Structure> structure) {
        String structureLocation = structure.location().toString();
        String customName = StructureCredits.CONFIG_VALUES.getCustomStructureName().getOrDefault(structureLocation, structureLocation);

        String[] parts = customName.split(":");
        if (parts.length == 2) {
            String modName = formatName(parts[0]);
            String structureName = formatName(parts[1]);

            namesWordCount = modName.length() + structureName.length();

            isInDontShowAllList(structureLocation);

            if (showAgain || (!isInDontShowAll && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureLocation))) {
                String messageKey = StructureCredits.CONFIG_VALUES.isShowCreator() ?
                        "text.structurecredits.message" :
                        "text.structurecredits.message_no_creator";

                player.displayClientMessage(Component.translatable(messageKey, structureName, modName), !StructureCredits.CONFIG_VALUES.isChatMessage());
                if (StructureCredits.CONFIG_VALUES.isOnlyOneTime() && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureLocation)) {
                    StructureCredits.CONFIG_VALUES.getDontShow().add(structureLocation);
                    ConfigData.save(StructureCredits.CONFIG_VALUES);
                }
                showAgain = false;
            }
            actualStructure = structure;
        }
    }

    private String formatName(String name) {
        return Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private void displayDimensionalDungeonMessage(net.minecraft.world.entity.player.Player player, String structureKey) {
        String customName = StructureCredits.CONFIG_VALUES.getCustomStructureName().getOrDefault(structureKey, structureKey);
        String[] parts = customName.split(":");
        if (parts.length == 2) {
            String modName = formatName(parts[0]);
            String structureName = formatName(parts[1]);

            isInDontShowAllList(structureKey);

            if (!isInDontShowAll && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureKey)) {
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

    private void isInDontShowAllList(String structureKey) {
        isInDontShowAll = StructureCredits.CONFIG_VALUES.getDontShowAll().stream().anyMatch(structureKey::startsWith);
    }
}