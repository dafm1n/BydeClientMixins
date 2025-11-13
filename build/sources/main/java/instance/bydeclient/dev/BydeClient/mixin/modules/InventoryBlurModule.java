package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.opengl.GL11;

/**
 * Мод Inventory Blur - размывает фон при открытии инвентаря
 */
public class InventoryBlurModule extends Module {
    private float blurStrength = 0.5f;

    public InventoryBlurModule() {
        super("Inventory Blur", "Размывает фон инвентаря для лучшей видимости");
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
     * Применить размытие фона инвентаря
     */
    public void applyInventoryBlur() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiInventory)) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Применяем полупрозрачное размытие на фон
        GL11.glColor4f(0.0f, 0.0f, 0.0f, blurStrength);
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

    public float getBlurStrength() {
        return blurStrength;
    }

    public void setBlurStrength(float strength) {
        this.blurStrength = Math.min(Math.max(strength, 0.1f), 1.0f);
    }
}

