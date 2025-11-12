package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

/**
 * Мод BlockOverlay - подсвечивает блок, на который смотрит игрок
 */
public class BlockOverlayModule extends Module {
    private float overlayAlpha = 0.3f;

    public BlockOverlayModule() {
        super("BlockOverlay", "Подсвечивает блок под курсором");
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

    public void renderBlockOverlay() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.objectMouseOver == null) return;

        MovingObjectPosition mop = mc.objectMouseOver;

        // Проверяем, смотрим ли на блок
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Цвет подсветки (красный)
            GL11.glColor4f(1.0f, 0.0f, 0.0f, overlayAlpha);

            // Получаем координаты блока правильно
            int x = (int) Math.floor(mop.hitVec.xCoord);
            int y = (int) Math.floor(mop.hitVec.yCoord);
            int z = (int) Math.floor(mop.hitVec.zCoord);

            // Рисуем куб вокруг блока
            drawBlockCube(x, y, z);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    private void drawBlockCube(int x, int y, int z) {
        GL11.glBegin(GL11.GL_QUADS);

        // Передняя грань
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x + 1, y + 1, z);
        GL11.glVertex3d(x, y + 1, z);

        // Задняя грань
        GL11.glVertex3d(x, y, z + 1);
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y, z + 1);

        // Левая грань
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x, y, z + 1);

        // Правая грань
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x + 1, y, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z);

        // Верхняя грань
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x + 1, y + 1, z);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x, y + 1, z + 1);

        // Нижняя грань
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z + 1);
        GL11.glVertex3d(x + 1, y, z + 1);
        GL11.glVertex3d(x + 1, y, z);

        GL11.glEnd();
    }

    public void setOverlayAlpha(float alpha) {
        this.overlayAlpha = Math.max(0.1f, Math.min(1.0f, alpha));
    }

    public float getOverlayAlpha() {
        return overlayAlpha;
    }
}
