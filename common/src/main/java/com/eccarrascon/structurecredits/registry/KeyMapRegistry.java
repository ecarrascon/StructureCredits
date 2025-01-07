package com.eccarrascon.structurecredits.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class KeyMapRegistry {
    private static KeyMapRegistry instance;

    private final KeyMapping deactivateMsgKeyMapping;
    private final KeyMapping showAgainMsgKeyMapping;
    private final KeyMapping dontShowMsgKeyMapping;

    private static final String CATEGORY_KEY = "category.structurecredits.keys";

    private KeyMapRegistry() {
        deactivateMsgKeyMapping = new KeyMapping(
                "key.structurecredits.deactivate_key",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        showAgainMsgKeyMapping = new KeyMapping(
                "key.structurecredits.show_again_key",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        dontShowMsgKeyMapping = new KeyMapping(
                "key.structurecredits.dont_show_key",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );
    }

    public static KeyMapRegistry getInstance() {
        if (instance == null) {
            instance = new KeyMapRegistry();
        }
        return instance;
    }

    public KeyMapping getDeactivateMsgKeyMapping() {
        return deactivateMsgKeyMapping;
    }

    public KeyMapping getShowAgainMsgKeyMapping() {
        return showAgainMsgKeyMapping;
    }

    public KeyMapping getDontShowMsgKeyMapping() {
        return dontShowMsgKeyMapping;
    }

    public Set<KeyMapping> getKeys() {
        return Set.of(deactivateMsgKeyMapping, showAgainMsgKeyMapping, dontShowMsgKeyMapping);
    }
}