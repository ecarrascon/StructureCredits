package com.eccarrascon.structurecredits.event;

import com.catastrophe573.dimdungeons.dimension.DungeonData;
import com.catastrophe573.dimdungeons.structure.DungeonRoom;
import com.catastrophe573.dimdungeons.utils.DungeonUtils;
import com.eccarrascon.structurecredits.StructureCredits;
import com.eccarrascon.structurecredits.network.StructureCreditsNet;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;

import static dev.architectury.utils.GameInstance.getServer;

public class DetectStructure implements TickEvent.Player {

    private int ticksSinceCheck = 0;
    private Holder<Structure> actualStructure;
    private String actualDimensionalStructure;

    public static ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel serverLevel)
            return serverLevel;
        MinecraftServer server = getServer();
        return server == null ? null : server.getLevel(level.dimension());
    }

    @Override
    public void tick(net.minecraft.world.entity.player.Player player) {
        if (player.level().isClientSide()) return;

        if (StructureCredits.DIMD_COMPAT && DungeonUtils.isDimensionDungeon(player.level())) {
            handleDimensionalDungeonDetection(player);
        } else {
            ticksSinceCheck++;
            if (ticksSinceCheck >= 20) {
                ticksSinceCheck = 0;
                isPlayerInAnyStructure(player, getServerLevel(player.level()), player.getX(), player.getY(), player.getZ());
            }
        }
    }

    private void handleDimensionalDungeonDetection(net.minecraft.world.entity.player.Player player) {
        DungeonRoom room = DungeonData.get(player.level()).getRoomAtPos(player.chunkPosition());
        if (room != null && (actualDimensionalStructure == null || !actualDimensionalStructure.equals(room.structure))) {
            actualDimensionalStructure = room.structure;
            if (player instanceof ServerPlayer serverPlayer) {
                StructureCreditsNet.sendStructureName(serverPlayer, room.structure);
            }
        }
    }

    public void isPlayerInAnyStructure(net.minecraft.world.entity.player.Player player, ServerLevel level, double x, double y, double z) {
        if (actualStructure != null && LocationPredicate.Builder.inStructure(actualStructure).build().matches(level, x, y, z)) {
            return;
        }

        actualStructure = null;

        ChunkPos playerChunkPos = new ChunkPos((int) x >> 4, (int) z >> 4);
        List<StructureStart> structuresInChunk = level.structureManager()
                .startsForStructure(playerChunkPos, predicate -> true);

        for (StructureStart structure : structuresInChunk) {
            ResourceLocation id = level.registryAccess()
                    .registryOrThrow(Registries.STRUCTURE)
                    .getKey(structure.getStructure());
            ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, id);

            Holder<Structure> holder = level.registryAccess()
                    .registryOrThrow(Registries.STRUCTURE)
                    .getHolderOrThrow(key);

            if (LocationPredicate.Builder.inStructure(holder).build().matches(level, x, y, z)) {
                actualStructure = holder;
                if (player instanceof ServerPlayer sp) {
                    StructureCreditsNet.sendStructureName(sp, id.toString());
                }
                break;
            }
        }
    }
}