package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;

/**
 * Мод ItemPhysics - добавляет физику к предметам
 */
public class ItemPhysicsModule extends Module {
    private float rotationSpeed = 5.0f;
    private float bounceHeight = 0.5f;

    public ItemPhysicsModule() {
        super("ItemPhysics", "Добавляет физику к предметам");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        // Применяем физику к предметам
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityItem) {
                EntityItem item = (EntityItem) obj;
                applyPhysics(item);
            }
        }
    }

    private void applyPhysics(EntityItem item) {
        // Добавляем вращение
        item.rotationYaw += rotationSpeed;
        if (item.rotationYaw > 360) {
            item.rotationYaw -= 360;
        }

        // Добавляем небольшое подпрыгивание
        if (item.onGround) {
            item.motionY = bounceHeight * 0.1f;
        }
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = Math.max(0.1f, Math.min(20.0f, speed));
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setBounceHeight(float height) {
        this.bounceHeight = Math.max(0.1f, Math.min(2.0f, height));
    }

    public float getBounceHeight() {
        return bounceHeight;
    }
}
