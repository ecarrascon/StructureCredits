package com.eccarrascon.structurecredits;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static com.eccarrascon.structurecredits.registry.KeyMapRegistry.*;

@Environment(EnvType.CLIENT)
public class StructureCreditsClient {

    public static void onInitializeClient() {
        KeyMappingRegistry.register(DEACTIVATE_MSG_KEYMAPPING);
        KeyMappingRegistry.register(SHOW_AGAIN_MSG_KEYMAPPING);
        KeyMappingRegistry.register(DONT_SHOW_MSG_KEYMAPPING);

    }
}
