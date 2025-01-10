package com.eccarrascon.structurecredits.neoforge;

import com.eccarrascon.structurecredits.StructureCredits;

import com.eccarrascon.structurecredits.registry.KeyMapRegistry;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static com.eccarrascon.structurecredits.StructureCreditsClient.onInitializeClient;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = StructureCredits.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value =  Dist.CLIENT)
public class StructureCreditsClientForge {

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
        for (KeyMapping kb : KeyMapRegistry.getInstance().getKeys()) {
            event.register(kb);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        onInitializeClient();

    }
}
