package com.eccarrascon.structurecredits;

import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static com.eccarrascon.structurecredits.registry.KeyMapRegistry.*;

@Environment(EnvType.CLIENT)
public class StructureCreditsClient {

    public static void onInitializeClient() {
        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance(); // Access the singleton instance

        KeyMappingRegistry.register(keyMapRegistry.getDeactivateMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getShowAgainMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getDontShowMsgKeyMapping());
    }
}

