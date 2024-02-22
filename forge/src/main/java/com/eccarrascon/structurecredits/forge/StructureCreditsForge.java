package com.eccarrascon.structurecredits.forge;

import dev.architectury.platform.forge.EventBuses;
import com.eccarrascon.structurecredits.StructureCredits;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(StructureCredits.MOD_ID)
public class StructureCreditsForge {
    public StructureCreditsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(StructureCredits.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        StructureCredits.init();
    }
}
