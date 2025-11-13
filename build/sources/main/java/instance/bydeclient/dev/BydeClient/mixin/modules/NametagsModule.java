package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

/**
 * Мод Nametags - показывает имена игроков через стены
 */
public class NametagsModule extends Module {
    private float maxDistance = 100.0f;

    public NametagsModule() {
        super("Nametags", "Показывает имена игроков через стены");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        // Обновление происходит в рендере
    }

    public void renderNametags() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Получаем всех игроков в мире
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) obj;

                // Не показываем свой ник
                if (player == mc.thePlayer) continue;

                // Проверяем расстояние
                double distance = mc.thePlayer.getDistanceToEntity(player);
                if (distance > maxDistance) continue;

                // Получаем здоровье
                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();
                int healthPercent = (int) ((health / maxHealth) * 100);

                // Определяем цвет по здоровью
                int healthColor = getHealthColor(healthPercent);

                // Рисуем ник и здоровье
                renderPlayerNametag(player, healthColor, healthPercent);
            }
        }
    }

    private void renderPlayerNametag(EntityPlayer player, int healthColor, int healthPercent) {
        Minecraft mc = Minecraft.getMinecraft();

        // Получаем позицию игрока
        Vec3 playerPos = new Vec3(player.posX, player.posY + player.height + 0.5, player.posZ);
        Vec3 cameraPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        // Вычисляем расстояние
        double distance = mc.thePlayer.getDistanceToEntity(player);

        // Формируем текст
        String nametag = String.format("%s [%d%%]", player.getName(), healthPercent);

        // Рисуем текст (в реальной реализации нужна трансформация координат)
        // Здесь упрощённая версия
    }

    private int getHealthColor(int healthPercent) {
        if (healthPercent > 75) {
            return 0xFF00FF00; // Зелёный
        } else if (healthPercent > 50) {
            return 0xFFFFFF00; // Жёлтый
        } else if (healthPercent > 25) {
            return 0xFFFF8800; // Оранжевый
        } else {
            return 0xFFFF0000; // Красный
        }
    }

    public void setMaxDistance(float distance) {
        this.maxDistance = Math.max(10.0f, distance);
    }

    public float getMaxDistance() {
        return maxDistance;
    }
}
