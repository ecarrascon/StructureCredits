package com.eccarrascon.structurecredits;

import com.ecarrascon.carrasconlib.config.LibConfig;
import com.eccarrascon.structurecredits.event.DisplayNameClient;
import com.eccarrascon.structurecredits.network.StructureCreditsNet;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static com.eccarrascon.structurecredits.api.StructureCreditsAPI.flushPendingMappings;


@Environment(EnvType.CLIENT)
public class StructureCreditsClient {

    public static LibConfig<ConfigData> CONFIG;
    public static ConfigData CONFIG_VALUES;

    public static void onInitializeClient() {
        CONFIG = new LibConfig<>(
                "structurecredits-config.json",
                ConfigData.class,
                StructureCredits.LOGGER
        );

        CONFIG_VALUES = CONFIG.get();

        flushPendingMappings();

        ClientTickEvent.CLIENT_POST.register(new DisplayNameClient());

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, StructureCreditsNet.STRUCTURE_NAME_PACKET_ID, (buf, context) -> {
            String structureName = buf.readUtf();
            context.queue(() -> {
                DisplayNameClient.updateStructureName(structureName, true);
            });
        });


    }

    public static void onInitializeRegisterKey() {
        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance();

        KeyMappingRegistry.register(keyMapRegistry.getDeactivateMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getShowAgainMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getDontShowMsgKeyMapping());
    }
}

