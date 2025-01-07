package com.eccarrascon.structurecredits;

import com.eccarrascon.structurecredits.event.DetectStructure;
import com.eccarrascon.structurecredits.event.ObtainAllStructuresEvent;
import com.eccarrascon.structurecredits.network.StructureCreditsNet;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.architectury.platform.Platform.isModLoaded;

public class StructureCredits {
    public static final String MOD_ID = "structurecredits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DIMD_COMPAT = isModLoaded("dimdungeons");

    public static void init() {
        StructureCreditsNet.initialize();
        LifecycleEvent.SERVER_LEVEL_LOAD.register(new ObtainAllStructuresEvent());
        TickEvent.PLAYER_POST.register(new DetectStructure());

    }
}
