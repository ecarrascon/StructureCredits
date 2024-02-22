package com.eccarrascon.structurecredits.event;

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

import static dev.architectury.utils.GameInstance.getServer;

public class DetectStructure implements TickEvent.Player {

    private ResourceKey<Structure> actualStructure;

    private Integer tickCounter = 0;

    public static ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel)
            return (ServerLevel) level;
        MinecraftServer server = getServer();
        return server == null ? null : server.getLevel(level.dimension());
    }

    @Override
    public void tick(net.minecraft.world.entity.player.Player player) {
        isPlayerInAnyStructure(player, getServerLevel(player.level()), player.getX(), player.getY(), player.getZ());
    }

    public void isPlayerInAnyStructure(net.minecraft.world.entity.player.Player player, ServerLevel level, double x, double y, double z) {

        if (actualStructure != null && LocationPredicate.inStructure(actualStructure).matches(level, x, y, z)) {
            return;
        } else if (actualStructure != null && !LocationPredicate.inStructure(actualStructure).matches(level, x, y, z)) {
            actualStructure = null;
            tickCounter = 1;
        }
        if (tickCounter >= 1) {
            tickCounter++;
            if (tickCounter >= 300) {
                tickCounter = 0;
            }
        }

        if (tickCounter != 0) {
            return;
        }
        for (ResourceKey<Structure> structureKey : ObtainAllStructuresEvent.allStructures) {
            LocationPredicate locationPredicate = LocationPredicate.inStructure(structureKey);
            if (locationPredicate.matches(level, x, y, z)) {
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

                    if (!modName.equals("Minecraft")) {
                        player.displayClientMessage(Component.translatable("text.structurecredits.message", structureName, modName).withStyle(ChatFormatting.WHITE), true);
                    }
                    actualStructure = structureKey;
                }

            }
        }

    }
}