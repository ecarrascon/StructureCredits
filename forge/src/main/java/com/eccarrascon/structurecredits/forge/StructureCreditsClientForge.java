package com.eccarrascon.structurecredits.forge;

import com.eccarrascon.structurecredits.StructureCredits;
import com.eccarrascon.structurecredits.StructureCreditsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = StructureCredits.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StructureCreditsClientForge {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        StructureCreditsClient.onInitializeClient();
    }
}
