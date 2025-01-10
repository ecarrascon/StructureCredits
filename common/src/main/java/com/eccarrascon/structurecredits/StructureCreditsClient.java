package com.eccarrascon.structurecredits;

import com.eccarrascon.structurecredits.event.DisplayNameClient;
import com.eccarrascon.structurecredits.network.StructureCreditsNet;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class StructureCreditsClient {

    public static ConfigData CONFIG_VALUES = new ConfigData();


    public static void onInitializeClient() {
        CONFIG_VALUES = ConfigData.init();
        StructureCreditsNet.initializeClient();

        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance(); // Access the singleton instance

        KeyMappingRegistry.register(keyMapRegistry.getDeactivateMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getShowAgainMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getDontShowMsgKeyMapping());

        ClientTickEvent.CLIENT_POST.register(new DisplayNameClient());
    }
}

