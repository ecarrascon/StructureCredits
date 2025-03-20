package com.eccarrascon.structurecredits.api;

import com.eccarrascon.structurecredits.StructureCredits;
import com.eccarrascon.structurecredits.StructureCreditsClient;

public class StructureCreditsAPI {

    /**
     * Registers a custom display name for a structure.
     *
     * @param originalStructure the identifier of the structure to override (e.g. "mod:structure")
     * @param customStructure   the custom identifier to display (e.g. "henry_the_builder:cool_castle")
     */
    public static void registerCustomStructure(String originalStructure, String customStructure) {

        StructureCreditsClient.CONFIG_VALUES.getCustomStructureName().put(originalStructure, customStructure);

        StructureCreditsClient.CONFIG.save();
        StructureCredits.LOGGER.info("Registered custom structure name: {} -> {}", originalStructure, customStructure);
    }
}
