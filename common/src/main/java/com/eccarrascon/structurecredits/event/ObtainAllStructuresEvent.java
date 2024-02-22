package com.eccarrascon.structurecredits.event;

import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ObtainAllStructuresEvent implements LifecycleEvent.ServerLevelState {

    public static Iterable<ResourceKey<Structure>> allStructures;

    /**
     * This Event is responsible for obtaining all structures in "Registries".
     * Only when the ServerLevel is loaded, this event will be triggered.
     */
    @Override
    public void act(ServerLevel level) {
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        allStructures = structureRegistry.registryKeySet();

    }
}
