package instance.bydeclient.dev.BydeClient.mixin.modules;

import instance.bydeclient.dev.BydeClient.mixin.utils.PositionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Мод Keystrokes - показывает нажатые клавиши на экране с анимацией и эффектами
 */
public class KeystrokesModule extends Module {
    private static final int KEY_SIZE = 30;
    private static final int SPACING = 5;
    private boolean animationEnabled = true;

    // Анимация для каждой клавиши
    private float wScale = 1.0f;
    private float aScale = 1.0f;
    private float sScale = 1.0f;
    private float dScale = 1.0f;
    private float spaceScale = 1.0f;

    // Эффекты клика
    private float wClickEffect = 0.0f;
    private float aClickEffect = 0.0f;
    private float sClickEffect = 0.0f;
    private float dClickEffect = 0.0f;
    private float spaceClickEffect = 0.0f;

    // Отслеживание предыдущего состояния
    private boolean wPrevPressed = false;
    private boolean aPrevPressed = false;
    private boolean sPrevPressed = false;
    private boolean dPrevPressed = false;
    private boolean spacePrevPressed = false;

    // Позиция
    private int posX = 10;
    private int posY = 10;

    public KeystrokesModule() {
        super("Keystrokes", "Показывает нажатые клавиши");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        // Обновляем анимацию
        updateAnimation();
    }

    private void updateAnimation() {
        if (!animationEnabled) return;

        // Плавное уменьшение масштаба
        wScale = lerp(wScale, Keyboard.isKeyDown(Keyboard.KEY_W) ? 1.1f : 1.0f, 0.1f);
        aScale = lerp(aScale, Keyboard.isKeyDown(Keyboard.KEY_A) ? 1.1f : 1.0f, 0.1f);
        sScale = lerp(sScale, Keyboard.isKeyDown(Keyboard.KEY_S) ? 1.1f : 1.0f, 0.1f);
        dScale = lerp(dScale, Keyboard.isKeyDown(Keyboard.KEY_D) ? 1.1f : 1.0f, 0.1f);
        spaceScale = lerp(spaceScale, Keyboard.isKeyDown(Keyboard.KEY_SPACE) ? 1.1f : 1.0f, 0.1f);
    }

    private float lerp(float start, float end, float factor) {
        return start + (end - start) * factor;
    }

    public void renderKeystrokes() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Рисуем клавиши WASD в виде стрелок
        drawKey("W", posX + KEY_SIZE + SPACING, posY, Keyboard.isKeyDown(Keyboard.KEY_W), wScale);
        drawKey("A", posX, posY + KEY_SIZE + SPACING, Keyboard.isKeyDown(Keyboard.KEY_A), aScale);
        drawKey("S", posX + KEY_SIZE + SPACING, posY + KEY_SIZE + SPACING, Keyboard.isKeyDown(Keyboard.KEY_S), sScale);
        drawKey("D", posX + (KEY_SIZE + SPACING) * 2, posY + KEY_SIZE + SPACING, Keyboard.isKeyDown(Keyboard.KEY_D), dScale);

        // Рисуем Space как прямую линию
        drawSpaceBar(posX, posY + (KEY_SIZE + SPACING) * 2, Keyboard.isKeyDown(Keyboard.KEY_SPACE), spaceScale);
    }

    private void drawKey(String label, int x, int y, boolean pressed, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x + KEY_SIZE / 2, y + KEY_SIZE / 2, 0);
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glTranslatef(-(KEY_SIZE / 2), -(KEY_SIZE / 2), 0);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Цвет в зависимости от нажатия
        if (pressed) {
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.9f); // Яркий зелёный
        } else {
            GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.7f); // Тёмный серый
        }

        // Рисуем прямоугольник
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(0, 0);
        GL11.glVertex2i(KEY_SIZE, 0);
        GL11.glVertex2i(KEY_SIZE, KEY_SIZE);
        GL11.glVertex2i(0, KEY_SIZE);
        GL11.glEnd();

        // Граница
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2i(0, 0);
        GL11.glVertex2i(KEY_SIZE, 0);
        GL11.glVertex2i(KEY_SIZE, KEY_SIZE);
        GL11.glVertex2i(0, KEY_SIZE);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();

        // Рисуем текст
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawStringWithShadow(label, x + KEY_SIZE / 2 - mc.fontRendererObj.getStringWidth(label) / 2, y + KEY_SIZE / 2 - 4, 0xFFFFFF);
    }

    private void drawSpaceBar(int x, int y, boolean pressed, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 50, y + KEY_SIZE / 2, 0);
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glTranslatef(-50, -(KEY_SIZE / 2), 0);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Цвет в зависимости от нажатия
        if (pressed) {
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.9f); // Яркий зелёный
        } else {
            GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.7f); // Тёмный серый
        }

        // Рисуем прямоугольник (длинный для space)
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(0, 0);
        GL11.glVertex2i(100, 0);
        GL11.glVertex2i(100, KEY_SIZE);
        GL11.glVertex2i(0, KEY_SIZE);
        GL11.glEnd();

        // Граница
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2i(0, 0);
        GL11.glVertex2i(100, 0);
        GL11.glVertex2i(100, KEY_SIZE);
        GL11.glVertex2i(0, KEY_SIZE);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();

        // Рисуем текст
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawStringWithShadow("SPACE", x + 50 - mc.fontRendererObj.getStringWidth("SPACE") / 2, y + KEY_SIZE / 2 - 4, 0xFFFFFF);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean enabled) {
        this.animationEnabled = enabled;
    }
}
