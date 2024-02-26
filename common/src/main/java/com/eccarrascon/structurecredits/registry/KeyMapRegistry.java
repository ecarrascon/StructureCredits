package com.eccarrascon.structurecredits.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class KeyMapRegistry {
    public static final KeyMapping CUSTOM_KEYMAPPING = new KeyMapping(
            "key.structurecredits.deactivate_key", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            -1, // The default keycode
            "category.structurecredits.keys" // The category translation key used to categorize in the Controls screen
    );


}
