package com.eccarrascon.structurecredits.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

import static com.eccarrascon.structurecredits.StructureCredits.MOD_ID;


public interface StructureCreditsNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(MOD_ID);

    MessageType STRUCTURE_NAME_SYNC = NET.registerS2C("structure_name_sync", StructureNameSyncMessage::new);

    static void initialize() {

    }

    static void initializeClient() {

    }
}
