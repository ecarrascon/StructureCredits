package com.eccarrascon.structurecredits.event;

import com.catastrophe573.dimdungeons.dimension.DungeonData;
import com.catastrophe573.dimdungeons.structure.DungeonRoom;
import com.catastrophe573.dimdungeons.utils.DungeonUtils;
import com.eccarrascon.structurecredits.StructureCredits;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.eccarrascon.structurecredits.registry.KeyMapRegistry.CUSTOM_KEYMAPPING;
import static dev.architectury.utils.GameInstance.getServer;

public class DetectStructure implements TickEvent.Player {

    private ResourceKey<Structure> actualStructure;
    private String actualDimensionalStructure;

    private Integer tickCounter = 0;
    private boolean isActive = true;

    private boolean isInDontShowAll = false;

    public static ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel serverLevel)
            return serverLevel;
        MinecraftServer server = getServer();
        return server == null ? null : server.getLevel(level.dimension());
    }

    @Override
    public void tick(net.minecraft.world.entity.player.Player player) {
        if (player.level().isClientSide()) {
            while (CUSTOM_KEYMAPPING.consumeClick()) {
                isActive = !isActive;
                if (!isActive) {
                    player.displayClientMessage(Component.translatable("text.structurecredits.deactivated").withStyle(ChatFormatting.GRAY), true);
                } else {
                    player.displayClientMessage(Component.translatable("text.structurecredits.activated").withStyle(ChatFormatting.GRAY), true);
                }
            }
            return;
        }
        if (!isActive) {
            return;
        }
        if (StructureCredits.DIMD_COMPAT && DungeonUtils.isDimensionDungeon(player.level())) {
            DungeonRoom room = DungeonData.get(player.level()).getRoomAtPos(player.chunkPosition());
            if (room != null && (actualDimensionalStructure == null || !actualDimensionalStructure.matches(room.structure))) {
                actualDimensionalStructure = room.structure;
                displayDimensionalDungeonMessage(player, actualDimensionalStructure);
            }
        } else {
            isPlayerInAnyStructure(player, getServerLevel(player.level()), player.getX(), player.getY(), player.getZ());
        }
    }

    public void isPlayerInAnyStructure(net.minecraft.world.entity.player.Player player, ServerLevel level, double x, double y, double z) {

        if (actualStructure != null && LocationPredicate.inStructure(actualStructure).matches(level, x, y, z)) {
            return;
        } else if (actualStructure != null && !LocationPredicate.inStructure(actualStructure).matches(level, x, y, z)) {
            actualStructure = null;
            tickCounter = 1;
        }

        if (tickCounter >= 1) {
            if (tickCounter <= StructureCredits.CONFIG_VALUES.getCooldown() * 20) {
                tickCounter++;
            } else if (tickCounter >= StructureCredits.CONFIG_VALUES.getCooldown() * 20) {
                tickCounter = 0;
            }
        }

        if (tickCounter != 0) {
            return;
        }
        for (ResourceKey<Structure> structureKey : ObtainAllStructuresEvent.allStructures) {
            LocationPredicate locationPredicate = LocationPredicate.inStructure(structureKey);
            if (locationPredicate.matches(level, x, y, z)) {
                displayStructureMessage(player, structureKey);
            }
        }

    }

    private void displayStructureMessage(net.minecraft.world.entity.player.Player player, ResourceKey<Structure> structureKey) {
        String fullLocation = structureKey.location().toString();
        String[] parts = fullLocation.split(":");
        if (parts.length == 2) {
            String modName = parts[0];
            String structureName = parts[1];

            modName = Arrays.stream(modName.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));

            structureName = Arrays.stream(structureName.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));


            isInDontShowAllList(fullLocation);

            if (!isInDontShowAll && !StructureCredits.CONFIG_VALUES.getDontShow().contains(fullLocation)) {
                player.displayClientMessage(Component.translatable("text.structurecredits.message", structureName, modName).withStyle(ChatFormatting.WHITE), true);
            }
            actualStructure = structureKey;
        }
    }

    private void displayDimensionalDungeonMessage(net.minecraft.world.entity.player.Player player, String structureKey) {

        String[] parts = structureKey.split(":");
        if (parts.length == 2) {
            String modName = parts[0];
            String structureName = parts[1];

            modName = Arrays.stream(modName.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));

            structureName = Arrays.stream(structureName.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));

            isInDontShowAllList(structureKey);

            if (!isInDontShowAll && !StructureCredits.CONFIG_VALUES.getDontShow().contains(structureKey)) {
                player.displayClientMessage(Component.translatable("text.structurecredits.message", structureName, modName).withStyle(ChatFormatting.WHITE), true);
            }
        }
    }

    private void isInDontShowAllList(String structureKey) {
        isInDontShowAll = false;
        for (String prefix : StructureCredits.CONFIG_VALUES.getDontShowAll()) {
            if (structureKey.startsWith(prefix)) {
                isInDontShowAll = true;
                break;
            }
        }
    }
}
