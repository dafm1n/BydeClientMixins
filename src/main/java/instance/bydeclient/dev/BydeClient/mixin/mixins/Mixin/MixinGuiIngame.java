package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import instance.bydeclient.dev.BydeClient.mixin.modules.*;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin для рендера GUI в игре - используется для отрисовки модов
 */
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void onRenderGameOverlay(float partialTicks, CallbackInfo ci) {
        ModuleManager manager = ModuleManager.getInstance();

        // Обновляем все модули
        manager.updateAll();

        // Рендерим Keystrokes
        KeystrokesModule keystrokesModule = manager.getKeystrokesModule();
        if (keystrokesModule != null && keystrokesModule.isEnabled()) {
            keystrokesModule.renderKeystrokes();
        }

        // Рендерим Ping
        PingModule pingModule = manager.getPingModule();
        if (pingModule != null && pingModule.isEnabled()) {
            pingModule.renderPing();
        }

        // Рендерим BlockOverlay
        BlockOverlayModule blockOverlayModule = manager.getBlockOverlayModule();
        if (blockOverlayModule != null && blockOverlayModule.isEnabled()) {
            blockOverlayModule.renderBlockOverlay();
        }

        // Рендерим ArmorStatus
        ArmorStatusModule armorStatusModule = manager.getArmorStatusModule();
        if (armorStatusModule != null && armorStatusModule.isEnabled()) {
            armorStatusModule.renderArmorStatus();
        }

        // Рендерим Player Counter
        PlayerCounterModule playerCounterModule = manager.getPlayerCounterModule();
        if (playerCounterModule != null && playerCounterModule.isEnabled()) {
            playerCounterModule.renderPlayerCounter();
        }

        // Применяем Motion Blur
        MotionBlurModule motionBlurModule = manager.getMotionBlurModule();
        if (motionBlurModule != null && motionBlurModule.isEnabled()) {
            motionBlurModule.applyMotionBlur(partialTicks);
        }

        // Применяем Inventory Blur
        InventoryBlurModule inventoryBlurModule = manager.getInventoryBlurModule();
        if (inventoryBlurModule != null && inventoryBlurModule.isEnabled()) {
            inventoryBlurModule.applyInventoryBlur();
        }

        // Рендерим FPS
        FPSModule fpsModule = manager.getFPSModule();
        if (fpsModule != null && fpsModule.isEnabled()) {
            fpsModule.renderFPS();
        }
    }
}
