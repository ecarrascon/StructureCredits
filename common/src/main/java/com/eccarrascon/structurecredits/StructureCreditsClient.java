package com.eccarrascon.structurecredits;

import com.ecarrascon.carrasconlib.config.LibConfig;
import com.eccarrascon.structurecredits.client.StructureOverlayRenderer;
import com.eccarrascon.structurecredits.event.DisplayNameClient;
import com.eccarrascon.structurecredits.network.StructureCreditsNet;
import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
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

        StructureCreditsNet.initializeClient();

        KeyMapRegistry keyMapRegistry = KeyMapRegistry.getInstance();

        // Register all keybindings
        KeyMappingRegistry.register(keyMapRegistry.getToggleActiveKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getToggleOnlyOneTimeKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getToggleChatMessageKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getToggleShowCreatorKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getToggleContinuousDisplayKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getToggleRequireDifferentStructureKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getShowAgainMsgKeyMapping());
        KeyMappingRegistry.register(keyMapRegistry.getDontShowMsgKeyMapping());

        ClientTickEvent.CLIENT_POST.register(new DisplayNameClient());

        // Register HUD overlay renderer
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, partialTick) -> {
            if (!CONFIG_VALUES.isChatMessage()) {
                StructureOverlayRenderer.render(guiGraphics, partialTick);
            }
        });
    }
}