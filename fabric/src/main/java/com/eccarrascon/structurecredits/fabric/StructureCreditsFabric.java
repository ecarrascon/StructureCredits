package com.eccarrascon.structurecredits.fabric;

import com.eccarrascon.structurecredits.StructureCredits;
import net.fabricmc.api.ModInitializer;

public class StructureCreditsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        StructureCredits.init();
    }
}
