package com.eccarrascon.structurecredits.api;

import com.eccarrascon.structurecredits.StructureCredits;
import com.eccarrascon.structurecredits.StructureCreditsClient;

import java.util.HashMap;
import java.util.Map;

/**
 * register custom display names for structures. Override the default structure identifiers for
 * client display purposes.
 * <p>
 * <strong>Usage:</strong> Call the registration methods during your mod's client-side initialization,
 * for example in a client setup event. This ensures that the client configuration is available. If a
 * registration is attempted before the configuration is loaded, the mapping will be stored and later flushed
 * automatically.
 * </p>
 */
public class StructureCreditsAPI {

    /**
     * Temporary storage for mappings when the client configuration is not yet initialized.
     * This field is internal and not intended for direct use by external mod developers.
     */
    private static final Map<String, String> pendingMappings = new HashMap<>();

    /**
     * Registers a custom display name for a single structure.
     * <p>
     * If the client configuration is available, the custom mapping is immediately applied and saved.
     * Otherwise, the mapping is stored and will be automatically applied once the configuration is loaded.
     * </p>
     *
     * @param originalStructure the identifier of the structure to override (e.g. "mod:structure")
     * @param customStructure   the custom identifier to display (e.g. "henry_the_builder:cool_castle")
     */
    public static void registerCustomStructure(String originalStructure, String customStructure) {
        if (StructureCreditsClient.CONFIG_VALUES == null) {
            pendingMappings.put(originalStructure, customStructure);
        } else {
            StructureCreditsClient.CONFIG_VALUES.getCustomStructureName().put(originalStructure, customStructure);
            StructureCreditsClient.CONFIG.save();
            StructureCredits.LOGGER.info("Registered custom structure name: {} -> {}", originalStructure, customStructure);
        }
    }

    /**
     * Registers multiple custom structure mappings in a single call.
     * <p>
     * Each entry in the provided map represents a mapping from an original structure identifier to a custom display name.
     * If the client configuration is not yet available, the mappings are stored and will be automatically applied
     * once the configuration is loaded.
     * </p>
     *
     * @param mappings a map where keys are original structure identifiers (e.g. "mod:structure")
     *                 and values are the corresponding custom display names (e.g. "henry_the_builder:cool_castle")
     */
    public static void registerCustomStructures(Map<String, String> mappings) {
        if (StructureCreditsClient.CONFIG_VALUES == null) {
            pendingMappings.putAll(mappings);
        } else {
            mappings.forEach((originalStructure, customStructure) -> {
                StructureCreditsClient.CONFIG_VALUES.getCustomStructureName().put(originalStructure, customStructure);
                StructureCredits.LOGGER.info("Registered custom structure name: {} -> {}", originalStructure, customStructure);
            });
            StructureCreditsClient.CONFIG.save();
        }
    }

    /**
     * <em>Internal API:</em> Flushes any pending mappings that were registered before the client configuration was available.
     * <p>
     * This method is intended for internal use only by the StructureCredits mod.
     * It should be called after the client configuration has been initialized (typically during
     * {@link StructureCreditsClient#onInitializeClient()}).
     * </p>
     */
    public static void flushPendingMappings() {
        if (StructureCreditsClient.CONFIG_VALUES != null) {
            pendingMappings.forEach((original, custom) -> {
                StructureCreditsClient.CONFIG_VALUES.getCustomStructureName().put(original, custom);
                StructureCredits.LOGGER.info("Flushed pending mapping: {} -> {}", original, custom);
            });
            if (!pendingMappings.isEmpty()) {
                StructureCreditsClient.CONFIG.save();
            }
            pendingMappings.clear();
        }
    }
}
