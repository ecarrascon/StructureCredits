package com.eccarrascon.structurecredits.neoforge;

import com.eccarrascon.structurecredits.StructureCredits;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(StructureCredits.MOD_ID)
public class StructureCreditsForge {
    public StructureCreditsForge(IEventBus modBus) {
        StructureCredits.init();
    }
}
