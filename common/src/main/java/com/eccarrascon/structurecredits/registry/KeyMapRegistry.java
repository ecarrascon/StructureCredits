package com.eccarrascon.structurecredits.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public class KeyMapRegistry {
    public static final KeyMapping DEACTIVATE_MSG_KEYMAPPING = new KeyMapping(
            "key.structurecredits.deactivate_key", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            -1, // The default keycode
            "category.structurecredits.keys" // The category translation key used to categorize in the Controls screen
    );

    public static final KeyMapping SHOW_AGAIN_MSG_KEYMAPPING = new KeyMapping(
            "key.structurecredits.show_again_key", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            -1, // The default keycode
            "category.structurecredits.keys" // The category translation key used to categorize in the Controls screen
    );


}
