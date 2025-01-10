package com.eccarrascon.structurecredits.network;

import com.eccarrascon.structurecredits.event.DisplayNameClient;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class StructureNameSyncMessage extends BaseS2CMessage {
    private final String structureName;

    public StructureNameSyncMessage(String structureName) {
        this.structureName = structureName;
    }

    public StructureNameSyncMessage(FriendlyByteBuf buf) {
        this.structureName = buf.readUtf(32767);
    }

    @Override
    public MessageType getType() {
        return StructureCreditsNet.STRUCTURE_NAME_SYNC;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(structureName);
    }


    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> DisplayNameClient.updateStructureName(structureName, true));
    }


}
