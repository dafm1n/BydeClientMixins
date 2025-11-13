package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;

/**
 * Мод FPS - показывает текущий FPS на экране
 */
public class FPSModule extends Module {
    private int posX = 10;
    private int posY = 10;
    private int currentFPS = 0;
    private long lastUpdateTime = 0;

    public FPSModule() {
        super("FPS", "Показывает текущий FPS на экране");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        updateFPS();
    }

    private void updateFPS() {
        currentFPS = Minecraft.getDebugFPS();
    }

    /**
     * Отрисовать FPS счетчик
     */
    public void renderFPS() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Определяем цвет по FPS
        int fpsColor = getFPSColor(currentFPS);

        // Формируем текст
        String fpsText = "FPS: " + currentFPS;

        // Рисуем с тенью
        mc.fontRendererObj.drawStringWithShadow(fpsText, posX, posY, fpsColor);
    }

    /**
     * Получить цвет в зависимости от FPS
     */
    private int getFPSColor(int fps) {
        if (fps >= 60) {
            return 0xFF00FF00; // Зелёный - отличный FPS
        } else if (fps >= 40) {
            return 0xFFFFFF00; // Жёлтый - хороший FPS
        } else if (fps >= 25) {
            return 0xFFFF8800; // Оранжевый - нормальный FPS
        } else if (fps >= 15) {
            return 0xFFFF4400; // Красно-оранжевый - плохой FPS
        } else {
            return 0xFFFF0000; // Красный - очень плохой FPS
        }
    }

    public int getCurrentFPS() {
        return currentFPS;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int x) {
        this.posX = x;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int y) {
        this.posY = y;
    }

    public String getInfo() {
        return "FPS: " + currentFPS;
    }
}

