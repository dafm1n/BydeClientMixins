package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Мод AutoSprint - автоматический спринт при движении вперёд
 */
public class AutoSprintModule extends Module {
    private boolean wasSprintingLastTick = false;

    public AutoSprintModule() {
        super("AutoSprint", "Автоматический спринт при движении");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        wasSprintingLastTick = false;
    }

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;

        if (player == null) return;

        // Проверяем, движется ли игрок вперёд
        boolean isMovingForward = mc.gameSettings.keyBindForward.isKeyDown();

        // Если движется вперёд и не в воде/лаве
        if (isMovingForward && !player.isInWater() && !player.isInLava()) {
            // Включаем спринт
            if (!player.isSprinting()) {
                player.setSprinting(true);
            }
        } else {
            // Отключаем спринт
            if (player.isSprinting()) {
                player.setSprinting(false);
            }
        }
    }
}
