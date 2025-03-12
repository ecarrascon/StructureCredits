package com.eccarrascon.structurecredits.network;

import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;


public class StructureCreditsNet {
    public static final ResourceLocation STRUCTURE_NAME_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath("structurecredits", "sync_structure_name");

    public static void registerPackets() {
        NetworkAggregator.registerS2CType(STRUCTURE_NAME_PACKET_ID, List.of());
    }

    public static void sendStructureName(ServerPlayer serverPlayer, String structureName) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeUtf(structureName);

        RegistryFriendlyByteBuf registryBuf = new RegistryFriendlyByteBuf(buf, serverPlayer.server.registryAccess());

        NetworkManager.sendToPlayer(serverPlayer, STRUCTURE_NAME_PACKET_ID, registryBuf);
    }
}
