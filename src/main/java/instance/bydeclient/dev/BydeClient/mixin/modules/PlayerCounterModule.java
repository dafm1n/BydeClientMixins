package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.Collection;

/**
 * Мод Player Counter - показывает количество игроков на сервере
 */
public class PlayerCounterModule extends Module {
    private int playerCount = 0;
    private int posX = 10;
    private int posY = 150;

    public PlayerCounterModule() {
        super("Player Counter", "Показывает количество игроков онлайн на сервере");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        updatePlayerCount();
    }

    private void updatePlayerCount() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.getNetHandler() == null) {
            playerCount = 0;
            return;
        }

        // Получаем список всех игроков на сервере из таба
        Collection<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();
        playerCount = players != null ? players.size() : 0;
    }

    /**
     * Отрисовать счетчик игроков
     */
    public void renderPlayerCounter() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Формируем текст
        String playerText = "Players: " + playerCount;

        // Определяем цвет
        int color = getPlayerCountColor(playerCount);

        // Рисуем с тенью
        mc.fontRendererObj.drawStringWithShadow(playerText, posX, posY, color);
    }

    /**
     * Получить цвет в зависимости от количества игроков
     */
    private int getPlayerCountColor(int count) {
        if (count == 0) {
            return 0xFFFF0000; // Красный - никого нет
        } else if (count <= 5) {
            return 0xFFFFFF00; // Жёлтый - мало
        } else if (count <= 20) {
            return 0xFF00FF00; // Зелёный - нормально
        } else {
            return 0xFF00FFFF; // Голубой - много
        }
    }

    public int getPlayerCount() {
        return playerCount;
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
        return "Players Online: " + playerCount;
    }
}

