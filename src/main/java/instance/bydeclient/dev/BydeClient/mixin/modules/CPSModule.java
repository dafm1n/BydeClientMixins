package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

/**
 * Мод CPS - показывает количество кликов в секунду
 */
public class CPSModule extends Module {
    private int posX = 10;
    private int posY = 10;
    private int cps = 0;
    private int clicks = 0;
    private long lastResetTime = System.currentTimeMillis();
    private boolean lastMouseButtonState = false;

    public CPSModule() {
        super("CPS", "Показывает количество кликов в секунду");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        updateCPS();
    }

    private void updateCPS() {
        long currentTime = System.currentTimeMillis();

        // Сбрасываем счетчик каждую секунду
        if (currentTime - lastResetTime >= 1000) {
            cps = clicks;
            clicks = 0;
            lastResetTime = currentTime;
        }

        // Отслеживаем левую кнопку мыши
        boolean mouseButtonDown = Mouse.isButtonDown(0);
        if (mouseButtonDown && !lastMouseButtonState) {
            clicks++;
        }
        lastMouseButtonState = mouseButtonDown;
    }

    /**
     * Отрисовать CPS счетчик
     */
    public void renderCPS() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        String cpsText = "CPS: " + cps;
        mc.fontRendererObj.drawStringWithShadow(cpsText, posX, posY, 0xFFFFFFFF);
    }

    public int getCPS() {
        return cps;
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
        return "CPS: " + cps;
    }
}

