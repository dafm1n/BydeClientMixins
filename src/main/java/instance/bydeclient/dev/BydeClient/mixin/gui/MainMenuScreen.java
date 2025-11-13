package instance.bydeclient.dev.BydeClient.mixin.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Главное меню BydeClient с красивым дизайном
 */
public class MainMenuScreen extends GuiScreen {
    private GuiButton singleplayerButton;
    private GuiButton multiplayerButton;
    private GuiButton storeButton;
    private GuiButton quitButton;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_SPACING = 15;

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Кнопка Singleplayer
        singleplayerButton = new GuiButton(
                1,
                centerX - BUTTON_WIDTH / 2,
                centerY - 60,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "Singleplayer"
        );
        this.buttonList.add(singleplayerButton);

        // Кнопка Multiplayer
        multiplayerButton = new GuiButton(
                2,
                centerX - BUTTON_WIDTH / 2,
                centerY - 60 + BUTTON_HEIGHT + BUTTON_SPACING,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "Multiplayer"
        );
        this.buttonList.add(multiplayerButton);

        // Кнопка Store
        storeButton = new GuiButton(
                3,
                centerX - BUTTON_WIDTH / 2,
                centerY - 60 + (BUTTON_HEIGHT + BUTTON_SPACING) * 2,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "Store"
        );
        this.buttonList.add(storeButton);

        // Кнопка Quit
        quitButton = new GuiButton(
                4,
                centerX - BUTTON_WIDTH / 2,
                centerY - 60 + (BUTTON_HEIGHT + BUTTON_SPACING) * 3,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                "Quit"
        );
        this.buttonList.add(quitButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Красивый фон с градиентом
        drawBackground(mouseX, mouseY);

        // Логотип/название
        drawLogo();

        // Рисуем кнопки с красивым стилем
        drawCustomButtons(mouseX, mouseY);
    }

    private void drawBackground(int mouseX, int mouseY) {
        // Полупрозрачный черный фон
        drawRect(0, 0, this.width, this.height, 0xFF000000);

        // Фон с небольшым градиентом
        drawGradient(0, 0, this.width, this.height, 0x44000033, 0x44003300);
    }

    private void drawGradient(int x1, int y1, int x2, int y2, int startColor, int endColor) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_QUADS);

        // Левый верхний угол (startColor)
        setColor(startColor);
        GL11.glVertex2i(x1, y1);

        // Правый верхний угол (startColor)
        GL11.glVertex2i(x2, y1);

        // Правый нижний угол (endColor)
        setColor(endColor);
        GL11.glVertex2i(x2, y2);

        // Левый нижний угол (endColor)
        GL11.glVertex2i(x1, y2);

        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void setColor(int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }

    private void drawLogo() {
        String title = "SILENT CLIENT";
        String version = "v2.0";

        int centerX = this.width / 2;
        int logoY = 30;

        // Рисуем логотип (белый квадрат)
        drawRoundedRect(centerX - 25, logoY - 20, centerX + 25, logoY + 20, 5, 0xFFFFFFFF);

        // Текст в логотипе
        this.fontRendererObj.drawString(
                "SC",
                centerX - 5,
                logoY - 4,
                0xFF000000
        );

        // Название
        this.fontRendererObj.drawString(
                title,
                centerX - this.fontRendererObj.getStringWidth(title) / 2,
                logoY + 40,
                0xFFFFFFFF
        );

        // Версия
        this.fontRendererObj.drawString(
                version,
                centerX - this.fontRendererObj.getStringWidth(version) / 2,
                logoY + 55,
                0xFFAAAAAA
        );
    }

    private void drawCustomButtons(int mouseX, int mouseY) {
        for (GuiButton button : this.buttonList) {
            boolean isHovered = mouseX >= button.xPosition && mouseX < button.xPosition + 200 &&
                               mouseY >= button.yPosition && mouseY < button.yPosition + 40;

            // Цвет кнопки
            int bgColor = isHovered ? 0xFF00AA00 : 0xFF2a2a2a;
            int borderColor = isHovered ? 0xFF00FF00 : 0xFF1a1a1a;

            // Рисуем закругленную кнопку
            drawRoundedRect(button.xPosition, button.yPosition, button.xPosition + 200,
                          button.yPosition + 40, 5, bgColor);

            // Граница кнопки
            drawRoundedBorder(button.xPosition, button.yPosition, button.xPosition + 200,
                            button.yPosition + 40, 5, 2, borderColor);

            // Текст кнопки
            int textColor = isHovered ? 0xFF000000 : 0xFFFFFFFF;
            this.fontRendererObj.drawString(
                    button.displayString,
                    button.xPosition + 100 - this.fontRendererObj.getStringWidth(button.displayString) / 2,
                    button.yPosition + 16,
                    textColor
            );
        }
    }

    private void drawRoundedRect(int x1, int y1, int x2, int y2, int radius, int color) {
        drawRect(x1 + radius, y1, x2 - radius, y2, color);
        drawRect(x1, y1 + radius, x2, y2 - radius, color);

        // Углы
        drawRect(x1, y1, x1 + radius, y1 + radius, color);
        drawRect(x2 - radius, y1, x2, y1 + radius, color);
        drawRect(x1, y2 - radius, x1 + radius, y2, color);
        drawRect(x2 - radius, y2 - radius, x2, y2, color);
    }

    private void drawRoundedBorder(int x1, int y1, int x2, int y2, int radius, int thickness, int color) {
        // Верхняя граница
        drawRect(x1 + radius, y1, x2 - radius, y1 + thickness, color);
        // Нижняя граница
        drawRect(x1 + radius, y2 - thickness, x2 - radius, y2, color);
        // Левая граница
        drawRect(x1, y1 + radius, x1 + thickness, y2 - radius, color);
        // Правая граница
        drawRect(x2 - thickness, y1 + radius, x2, y2 - radius, color);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            // Singleplayer - открываем выбор мира
            this.mc.displayGuiScreen(new net.minecraft.client.gui.GuiSelectWorld(this));
        } else if (button.id == 2) {
            // Multiplayer - открываем список серверов
            this.mc.displayGuiScreen(new net.minecraft.client.gui.GuiMultiplayer(this));
        } else if (button.id == 3) {
            // Store - можно открыть браузер или просто сообщение
            if (this.mc.thePlayer != null) {
                this.mc.thePlayer.addChatMessage(
                        new net.minecraft.util.ChatComponentText("§cStore coming soon!")
                );
            }
        } else if (button.id == 4) {
            // Quit - выход
            this.mc.shutdown();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

