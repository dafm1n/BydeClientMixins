package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * Мод ArmorStatus - показывает статус брони и её прочность
 */
public class ArmorStatusModule extends Module {
    private int posX = 10;
    private int posY = 10;
    private static final int ARMOR_SIZE = 20;
    private static final int SPACING = 3;
    private boolean showDurability = true;
    private boolean showPercentage = true;

    public ArmorStatusModule() {
        super("Armor Status", "Показывает статус и прочность брони");
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

    public void renderArmorStatus() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        ItemStack[] armorItems = mc.thePlayer.inventory.armorInventory;
        int yOffset = posY;

        // Порядок: Helmet, Chestplate, Leggings, Boots
        String[] armorNames = {"Helmet", "Chest", "Legs", "Boots"};

        for (int i = 0; i < armorItems.length; i++) {
            if (armorItems[i] != null) {
                drawArmorItem(mc, armorItems[i], armorNames[i], posX, yOffset, i);
                yOffset += ARMOR_SIZE + SPACING;
            }
        }

        // Рисуем линию разделения
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(posX - 2, yOffset);
        GL11.glVertex2f(posX + 70, yOffset);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawArmorItem(Minecraft mc, ItemStack armorStack, String name, int x, int y, int slot) {
        // Рисуем иконку брони (если нужно отсосировать иконку)
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);

        // Фон квадрата
        drawRect(0, 0, ARMOR_SIZE, ARMOR_SIZE, 0x40000000);

        // Граница
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(ARMOR_SIZE, 0);
        GL11.glVertex2f(ARMOR_SIZE, ARMOR_SIZE);
        GL11.glVertex2f(0, ARMOR_SIZE);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();

        // Текст с названием
        mc.fontRendererObj.drawStringWithShadow(name, x + ARMOR_SIZE + SPACING, y + 4, 0xFFFFFFFF);

        // Прочность
        if (showDurability && armorStack.isItemDamaged()) {
            int maxDamage = armorStack.getMaxDamage();
            int damage = armorStack.getItemDamage();
            int durability = maxDamage - damage;

            // Цвет по прочности
            int durabilityColor = getDurabilityColor(durability, maxDamage);

            if (showPercentage) {
                int percentage = (int) ((durability * 100.0f) / maxDamage);
                mc.fontRendererObj.drawStringWithShadow(
                        percentage + "%",
                        x + ARMOR_SIZE + SPACING,
                        y + 12,
                        durabilityColor
                );
            } else {
                mc.fontRendererObj.drawStringWithShadow(
                        durability + "/" + maxDamage,
                        x + ARMOR_SIZE + SPACING,
                        y + 12,
                        durabilityColor
                );
            }
        }
    }

    private int getDurabilityColor(int durability, int maxDurability) {
        float ratio = (float) durability / maxDurability;

        if (ratio > 0.75f) {
            return 0xFF00FF00; // Зелёный - отлично
        } else if (ratio > 0.5f) {
            return 0xFFFFFF00; // Жёлтый - хорошо
        } else if (ratio > 0.25f) {
            return 0xFFFF8800; // Оранжевый - норма
        } else if (ratio > 0.1f) {
            return 0xFFFF4400; // Красно-оранжевый - плохо
        } else {
            return 0xFFFF0000; // Красный - критично
        }
    }

    private void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
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

    public boolean isShowDurability() {
        return showDurability;
    }

    public void setShowDurability(boolean show) {
        this.showDurability = show;
    }

    public boolean isShowPercentage() {
        return showPercentage;
    }

    public void setShowPercentage(boolean show) {
        this.showPercentage = show;
    }
}

