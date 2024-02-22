package com.eccarrascon.structurecredits;

import com.eccarrascon.structurecredits.event.DetectStructure;
import com.eccarrascon.structurecredits.event.ObtainAllStructuresEvent;
import com.google.common.base.Suppliers;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class StructureCredits {
    public static final String MOD_ID = "structurecredits";

    public static void init() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(new ObtainAllStructuresEvent());
        TickEvent.PLAYER_POST.register(new DetectStructure());
    }
}
