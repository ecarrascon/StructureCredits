package com.eccarrascon.structurecredits.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class KeyMapRegistry {
    private static KeyMapRegistry instance;

    // Toggle keys for boolean configs
    private final KeyMapping toggleActiveKeyMapping;
    private final KeyMapping toggleOnlyOneTimeKeyMapping;
    private final KeyMapping toggleChatMessageKeyMapping;
    private final KeyMapping toggleShowCreatorKeyMapping;
    private final KeyMapping toggleContinuousDisplayKeyMapping;
    private final KeyMapping toggleRequireDifferentStructureKeyMapping;

    // Action keys
    private final KeyMapping showAgainMsgKeyMapping;
    private final KeyMapping dontShowMsgKeyMapping;

    private static final String CATEGORY_KEY = "category.structurecredits.keys";

    private KeyMapRegistry() {
        // Toggle keys for boolean configs
        toggleActiveKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_active",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        toggleOnlyOneTimeKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_only_one_time",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        toggleChatMessageKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_chat_message",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        toggleShowCreatorKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_show_creator",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        toggleContinuousDisplayKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_continuous_display",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        toggleRequireDifferentStructureKeyMapping = new KeyMapping(
                "key.structurecredits.toggle_require_different",
                InputConstants.Type.KEYSYM,
                -1,
                CATEGORY_KEY
        );

        // Action keys
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

    public KeyMapping getToggleActiveKeyMapping() {
        return toggleActiveKeyMapping;
    }

    public KeyMapping getToggleOnlyOneTimeKeyMapping() {
        return toggleOnlyOneTimeKeyMapping;
    }

    public KeyMapping getToggleChatMessageKeyMapping() {
        return toggleChatMessageKeyMapping;
    }

    public KeyMapping getToggleShowCreatorKeyMapping() {
        return toggleShowCreatorKeyMapping;
    }

    public KeyMapping getToggleContinuousDisplayKeyMapping() {
        return toggleContinuousDisplayKeyMapping;
    }

    public KeyMapping getToggleRequireDifferentStructureKeyMapping() {
        return toggleRequireDifferentStructureKeyMapping;
    }

    public KeyMapping getShowAgainMsgKeyMapping() {
        return showAgainMsgKeyMapping;
    }

    public KeyMapping getDontShowMsgKeyMapping() {
        return dontShowMsgKeyMapping;
    }

    public Set<KeyMapping> getKeys() {
        return Set.of(
                toggleActiveKeyMapping,
                toggleOnlyOneTimeKeyMapping,
                toggleChatMessageKeyMapping,
                toggleShowCreatorKeyMapping,
                toggleContinuousDisplayKeyMapping,
                toggleRequireDifferentStructureKeyMapping,
                showAgainMsgKeyMapping,
                dontShowMsgKeyMapping
        );
    }
}