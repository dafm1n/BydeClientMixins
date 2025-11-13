package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/**
 * Мод Motion Blur - добавляет эффект размытия движения
 */
public class MotionBlurModule extends Module {
    private float blurAmount = 0.5f;
    private boolean enabled_blur = true;

    public MotionBlurModule() {
        super("Motion Blur", "Добавляет эффект размытия при быстром движении");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
    }

    /**
     * Применить Motion Blur эффект
     */
    public void applyMotionBlur(float partialTicks) {
        if (!isEnabled() || !enabled_blur) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Вычисляем скорость движения игрока
        double playerMotion = Math.sqrt(
            mc.thePlayer.motionX * mc.thePlayer.motionX +
            mc.thePlayer.motionY * mc.thePlayer.motionY +
            mc.thePlayer.motionZ * mc.thePlayer.motionZ
        );

        // Если скорость достаточна - применяем blur
        if (playerMotion > 0.1) {
            float blur = (float) Math.min(playerMotion * blurAmount, 1.0f);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, blur * 0.1f);

            // Рисуем полупрозрачный экран для эффекта размытия
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(mc.displayWidth, 0);
            GL11.glVertex2f(mc.displayWidth, mc.displayHeight);
            GL11.glVertex2f(0, mc.displayHeight);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public float getBlurAmount() {
        return blurAmount;
    }

    public void setBlurAmount(float amount) {
        this.blurAmount = Math.min(Math.max(amount, 0.1f), 1.0f);
    }

    public boolean isBlurEnabled() {
        return enabled_blur;
    }

    public void setBlurEnabled(boolean enabled) {
        this.enabled_blur = enabled;
    }
}

