package instance.bydeclient.dev.BydeClient.mixin.gui;

import instance.bydeclient.dev.BydeClient.mixin.modules.*;
import instance.bydeclient.dev.BydeClient.mixin.utils.PositionManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * GUI для редактирования позиций HUD элементов (Keystrokes, CPS, Ping, ArmorStatus)
 */
public class EditHudScreen extends GuiScreen {
    private GuiButton doneButton;
    private GuiButton resetButton;
    private GuiScreen parent;
    private PositionManager positionManager;

    private KeystrokesModule keystrokesModule;
    private ArmorStatusModule armorStatusModule;
    private PingModule pingModule;
    private FPSModule fpsModule;
    private CPSModule cpsModule;

    private SelectedElement selectedElement = null;
    private int dragStartX = 0;
    private int dragStartY = 0;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    private static final int ELEMENT_WIDTH = 120;
    private static final int ELEMENT_HEIGHT = 60;

    public EditHudScreen(GuiScreen parent) {
        this.parent = parent;
        this.positionManager = PositionManager.getInstance();
        ModuleManager manager = ModuleManager.getInstance();
        this.keystrokesModule = manager.getKeystrokesModule();
        this.armorStatusModule = manager.getArmorStatusModule();
        this.pingModule = manager.getPingModule();
        this.fpsModule = manager.getFPSModule();
        this.cpsModule = manager.getCpsModule();
    }

