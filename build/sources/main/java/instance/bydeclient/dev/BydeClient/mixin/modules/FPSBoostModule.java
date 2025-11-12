package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

/**
 * Мод FPS Boost - оптимизирует настройки для повышения FPS
 */
public class FPSBoostModule extends Module {
    private int originalParticles = -1;
    private boolean originalVsync = false;
    private int originalRenderDistance = -1;

    public FPSBoostModule() {
        super("FPS Boost", "Оптимизирует настройки для повышения FPS");
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;

        // Сохраняем оригинальные значения
        originalParticles = settings.particleSetting;
        originalVsync = settings.enableVsync;
        originalRenderDistance = settings.renderDistanceChunks;

        // Применяем оптимизации
        settings.particleSetting = 2; // Минимум частиц (2 = minimal)
        settings.enableVsync = false; // Отключаем VSync
        settings.renderDistanceChunks = 8; // Уменьшаем дальность рендера
        settings.fancyGraphics = false; // Отключаем fancy graphics
        settings.ambientOcclusion = 0; // Отключаем ambient occlusion
        settings.clouds = 0; // Отключаем облака

        // Сохраняем настройки
        settings.saveOptions();

        System.out.println("[BydeClient] FPS Boost мод включен - оптимизации применены");
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;

        // Восстанавливаем оригинальные значения
        if (originalParticles != -1) {
            settings.particleSetting = originalParticles;
        }
        settings.enableVsync = originalVsync;
        if (originalRenderDistance != -1) {
            settings.renderDistanceChunks = originalRenderDistance;
        }

        // Сохраняем настройки
        settings.saveOptions();

        System.out.println("[BydeClient] FPS Boost мод отключен - оригинальные настройки восстановлены");
    }

    @Override
    public void onUpdate() {
        // Нет необходимости в обновлении каждый тик
    }

    /**
     * Получить текущий FPS
     */
    public int getCurrentFPS() {
        return Minecraft.getDebugFPS();
    }

    /**
     * Получить информацию о текущих оптимизациях
     */
    public String getOptimizationInfo() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;

        return String.format(
            "FPS: %d | Particles: %d | Render Distance: %d | VSync: %s",
            getCurrentFPS(),
            settings.particleSetting,
            settings.renderDistanceChunks,
            settings.enableVsync ? "ON" : "OFF"
        );
    }
}
