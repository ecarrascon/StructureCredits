package com.eccarrascon.structurecredits;

import com.eccarrascon.structurecredits.event.DetectStructure;
import com.eccarrascon.structurecredits.event.ObtainAllStructuresEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.eccarrascon.structurecredits.registry.KeyMapRegistry.CUSTOM_KEYMAPPING;
import static dev.architectury.platform.Platform.isModLoaded;

public class StructureCredits {
    public static final String MOD_ID = "structurecredits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DIMD_COMPAT = isModLoaded("dimdungeons");
    public static ConfigData CONFIG_VALUES = new ConfigData();

    public static void init() {
        CONFIG_VALUES = ConfigData.init();
        LifecycleEvent.SERVER_LEVEL_LOAD.register(new ObtainAllStructuresEvent());
        TickEvent.PLAYER_POST.register(new DetectStructure());
        KeyMappingRegistry.register(CUSTOM_KEYMAPPING);
    }
}
