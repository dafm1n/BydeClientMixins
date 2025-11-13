package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;

/**
 * Мод Particles - контролирует отображение частиц
 */
public class ParticlesModule extends Module {
    private int particleCount = 0;
    private boolean reduceParticles = false;

    public ParticlesModule() {
        super("Particles", "Контролирует отображение частиц для оптимизации");
    }

    @Override
    public void onEnable() {
        reduceParticles = true;
    }

    @Override
    public void onDisable() {
        reduceParticles = false;
    }

    @Override
    public void onUpdate() {
        updateParticles();
    }

    private void updateParticles() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.effectRenderer == null) return;

        // Подсчитываем количество активных частиц
        // На версии 1.8.9 счетчик частиц недоступен напрямую
        particleCount = 0;

        // Если включено уменьшение - понижаем качество частиц
        if (reduceParticles) {
            // Ограничение частиц
        }
    }

    private void limitParticles(EffectRenderer effectRenderer) {
        // Метод для ограничения частиц
        // На версии 1.8.9 это внутренний механизм
    }

    public int getParticleCount() {
        return particleCount;
    }

    public boolean isReduceParticles() {
        return reduceParticles;
    }

    public void setReduceParticles(boolean reduce) {
        this.reduceParticles = reduce;
    }

    public String getParticleInfo() {
        return String.format("Частицы: %d", particleCount);
    }
}

