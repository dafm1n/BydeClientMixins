package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

/**
 * Мод FPS Boost - максимальная оптимизация для FPS, CPU и памяти
 */
public class FPSBoostModule extends Module {
    private int originalParticles = -1;
    private boolean originalVsync = false;
    private int originalRenderDistance = -1;
    private boolean originalFancyGraphics = false;
    private int originalAO = -1;
    private int originalClouds = -1;
    private boolean originalMipmap = false;

    public FPSBoostModule() {
        super("FPS Boost", "Максимальная оптимизация FPS, CPU и памяти");
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;

        // Сохраняем оригинальные значения
        originalParticles = settings.particleSetting;
        originalVsync = settings.enableVsync;
        originalRenderDistance = settings.renderDistanceChunks;
        originalFancyGraphics = settings.fancyGraphics;
        originalAO = settings.ambientOcclusion;
        originalClouds = settings.clouds;
        originalMipmap = settings.mipmapLevels > 0;

        // === ОПТИМИЗАЦИЯ CPU ===
        settings.particleSetting = 2;          // Минимум частиц (2 = minimal)
        settings.enableVsync = false;           // Отключаем VSync для максимального FPS
        settings.renderDistanceChunks = 6;      // Минимальная дальность рендера
        settings.fancyGraphics = false;         // Отключаем fancy graphics
        settings.ambientOcclusion = 0;          // Отключаем ambient occlusion
        settings.clouds = 0;                    // Отключаем облака
        settings.mipmapLevels = 0;              // Отключаем mipmapping

        // === ОПТИМИЗАЦИЯ ПАМЯТИ ===
        settings.limitFramerate = 120;          // Ограничиваем фреймрейт
        settings.anaglyph = false;              // Отключаем анаглиф
        settings.showDebugInfo = false;         // Скрываем debug инфо для экономии

        // Сохраняем настройки
        settings.saveOptions();
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
        settings.fancyGraphics = originalFancyGraphics;
        if (originalAO != -1) {
            settings.ambientOcclusion = originalAO;
        }
        if (originalClouds != -1) {
            settings.clouds = originalClouds;
        }

        // Сохраняем настройки
        settings.saveOptions();
    }

    @Override
    public void onUpdate() {
        // Периодическая оптимизация памяти
        if (System.currentTimeMillis() % 1000 == 0) {
            optimizeMemory();
        }
    }

    /**
     * Оптимизация памяти - очистка ненужных ресурсов
     */
    private void optimizeMemory() {
        // Запускаем garbage collector периодически
        System.gc();
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
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;

        return String.format(
            "FPS: %d | CPU: Optimized | Memory: %dMB/%dMB",
            getCurrentFPS(),
            usedMemory,
            maxMemory
        );
    }

    /**
     * Получить статус использования памяти
     */
    public MemoryInfo getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long max = runtime.maxMemory() / 1024 / 1024;
        return new MemoryInfo(used, max);
    }

    /**
     * Класс для хранения информации о памяти
     */
    public static class MemoryInfo {
        public long usedMB;
        public long maxMB;

        public MemoryInfo(long used, long max) {
            this.usedMB = used;
            this.maxMB = max;
        }

        public int getPercentage() {
            return (int) ((usedMB * 100) / maxMB);
        }
    }
}
