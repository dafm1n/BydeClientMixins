package instance.bydeclient.dev.BydeClient.mixin.gui;

import instance.bydeclient.dev.BydeClient.mixin.modules.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Экран Settings для настройки параметров модулей
 */
public class SettingsScreen extends GuiScreen {
    private GuiButton backButton;
    private GuiButton fpsBoostInfoButton;
    private GuiScreen parent;
    private ModuleManager moduleManager;

    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_HEIGHT = 500;
    private static final int PADDING = 15;

    public SettingsScreen(GuiScreen parent) {
        this.parent = parent;
        this.moduleManager = ModuleManager.getInstance();
    }

    @Override
    public void initGui() {
        // Кнопка назад
        backButton = new GuiButton(1, 10, 10, 100, 20, "Back");
        this.buttonList.add(backButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Полупрозрачный фон
        drawRect(0, 0, this.width, this.height, 0x88000000);

        // Основная панель
        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        drawRoundedPanelRect(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 10, 0xFF0f0f0f);
        drawRoundedPanelBorder(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 10, 2, 0xFF2a2a2a);

        // Заголовок
        this.fontRendererObj.drawStringWithShadow(
                "§lSettings",
                panelX + PADDING,
                panelY + 15,
                0xFFFFFF
        );

        // Информация о модулях
        drawSettingsInfo(panelX + PADDING, panelY + 50);

        // Кнопка назад
        backButton.drawButton(this.mc, mouseX, mouseY);
    }

    private void drawSettingsInfo(int x, int y) {
        int currentY = y;

        // FPS Boost информация
        FPSBoostModule fpsModule = moduleManager.getFPSBoostModule();
        this.fontRendererObj.drawStringWithShadow("§n§lFPS Boost Module", x, currentY, 0xFFFFFF);
        currentY += 12;

        if (fpsModule != null && fpsModule.isEnabled()) {
            this.fontRendererObj.drawString("§aStatus: ACTIVE", x + 10, currentY, 0x00FF00);
            currentY += 11;


            this.fontRendererObj.drawString(
                    "FPS: " + fpsModule.getCurrentFPS(),
                    x + 10,
                    currentY,
                    0xFFFFFF
            );
            currentY += 11;
        } else {
            this.fontRendererObj.drawString("§cStatus: DISABLED", x + 10, currentY, 0xFF0000);
            currentY += 11;
        }

        currentY += 15;

        // Motion Blur информация
        this.fontRendererObj.drawStringWithShadow("§n§lMotion Blur Module", x, currentY, 0xFFFFFF);
        currentY += 12;
        MotionBlurModule motionModule = moduleManager.getMotionBlurModule();
        String motionStatus = motionModule != null && motionModule.isEnabled() ? "§aON" : "§cOFF";
        this.fontRendererObj.drawString("Status: " + motionStatus, x + 10, currentY, 0xFFFFFF);
        currentY += 11;

        currentY += 15;

        // Inventory Blur информация
        this.fontRendererObj.drawStringWithShadow("§n§lInventory Blur Module", x, currentY, 0xFFFFFF);
        currentY += 12;
        InventoryBlurModule invModule = moduleManager.getInventoryBlurModule();
        String invStatus = invModule != null && invModule.isEnabled() ? "§aON" : "§cOFF";
        this.fontRendererObj.drawString("Status: " + invStatus, x + 10, currentY, 0xFFFFFF);
        currentY += 11;

        currentY += 15;

        // Player Counter информация
        this.fontRendererObj.drawStringWithShadow("§n§lPlayer Counter Module", x, currentY, 0xFFFFFF);
        currentY += 12;
        PlayerCounterModule playerModule = moduleManager.getPlayerCounterModule();
        String playerStatus = playerModule != null && playerModule.isEnabled() ? "§aON" : "§cOFF";
        this.fontRendererObj.drawString("Status: " + playerStatus, x + 10, currentY, 0xFFFFFF);
        currentY += 11;
        if (playerModule != null && playerModule.isEnabled()) {
            this.fontRendererObj.drawString(
                    "Players: " + playerModule.getPlayerCount(),
                    x + 10,
                    currentY,
                    0xFFFFFF
            );
            currentY += 11;
        }

        currentY += 15;

        // Scoreboard информация
        this.fontRendererObj.drawStringWithShadow("§n§lScoreboard Module", x, currentY, 0xFFFFFF);
        currentY += 12;
        ScoreboardModule scoreboardModule = moduleManager.getScoreboardModule();
        String scoreboardStatus = scoreboardModule != null && scoreboardModule.isEnabled() ? "§aON" : "§cOFF";
        this.fontRendererObj.drawString("Status: " + scoreboardStatus, x + 10, currentY, 0xFFFFFF);
        currentY += 11;
    }

    private void drawPanelRect(int x1, int y1, int x2, int y2, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawRoundedPanelRect(int x1, int y1, int x2, int y2, int radius, int color) {
        drawPanelRect(x1 + radius, y1, x2 - radius, y2, color);
        drawPanelRect(x1, y1 + radius, x2, y2 - radius, color);
        drawPanelRect(x1, y1, x1 + radius, y1 + radius, color);
        drawPanelRect(x2 - radius, y1, x2, y1 + radius, color);
        drawPanelRect(x1, y2 - radius, x1 + radius, y2, color);
        drawPanelRect(x2 - radius, y2 - radius, x2, y2, color);
    }

    private void drawRoundedPanelBorder(int x1, int y1, int x2, int y2, int radius, int thickness, int color) {
        drawPanelRect(x1 + radius, y1, x2 - radius, y1 + thickness, color);
        drawPanelRect(x1 + radius, y2 - thickness, x2 - radius, y2, color);
        drawPanelRect(x1, y1 + radius, x1 + thickness, y2 - radius, color);
        drawPanelRect(x2 - thickness, y1 + radius, x2, y2 - radius, color);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            // Назад
            this.mc.displayGuiScreen(parent);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC
            this.mc.displayGuiScreen(parent);
        }
        super.keyTyped(typedChar, keyCode);
    }
}

