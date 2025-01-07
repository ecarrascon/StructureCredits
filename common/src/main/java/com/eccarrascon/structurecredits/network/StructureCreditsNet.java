package com.eccarrascon.structurecredits.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

import static com.eccarrascon.structurecredits.StructureCredits.MOD_ID;


public interface StructureCreditsNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(MOD_ID);

    // Define the message types
    MessageType STRUCTURE_NAME_SYNC = NET.registerS2C("structure_name_sync", StructureNameSyncMessage::new);

    static void initialize() {
        // Any additional setup can go here if needed
    }

    static void initializeClient() {
        // Client-specific setup, if any
    }
}
