package com.eccarrascon.structurecredits.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static com.eccarrascon.structurecredits.StructureCreditsClient.CONFIG_VALUES;

public class StructureOverlayRenderer {
    private static String structureName = null;
    private static String modName = null;
    private static boolean showCreator = true;
    private static int displayTimer = 0;
    private static int fadeTimer = 0;
    private static final int FADE_DURATION = 10;
    private static final float WORDS_PER_SECOND = 4.0f;
    private static final int MIN_DISPLAY_TICKS = 40;

    public static void setMessage(String structure, String mod, boolean withCreator) {
        structureName = structure;
        modName = mod;
        showCreator = withCreator;

        if (!CONFIG_VALUES.isContinuousDisplay()) {
            if (CONFIG_VALUES.isAutoCalculateDuration()) {
                displayTimer = calculateReadingDuration(structure, mod, withCreator);
            } else {
                displayTimer = CONFIG_VALUES.getDisplayDuration();
            }
            fadeTimer = 0;
        } else {
            displayTimer = Integer.MAX_VALUE;
            fadeTimer = FADE_DURATION;
        }
    }

    private static int calculateReadingDuration(String structure, String mod, boolean withCreator) {
        int wordCount = countWords(structure);
        if (withCreator) {
            wordCount += countWords(mod) + 4;
        } else {
            wordCount += 2;
        }

        int calculatedTicks = (int) ((wordCount / WORDS_PER_SECOND) * 20);
        return Math.max(MIN_DISPLAY_TICKS, calculatedTicks);
    }

    private static int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    public static void clearMessage() {
        structureName = null;
        modName = null;
        displayTimer = 0;
        fadeTimer = 0;
    }

    public static void tick() {
        if (structureName == null) {
            return;
        }

        if (CONFIG_VALUES.isContinuousDisplay()) {
            fadeTimer = FADE_DURATION;
            displayTimer = Integer.MAX_VALUE;
            return;
        }

        if (displayTimer > 0) {
            displayTimer--;
            if (fadeTimer < FADE_DURATION) {
                fadeTimer++;
            }
        }

        if (displayTimer == 0 && fadeTimer > 0) {
            fadeTimer--;
            if (fadeTimer == 0) {
                clearMessage();
            }
        }
    }

    public static void render(GuiGraphics guiGraphics, float partialTicks) {
        if (structureName == null || fadeTimer <= 0) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float scale = CONFIG_VALUES.getTextSize();
        poseStack.scale(scale, scale, 1.0f);

        String label1 = net.minecraft.network.chat.Component.translatable("text.structurecredits.overlay.welcome").getString();
        String label2 = showCreator ? net.minecraft.network.chat.Component.translatable("text.structurecredits.overlay.by").getString() : "";

        int label1Width = mc.font.width(label1);
        int structureWidth = mc.font.width(structureName);
        int label2Width = mc.font.width(label2);
        int modWidth = showCreator ? mc.font.width(modName) : 0;

        int totalWidth = label1Width + structureWidth + label2Width + modWidth;
        int messageHeight = mc.font.lineHeight;

        int screenWidth = (int) (mc.getWindow().getGuiScaledWidth() / scale);
        int screenHeight = (int) (mc.getWindow().getGuiScaledHeight() / scale);

        int x = calculateX(screenWidth, totalWidth);
        int y = calculateY(screenHeight, messageHeight);

        float alpha = calculateAlpha();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int labelColor = CONFIG_VALUES.getLabelColor();
        int nameColor = CONFIG_VALUES.getNameColor();

        int labelColorWithAlpha = applyAlpha(labelColor, alpha);
        int nameColorWithAlpha = applyAlpha(nameColor, alpha);

        guiGraphics.drawString(mc.font, label1, x, y, labelColorWithAlpha, true);
        x += label1Width;

        guiGraphics.drawString(mc.font, structureName, x, y, nameColorWithAlpha, true);
        x += structureWidth;

        if (showCreator) {
            guiGraphics.drawString(mc.font, label2, x, y, labelColorWithAlpha, true);
            x += label2Width;

            guiGraphics.drawString(mc.font, modName, x, y, nameColorWithAlpha, true);
        }

        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static int applyAlpha(int color, float alpha) {
        int alphaValue = (int) (alpha * 255);
        return (color & 0x00FFFFFF) | (alphaValue << 24);
    }

    private static int calculateX(int screenWidth, int messageWidth) {
        String position = CONFIG_VALUES.getDisplayPosition();
        int offset = CONFIG_VALUES.getDisplayOffsetX();

        return switch (position) {
            case "TOP_LEFT", "BOTTOM_LEFT" -> offset;
            case "TOP_CENTER", "CENTER", "BOTTOM_CENTER" -> (screenWidth - messageWidth) / 2;
            case "TOP_RIGHT", "BOTTOM_RIGHT" -> screenWidth - messageWidth - offset;
            default -> offset;
        };
    }

    private static int calculateY(int screenHeight, int messageHeight) {
        String position = CONFIG_VALUES.getDisplayPosition();
        int offset = CONFIG_VALUES.getDisplayOffsetY();

        return switch (position) {
            case "TOP_LEFT", "TOP_CENTER", "TOP_RIGHT" -> offset;
            case "CENTER" -> (screenHeight - messageHeight) / 2;
            case "BOTTOM_LEFT", "BOTTOM_CENTER", "BOTTOM_RIGHT" -> screenHeight - messageHeight - offset;
            default -> offset;
        };
    }

    private static float calculateAlpha() {
        if (CONFIG_VALUES.isContinuousDisplay()) {
            return 1.0f;
        }

        if (displayTimer > FADE_DURATION) {
            return Math.min(1.0f, fadeTimer / (float) FADE_DURATION);
        }

        if (displayTimer > 0 && fadeTimer >= FADE_DURATION) {
            return 1.0f;
        }

        if (displayTimer == 0) {
            return Math.max(0.0f, fadeTimer / (float) FADE_DURATION);
        }

        return 1.0f;
    }

    public static boolean isDisplaying() {
        return structureName != null && fadeTimer > 0;
    }
}