    @Override
    public void initGui() {
        // Кнопка "Done"
        doneButton = new GuiButton(1, this.width - 110, 10, 100, 20, "Done");
        this.buttonList.add(doneButton);

        // Кнопка "Reset Positions"
        resetButton = new GuiButton(2, this.width - 110, 35, 100, 20, "Reset");
        this.buttonList.add(resetButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Полупрозрачный фон
        drawRect(0, 0, this.width, this.height, 0x88000000);

        // Заголовок
        this.fontRendererObj.drawStringWithShadow(
                "§lEdit HUD",
                20,
                10,
                0xFFFFFF
        );

        // Инструкция
        this.fontRendererObj.drawStringWithShadow(
                "Drag elements to reposition them",
                20,
                25,
                0xAAAAAA
        );

        // Рисуем элементы
        drawHudElement("Keystrokes", keystrokesModule.getPosX(), keystrokesModule.getPosY(), mouseX, mouseY);
        drawHudElement("ArmorStatus", armorStatusModule.getPosX(), armorStatusModule.getPosY(), mouseX, mouseY);
        drawHudElement("Ping", pingModule.getPosX(), pingModule.getPosY(), mouseX, mouseY);
        drawHudElement("FPS", fpsModule.getPosX(), fpsModule.getPosY(), mouseX, mouseY);
        drawHudElement("CPS", cpsModule.getPosX(), cpsModule.getPosY(), mouseX, mouseY);

        // Рисуем кнопки
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawHudElement(String name, int x, int y, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX < x + ELEMENT_WIDTH &&
                           mouseY >= y && mouseY < y + ELEMENT_HEIGHT;

        // Фон элемента с закругленными углами
        int bgColor = isHovered ? 0xFF3a3a3a : 0xFF1a1a1a;
        drawRoundedRectInternal(x, y, x + ELEMENT_WIDTH, y + ELEMENT_HEIGHT, 5, bgColor);

        // Граница с закругленными углами
        drawRoundedBorderInternal(x, y, x + ELEMENT_WIDTH, y + ELEMENT_HEIGHT, 5, 1, 0xFF2a2a2a);

        // Заголовок элемента
        this.fontRendererObj.drawStringWithShadow(
                name,
                x + 8,
                y + 8,
                0xFFFFFF
        );

        // Координаты
        this.fontRendererObj.drawString(
                "X: " + x + " Y: " + y,
                x + 8,
                y + 20,
                0xAAAAAA
        );

        // Статус выделения
        if (selectedElement != null && selectedElement.name.equals(name)) {
            this.fontRendererObj.drawString(
                    "SELECTED",
                    x + 8,
                    y + 30,
                    0x00FF00
            );
        } else if (isHovered) {
            this.fontRendererObj.drawString(
                    "Click to select",
                    x + 8,
                    y + 30,
                    0xFFFF00
            );
        }
    }

    private void drawRectInternal(int x1, int y1, int x2, int y2, int color) {
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

    private void drawRoundedRectInternal(int x1, int y1, int x2, int y2, int radius, int color) {
        drawRectInternal(x1 + radius, y1, x2 - radius, y2, color);
        drawRectInternal(x1, y1 + radius, x2, y2 - radius, color);
        drawRectInternal(x1, y1, x1 + radius, y1 + radius, color);
        drawRectInternal(x2 - radius, y1, x2, y1 + radius, color);
        drawRectInternal(x1, y2 - radius, x1 + radius, y2, color);
        drawRectInternal(x2 - radius, y2 - radius, x2, y2, color);
    }

    private void drawRoundedBorderInternal(int x1, int y1, int x2, int y2, int radius, int thickness, int color) {
        drawRectInternal(x1 + radius, y1, x2 - radius, y1 + thickness, color);
        drawRectInternal(x1 + radius, y2 - thickness, x2 - radius, y2, color);
        drawRectInternal(x1, y1 + radius, x1 + thickness, y2 - radius, color);
        drawRectInternal(x2 - thickness, y1 + radius, x2, y2 - radius, color);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            // Проверяем клик по элементам
            if (isMouseInElement(mouseX, mouseY, keystrokesModule.getPosX(), keystrokesModule.getPosY())) {
                selectedElement = new SelectedElement("Keystrokes", keystrokesModule.getPosX(), keystrokesModule.getPosY());
                dragStartX = mouseX;
                dragStartY = mouseY;
                dragOffsetX = mouseX - selectedElement.x;
                dragOffsetY = mouseY - selectedElement.y;
            } else if (isMouseInElement(mouseX, mouseY, armorStatusModule.getPosX(), armorStatusModule.getPosY())) {
                selectedElement = new SelectedElement("ArmorStatus", armorStatusModule.getPosX(), armorStatusModule.getPosY());
                dragStartX = mouseX;
                dragStartY = mouseY;
                dragOffsetX = mouseX - selectedElement.x;
                dragOffsetY = mouseY - selectedElement.y;
            } else if (isMouseInElement(mouseX, mouseY, pingModule.getPosX(), pingModule.getPosY())) {
                selectedElement = new SelectedElement("Ping", pingModule.getPosX(), pingModule.getPosY());
                dragStartX = mouseX;
                dragStartY = mouseY;
                dragOffsetX = mouseX - selectedElement.x;
                dragOffsetY = mouseY - selectedElement.y;
            } else if (isMouseInElement(mouseX, mouseY, fpsModule.getPosX(), fpsModule.getPosY())) {
                selectedElement = new SelectedElement("FPS", fpsModule.getPosX(), fpsModule.getPosY());
                dragStartX = mouseX;
                dragStartY = mouseY;
                dragOffsetX = mouseX - selectedElement.x;
                dragOffsetY = mouseY - selectedElement.y;
            } else if (isMouseInElement(mouseX, mouseY, cpsModule.getPosX(), cpsModule.getPosY())) {
                selectedElement = new SelectedElement("CPS", cpsModule.getPosX(), cpsModule.getPosY());
                dragStartX = mouseX;
                dragStartY = mouseY;
                dragOffsetX = mouseX - selectedElement.x;
                dragOffsetY = mouseY - selectedElement.y;
            } else {
                selectedElement = null;
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceLastClick) {
        if (selectedElement != null && lastButtonClicked == 0) {
            int newX = mouseX - dragOffsetX;
            int newY = mouseY - dragOffsetY;

            // Ограничиваем координаты
            newX = Math.max(0, Math.min(newX, this.width - ELEMENT_WIDTH));
            newY = Math.max(50, Math.min(newY, this.height - ELEMENT_HEIGHT));

            selectedElement.x = newX;
            selectedElement.y = newY;

            // Обновляем позиции в модулях
            switch (selectedElement.name) {
                case "Keystrokes":
                    keystrokesModule.setPosX(newX);
                    keystrokesModule.setPosY(newY);
                    break;
                case "ArmorStatus":
                    armorStatusModule.setPosX(newX);
                    armorStatusModule.setPosY(newY);
                    break;
                case "Ping":
                    pingModule.setPosX(newX);
                    pingModule.setPosY(newY);
                    break;
                case "FPS":
                    fpsModule.setPosX(newX);
                    fpsModule.setPosY(newY);
                    break;
                case "CPS":
                    cpsModule.setPosX(newX);
                    cpsModule.setPosY(newY);
                    break;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            // Done button
            this.mc.displayGuiScreen(parent);
        } else if (button.id == 2) {
            // Reset button
            keystrokesModule.setPosX(10);
            keystrokesModule.setPosY(10);
            armorStatusModule.setPosX(10);
            armorStatusModule.setPosY(40);
            pingModule.setPosX(10);
            pingModule.setPosY(100);
            fpsModule.setPosX(10);
            fpsModule.setPosY(130);
            cpsModule.setPosX(10);
            cpsModule.setPosY(160);
            selectedElement = null;
        }
    }

    private boolean isMouseInElement(int mouseX, int mouseY, int elemX, int elemY) {
        return mouseX >= elemX && mouseX < elemX + ELEMENT_WIDTH &&
               mouseY >= elemY && mouseY < elemY + ELEMENT_HEIGHT;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC
            this.mc.displayGuiScreen(parent);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Класс для хранения информации о выбранном элементе
     */
    private static class SelectedElement {
        String name;
        int x;
        int y;

        SelectedElement(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }
}

