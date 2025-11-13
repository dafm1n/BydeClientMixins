package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;

/**
 * Мод SwordAnimation - улучшенная анимация размахивания мечом на 1.7.10
 */
public class SwordAnimationModule extends Module {
    private float swingProgress = 0.0f;
    private int swingProgressInt = 0;
    private boolean isSwinging = false;
    private float swingSpeed = 1.5f; // Скорость анимации
    private boolean smoothAnimation = true;

    public SwordAnimationModule() {
        super("Sword Animation", "Улучшенная анимация меча с плавными движениями");
    }

    @Override
    public void onEnable() {
        swingProgress = 0.0f;
        swingProgressInt = 0;
        isSwinging = false;
    }

    @Override
    public void onDisable() {
        resetAnimation();
    }

    @Override
    public void onUpdate() {
        updateAnimation();
    }

    private void updateAnimation() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.thePlayer.getHeldItem() == null) {
            resetAnimation();
            return;
        }

        // Проверяем, держит ли игрок меч
        if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            resetAnimation();
            return;
        }

        // Обновляем прогресс анимации
        if (isSwinging) {
            swingProgress += 0.09f * swingSpeed;
            if (swingProgress > 1.0f) {
                swingProgress = 0.0f;
                isSwinging = false;
            }
            swingProgressInt = (int)(swingProgress * 10);
        }
    }

    /**
     * Вызывается при атаке
     */
    public void onAttack() {
        if (isEnabled()) {
            isSwinging = true;
            swingProgress = 0.0f;
        }
    }

    private void resetAnimation() {
        swingProgress = 0.0f;
        swingProgressInt = 0;
        isSwinging = false;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public int getSwingProgressInt() {
        return swingProgressInt;
    }

    public boolean isSwinging() {
        return isSwinging;
    }

    public float getSwingSpeed() {
        return swingSpeed;
    }

    public void setSwingSpeed(float speed) {
        this.swingSpeed = speed;
    }

    public boolean isSmoothAnimation() {
        return smoothAnimation;
    }

    public void setSmoothAnimation(boolean smooth) {
        this.smoothAnimation = smooth;
    }
}

