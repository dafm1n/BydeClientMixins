package instance.bydeclient.dev.BydeClient.mixin.gui;

import instance.bydeclient.dev.BydeClient.mixin.modules.Module;
import instance.bydeclient.dev.BydeClient.mixin.modules.ModuleManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleGuiScreen extends GuiScreen {
    private static final int GRID_COLS = 5;
    private static final int MODULE_BOX_SIZE = 95;
    private static final int MODULE_BOX_SPACING = 12;
    private static final int PADDING = 20;
    private static final int HEADER_HEIGHT = 60;
    private static final int ROUNDED_RADIUS = 8;

    private GuiTextField searchField;
    private GuiButton closeButton;
    private GuiButton editHudButton;
    private int scrollOffset = 0;
    private String searchQuery = "";

    @Override
    public void initGui() {
        // Кнопка закрытия
        closeButton = new GuiButton(998, this.width - PADDING - 25, PADDING + 10, 20, 20, "✕");
        this.buttonList.add(closeButton);

        // Кнопка "Edit HUD"
        editHudButton = new GuiButton(999, this.width - PADDING - 120, PADDING + 10, 90, 20, "Edit HUD");
        this.buttonList.add(editHudButton);

        // Поле поиска
        searchField = new GuiTextField(997, this.fontRendererObj, PADDING + 250, PADDING + 15, 180, 18);
        searchField.setMaxStringLength(50);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setTextColor(0xFFFFFFFF);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Полупрозрачный фон
        drawRect(0, 0, this.width, this.height, 0x88000000);

        // Рисуем основное окно
        drawMainWindow(mouseX, mouseY);
    }

    private void drawMainWindow(int mouseX, int mouseY) {
        int mainX = PADDING;
        int mainY = PADDING;
        int mainWidth = this.width - PADDING * 2;
        int mainHeight = this.height - PADDING * 2;

        // Основной фон с закругленными углами
        drawRoundedRect(mainX, mainY, mainX + mainWidth, mainY + mainHeight, ROUNDED_RADIUS, 0xFF1a1a1a);
        drawRoundedBorder(mainX, mainY, mainX + mainWidth, mainY + mainHeight, ROUNDED_RADIUS, 2, 0xFF2a2a2a);

        // Заголовок слева
        drawHeader(mainX, mainY);

        // Search box с иконкой
        drawSearchBox(mainX, mainY);

        // Рисуем кнопки Edit HUD и Close
        closeButton.drawButton(this.mc, mouseX, mouseY);
        editHudButton.drawButton(this.mc, mouseX, mouseY);

        // Рисуем модули сеткой
        drawModulesGrid(mainX, mainY, mainWidth, mainHeight, mouseX, mouseY);

        // Сетка снизу (индикаторы страниц)
        drawPageIndicators(mainX, mainY, mainWidth, mainHeight);
    }

    private void drawHeader(int x, int y) {
        // Логотип
        drawRect(x + 15, y + 12, x + 30, y + 27, 0xFFFFFFFF);
        this.fontRendererObj.drawString("SC", x + 19, y + 14, 0xFF000000);

        // Текст
        this.fontRendererObj.drawStringWithShadow("SILENT CLIENT", x + 40, y + 14, 0xFFFFFFFF);
    }

    private void drawSearchBox(int x, int y) {
        int searchX = x + 250;
        int searchY = y + 12;

        // Фон
        drawRect(searchX, searchY, searchX + 185, searchY + 24, 0xFF0f0f0f);
        drawRect(searchX + 1, searchY + 1, searchX + 184, searchY + 23, 0xFF1a1a1a);

        // Иконка поиска
        this.fontRendererObj.drawString("🔍", searchX + 8, searchY + 6, 0xFF888888);

        // Поле ввода
        searchField.xPosition = searchX + 22;
        searchField.yPosition = searchY + 5;
        searchField.drawTextBox();
    }

    private void drawModulesGrid(int mainX, int mainY, int mainWidth, int mainHeight, int mouseX, int mouseY) {
        int gridStartY = mainY + HEADER_HEIGHT + 10;
        int gridMaxHeight = mainHeight - HEADER_HEIGHT - 40;
        int gridWidth = mainWidth - 30;

        ModuleManager manager = ModuleManager.getInstance();
        List<Module> modules = manager.getModules();

        // Фильтруем по поиску
        if (!searchQuery.isEmpty()) {
            modules = modules.stream()
                    .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int yOffset = gridStartY - scrollOffset;
        int moduleCount = 0;

        for (Module module : modules) {
            int col = moduleCount % GRID_COLS;
            int row = moduleCount / GRID_COLS;

            int boxX = mainX + 15 + (col * (MODULE_BOX_SIZE + MODULE_BOX_SPACING));
            int boxY = yOffset + (row * (MODULE_BOX_SIZE + MODULE_BOX_SPACING));

            // Проверяем видимость
            if (boxY + MODULE_BOX_SIZE < gridStartY || boxY > gridStartY + gridMaxHeight) {
                moduleCount++;
                continue;
            }

            // Рисуем модуль
            drawModuleBox(boxX, boxY, module, mouseX, mouseY);
            moduleCount++;
        }
    }

    private void drawModuleBox(int x, int y, Module module, int mouseX, int mouseY) {
        boolean isHovered = mouseX >= x && mouseX < x + MODULE_BOX_SIZE &&
                           mouseY >= y && mouseY < y + MODULE_BOX_SIZE;

        // Фон с закругленными углами
        int bgColor = isHovered ? 0xFF2a2a2a : 0xFF1a1a1a;
        drawRoundedRect(x, y, x + MODULE_BOX_SIZE, y + MODULE_BOX_SIZE, 5, bgColor);
        drawRoundedBorder(x, y, x + MODULE_BOX_SIZE, y + MODULE_BOX_SIZE, 5, 1, 0xFF2a2a2a);

        // Иконка/символ модуля (простой символ)
        String icon = getModuleIcon(module.getName());
        this.fontRendererObj.drawStringWithShadow(icon, x + MODULE_BOX_SIZE / 2 - 8, y + 15, 0xFFFFFFFF);

        // Название
        String name = module.getName();
        int nameWidth = this.fontRendererObj.getStringWidth(name);
        this.fontRendererObj.drawStringWithShadow(name, x + (MODULE_BOX_SIZE - nameWidth) / 2, y + 40, 0xFFFFFFFF);

        // Toggle switch
        boolean enabled = module.isEnabled();
        int toggleX = x + (MODULE_BOX_SIZE - 30) / 2;
        int toggleY = y + 60;
        drawToggleSwitch(toggleX, toggleY, enabled);
    }

    private void drawToggleSwitch(int x, int y, boolean enabled) {
        // Фон
        drawRect(x, y, x + 30, y + 12, 0xFF2a2a2a);
        drawRect(x + 1, y + 1, x + 29, y + 11, enabled ? 0xFF00AA00 : 0xFF555555);

        // Круг
        int circleX = enabled ? x + 17 : x + 2;
        drawRect(circleX, y + 2, circleX + 8, y + 10, 0xFFFFFFFF);
    }

    private void drawPageIndicators(int mainX, int mainY, int mainWidth, int mainHeight) {
        int indicatorY = mainY + mainHeight - 20;
        int indicatorSpacing = 8;
        int indicatorSize = 6;

        for (int i = 0; i < 8; i++) {
            int boxX = mainX + mainWidth / 2 - (8 * (indicatorSize + indicatorSpacing)) / 2 + (i * (indicatorSize + indicatorSpacing));
            drawRect(boxX, indicatorY, boxX + indicatorSize, indicatorY + indicatorSize, 0xFF2a2a2a);
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
        drawRect(x1 + radius, y1, x2 - radius, y1 + thickness, color);
        drawRect(x1 + radius, y2 - thickness, x2 - radius, y2, color);
        drawRect(x1, y1 + radius, x1 + thickness, y2 - radius, color);
        drawRect(x2 - thickness, y1 + radius, x2, y2 - radius, color);
    }

    private String getModuleIcon(String moduleName) {
        switch (moduleName.toLowerCase()) {
            case "animations": return "💫";
            case "armor status": return "🛡";
            case "auto gg": return "G";
            case "auto text": return "T";
            case "auto tip": return "$";
            case "block overlay": return "⬜";
            case "blockinfo": return "ℹ";
            case "boss bar": return "🗡";
            case "chat": return "💬";
            case "chunk borders": return "⬢";
            case "clear glass": return "🔍";
            case "clock": return "🕐";
            case "fps": return "fps";
            case "keystrokes": return "⌨";
            case "ping": return "📡";
            default: return "■";
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 998) {
            // Close
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (button.id == 999) {
            // Edit HUD
            this.mc.displayGuiScreen(new EditHudScreen(this));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

        // Обработка клика по модулям
        int mainX = PADDING;
        int mainY = PADDING;
        int mainWidth = this.width - PADDING * 2;
        int mainHeight = this.height - PADDING * 2;

        int gridStartY = mainY + HEADER_HEIGHT + 10;
        int gridMaxHeight = mainHeight - HEADER_HEIGHT - 40;

        ModuleManager manager = ModuleManager.getInstance();
        List<Module> modules = manager.getModules();

        if (!searchQuery.isEmpty()) {
            modules = modules.stream()
                    .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int yOffset = gridStartY - scrollOffset;
        int moduleCount = 0;

        for (Module module : modules) {
            int col = moduleCount % GRID_COLS;
            int row = moduleCount / GRID_COLS;

            int boxX = mainX + 15 + (col * (MODULE_BOX_SIZE + MODULE_BOX_SPACING));
            int boxY = yOffset + (row * (MODULE_BOX_SIZE + MODULE_BOX_SPACING));

            if (boxY + MODULE_BOX_SIZE < gridStartY || boxY > gridStartY + gridMaxHeight) {
                moduleCount++;
                continue;
            }

            if (mouseX >= boxX && mouseX < boxX + MODULE_BOX_SIZE &&
                    mouseY >= boxY && mouseY < boxY + MODULE_BOX_SIZE) {
                if (mouseButton == 0) {
                    module.toggle();
                }
            }

            moduleCount++;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dwheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (dwheel != 0) {
            scrollOffset += dwheel > 0 ? -15 : 15;
            scrollOffset = Math.max(0, scrollOffset);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (searchField.textboxKeyTyped(typedChar, keyCode)) {
            searchQuery = searchField.getText();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

