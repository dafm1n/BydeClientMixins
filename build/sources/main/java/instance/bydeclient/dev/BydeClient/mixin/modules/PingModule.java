package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * Мод Ping - показывает пинг в табе и на экране
 */
public class PingModule extends Module {
    private int posX = 10;
    private int posY = 100;
    private int currentPing = 0;

    public PingModule() {
        super("Ping", "Показывает пинг с цветовой индикацией");
    }

    @Override
    public void onEnable() {
        System.out.println("[BydeClient] Ping мод включен");
    }

    @Override
    public void onDisable() {
        System.out.println("[BydeClient] Ping мод отключен");
    }

    @Override
    public void onUpdate() {
        updatePing();
    }

    private void updatePing() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Получаем информацию о сетевом игроке
        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        if (playerInfo != null) {
            currentPing = playerInfo.getResponseTime();
        }
    }

    public void renderPing() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Определяем цвет по пингу
        int pingColor = getPingColor(currentPing);

        // Формируем текст
        String pingText = String.format("Ping: %dms", currentPing);

        // Рисуем текст с тенью
        mc.fontRendererObj.drawStringWithShadow(pingText, posX, posY, pingColor);
    }

    private int getPingColor(int ping) {
        if (ping < 50) {
            return 0xFF00FF00; // Зелёный - отличный пинг
        } else if (ping < 100) {
            return 0xFFFFFF00; // Жёлтый - хороший пинг
        } else if (ping < 150) {
            return 0xFFFF8800; // Оранжевый - нормальный пинг
        } else if (ping < 200) {
            return 0xFFFF4400; // Красно-оранжевый - плохой пинг
        } else {
            return 0xFFFF0000; // Красный - очень плохой пинг
        }
    }

    public int getCurrentPing() {
        return currentPing;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
