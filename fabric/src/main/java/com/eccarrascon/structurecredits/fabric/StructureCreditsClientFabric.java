package com.eccarrascon.structurecredits.fabric;

import com.eccarrascon.structurecredits.StructureCreditsClient;
import net.fabricmc.api.ClientModInitializer;

public class StructureCreditsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StructureCreditsClient.onInitializeClient();
    }
}
