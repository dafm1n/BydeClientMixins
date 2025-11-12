package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Render.class)
public class MixinRender {

    @Inject(method = "doRender", at = @At("RETURN"))
    private void onDoRender(Entity entity, double x, double y, double z, float yaw,
                            float partialTicks, CallbackInfo ci) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            // Подсвечиваем если игрок держит меч
            if (isHoldingSword(player)) {
                drawRedBox(x, y, z);
            }

            // Подсвечиваем если этот игрок кого-то убил мечом
            if (hasKilledWithSword(player)) {
                drawRedBox(x, y, z);
            }
        }
    }

    private boolean isHoldingSword(EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        return heldItem != null && heldItem.getItem() instanceof ItemSword;
    }

    private boolean hasKilledWithSword(EntityPlayer player) {
        ItemStack weapon = player.getHeldItem();
        // Если держит меч и недавно наносил урон (hurtTime > 0)
        if (weapon != null && weapon.getItem() instanceof ItemSword) {
            return player.hurtTime > 0;
        }
        return false;
    }

    private void drawRedBox(double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(2.5f);
        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.7f);

        double w = 0.5;
        double h = 1.8;

        renderBox(w, h);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void renderBox(double w, double h) {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(-w, 0, -w);
        GL11.glVertex3d(w, 0, -w);
        GL11.glVertex3d(w, h, -w);
        GL11.glVertex3d(-w, h, -w);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(-w, 0, w);
        GL11.glVertex3d(w, 0, w);
        GL11.glVertex3d(w, h, w);
        GL11.glVertex3d(-w, h, w);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(-w, 0, -w);
        GL11.glVertex3d(-w, 0, w);
        GL11.glVertex3d(w, 0, -w);
        GL11.glVertex3d(w, 0, w);
        GL11.glVertex3d(-w, h, -w);
        GL11.glVertex3d(-w, h, w);
        GL11.glVertex3d(w, h, -w);
        GL11.glVertex3d(w, h, w);
        GL11.glEnd();
    }
}