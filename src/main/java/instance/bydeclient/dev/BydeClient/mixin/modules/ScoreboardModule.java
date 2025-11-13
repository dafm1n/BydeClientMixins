package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Scoreboard;

/**
 * Мод Scoreboard - включает/выключает отображение скорборда
 */
public class ScoreboardModule extends Module {
    private boolean scoreboardVisible = true;

    public ScoreboardModule() {
        super("Scoreboard", "Включает/выключает отображение скорборда на экране");
    }

    @Override
    public void onEnable() {
        scoreboardVisible = true;
    }

    @Override
    public void onDisable() {
        scoreboardVisible = false;
    }

    @Override
    public void onUpdate() {
    }

    /**
     * Проверить, должен ли скорборд быть видим
     */
    public boolean isScoreboardVisible() {
        return scoreboardVisible && isEnabled();
    }

    /**
     * Установить видимость скорборда
     */
    public void setScoreboardVisible(boolean visible) {
        this.scoreboardVisible = visible;
    }

    /**
     * Получить текущий скорборд
     */
    public Scoreboard getCurrentScoreboard() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return null;
        return mc.theWorld.getScoreboard();
    }

    /**
     * Проверить наличие скорборда на сервере
     */
    public boolean hasScoreboard() {
        Scoreboard scoreboard = getCurrentScoreboard();
        return scoreboard != null && scoreboard.getObjectiveInDisplaySlot(1) != null;
    }

    public String getScoreboardStatus() {
        if (!isEnabled()) {
            return "Скорборд: ВЫКЛЮЧЕН";
        }
        if (hasScoreboard()) {
            return "Скорборд: ВИДИМ";
        }
        return "Скорборд: НЕ НАЙДЕН";
    }
}

