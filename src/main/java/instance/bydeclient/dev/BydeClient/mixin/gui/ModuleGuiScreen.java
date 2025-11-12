package instance.bydeclient.dev.BydeClient.mixin.gui;

import instance.bydeclient.dev.BydeClient.mixin.modules.KeystrokesModule;
import instance.bydeclient.dev.BydeClient.mixin.modules.Module;
import instance.bydeclient.dev.BydeClient.mixin.modules.ModuleManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleGuiScreen extends GuiScreen {
    private static final int PANEL_WIDTH = 300;
    private static final int TAB_HEIGHT = 32;
    private static final int PADDING = 12;
    private static final int SPACING = 2;
    private static final int MODULE_ITEM_HEIGHT = 45;

    private List<ModuleButton> moduleButtons = new ArrayList<>();
    private GuiTextField searchField;
    private GuiButton closeButton;
    private int selectedTab = 0;
    private int scrollOffset = 0;
    private String searchQuery = "";
    private int panelX;
    private int panelY;

    @Override
    public void initGui() {
        moduleButtons.clear();

        // Позиция панели (слева сверху)
        panelX = 10;
        panelY = 10;

        // Кнопка закрытия
        closeButton = new GuiButton(
                998,
                panelX + PANEL_WIDTH - 32,
                panelY + 8,
                20,
                20,
                "✕"
        );
        this.buttonList.add(closeButton);

        // Поле поиска
        searchField = new GuiTextField(997, this.fontRendererObj, panelX + PADDING, panelY + 60, PANEL_WIDTH - PADDING * 2, 25);
        searchField.setMaxStringLength(50);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setTextColor(0xFFFFFF);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Полупрозрачный фон всего экрана
        drawRect(0, 0, this.width, this.height, 0x88000000);

        // Рисуем основную панель
        drawPanel();

        // Рисуем заголовок
        drawHeader();

        // Рисуем вкладки
        drawTabs(mouseX, mouseY);

        // Рисуем поле поиска
        drawSearchField();

        // Рисуем список модулей
        drawModulesList(mouseX, mouseY);

        // Рисуем кнопки
        closeButton.drawButton(this.mc, mouseX, mouseY);
    }

    private void drawPanel() {
        int panelHeight = 500;

        // Основной фон панели
        drawRect(panelX, panelY, panelX + PANEL_WIDTH, panelY + panelHeight, 0xFF0f0f0f);

        // Верхняя граница (светлая)
        drawRect(panelX, panelY, panelX + PANEL_WIDTH, panelY + 1, 0xFF2a2a2a);
    }

    private void drawHeader() {
        // Логотип SC
        drawRect(panelX + PADDING, panelY + 8, panelX + PADDING + 18, panelY + 26, 0xFFFFFFFF);
        this.fontRendererObj.drawString(
                "SC",
                panelX + PADDING + 4,
                panelY + 10,
                0xFF000000
        );

        // Текст SILENT CLIENT
        this.fontRendererObj.drawStringWithShadow(
                "SILENT CLIENT",
                panelX + PADDING + 25,
                panelY + 10,
                0xFFFFFFFF
        );
    }

    private void drawTabs(int mouseX, int mouseY) {
        String[] tabs = {"Mods", "Settings", "Configs"};
        int tabWidth = (PANEL_WIDTH - PADDING * 2) / 3;
        int tabY = panelY + 35;

        for (int i = 0; i < tabs.length; i++) {
            int tabX = panelX + PADDING + (i * (tabWidth - 2));
            boolean isSelected = selectedTab == i;
            boolean isHovered = mouseX >= tabX && mouseX < tabX + tabWidth &&
                    mouseY >= tabY && mouseY < tabY + TAB_HEIGHT;

            // Фон вкладки
            int bgColor = isSelected ? 0xFF1a1a1a : 0xFF0f0f0f;
            drawRect(tabX, tabY, tabX + tabWidth, tabY + TAB_HEIGHT, bgColor);

            // Граница вкладки
            drawRect(tabX, tabY, tabX + tabWidth, tabY + 1, 0xFF2a2a2a);

            // Нижняя граница активной вкладки
            if (isSelected) {
                drawRect(tabX, tabY + TAB_HEIGHT - 2, tabX + tabWidth, tabY + TAB_HEIGHT, 0xFFFFFFFF);
            }

            // Текст
            int textColor = isSelected ? 0xFFFFFFFF : 0xFF888888;
            this.fontRendererObj.drawStringWithShadow(
                    tabs[i],
                    tabX + (tabWidth - this.fontRendererObj.getStringWidth(tabs[i])) / 2,
                    tabY + 10,
                    textColor
            );

            // Обработка клика по вкладке
            if (isHovered && mouseX >= tabX && mouseX < tabX + tabWidth) {
                // Клик будет обработан в mouseClicked
            }
        }
    }

    private void drawSearchField() {
        int searchY = panelY + 67;

        // Фон поля поиска
        drawRect(panelX + PADDING, searchY, panelX + PANEL_WIDTH - PADDING, searchY + 25, 0xFF1a1a1a);

        // Граница поля
        drawRect(panelX + PADDING, searchY, panelX + PANEL_WIDTH - PADDING, searchY + 1, 0xFF2a2a2a);

        // Поле ввода
        searchField.xPosition = panelX + PADDING + 5;
        searchField.yPosition = searchY + 5;
        searchField.drawTextBox();
    }

    private void drawModulesList(int mouseX, int mouseY) {
        int startY = panelY + 110;
        int maxHeight = 380;

        ModuleManager manager = ModuleManager.getInstance();
        List<Module> modules = manager.getModules();

        // Фильтруем по поиску
        if (!searchQuery.isEmpty()) {
            modules = modules.stream()
                    .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int yOffset = startY - scrollOffset;

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int buttonY = yOffset + (i * (MODULE_ITEM_HEIGHT + SPACING));

            if (buttonY + MODULE_ITEM_HEIGHT < startY || buttonY > startY + maxHeight) {
                continue;
            }

            // Рисуем элемент модуля
            drawModuleItem(this.mc, mouseX, mouseY, panelX + PADDING, buttonY,
                    PANEL_WIDTH - PADDING * 2, MODULE_ITEM_HEIGHT, module);
        }

        // Граница снизу
        drawRect(panelX, panelY + panelY + 490, panelX + PANEL_WIDTH, panelY + 495, 0xFF2a2a2a);
    }

    private void drawModuleItem(net.minecraft.client.Minecraft mc, int mouseX, int mouseY,
                                int x, int y, int width, int height, Module module) {
        boolean isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

        // Фон элемента
        int bgColor = isHovered ? 0xFF1a1a1a : 0xFF0f0f0f;
        drawRect(x, y, x + width, y + height, bgColor);

        // Граница элемента
        drawRect(x, y, x + width, y + 1, 0xFF1a1a1a);
        drawRect(x, y + height - 1, x + width, y + height, 0xFF1a1a1a);

        // Название модуля
        mc.fontRendererObj.drawStringWithShadow(
                module.getName(),
                x + 12,
                y + 8,
                0xFFFFFFFF
        );

        // Описание модуля
        String desc = module.getDescription();
        if (desc != null && !desc.isEmpty()) {
            mc.fontRendererObj.drawString(
                    desc,
                    x + 12,
                    y + 20,
                    0xFF888888
            );
        }

        // Тогл (переключатель)
        drawToggle(x + width - 40, y + 12, module.isEnabled());
    }

    private void drawToggle(int x, int y, boolean enabled) {
        // Фон тогла
        drawRect(x, y, x + 35, y + 18, 0xFF2a2a2a);
        drawRect(x + 1, y + 1, x + 34, y + 17, enabled ? 0xFF00AA00 : 0xFF555555);

        // Круглая часть
        int circleX = enabled ? x + 19 : x + 3;
        drawRect(circleX, y + 2, circleX + 14, y + 16, 0xFFFFFFFF);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 998) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

        // Проверка клика по вкладкам
        String[] tabs = {"Mods", "Settings", "Configs"};
        int tabWidth = (PANEL_WIDTH - PADDING * 2) / 3;
        int tabY = panelY + 35;

        for (int i = 0; i < tabs.length; i++) {
            int tabX = panelX + PADDING + (i * (tabWidth - 2));
            if (mouseX >= tabX && mouseX < tabX + tabWidth && mouseY >= tabY && mouseY < tabY + TAB_HEIGHT) {
                selectedTab = i;
                return;
            }
        }

        // Проверка клика по модулям
        int startY = panelY + 110;
        int maxHeight = 380;
        ModuleManager manager = ModuleManager.getInstance();
        List<Module> modules = manager.getModules();

        if (!searchQuery.isEmpty()) {
            modules = modules.stream()
                    .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int yOffset = startY - scrollOffset;

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int buttonY = yOffset + (i * (MODULE_ITEM_HEIGHT + SPACING));
            int itemX = panelX + PADDING;
            int itemWidth = PANEL_WIDTH - PADDING * 2;

            if (buttonY + MODULE_ITEM_HEIGHT < startY || buttonY > startY + maxHeight) {
                continue;
            }

            if (mouseX >= itemX && mouseX < itemX + itemWidth &&
                    mouseY >= buttonY && mouseY < buttonY + MODULE_ITEM_HEIGHT) {
                if (mouseButton == 0) {
                    module.toggle();
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dwheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (dwheel != 0) {
            scrollOffset += dwheel > 0 ? -10 : 10;
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

    private static class ModuleButton extends GuiButton {
        private Module module;

        public ModuleButton(int buttonId, int x, int y, int width, int height, Module module) {
            super(buttonId, x, y, width, height, "");
            this.module = module;
        }

        @Override
        public void drawButton(net.minecraft.client.Minecraft mc, int mouseX, int mouseY) {
            // Не используется, рисуем через drawModuleItem
        }

        public void toggleModule() {
            module.toggle();
        }
    }
}