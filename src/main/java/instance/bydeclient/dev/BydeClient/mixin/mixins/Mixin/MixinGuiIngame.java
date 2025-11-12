package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import instance.bydeclient.dev.BydeClient.mixin.modules.BlockOverlayModule;
import instance.bydeclient.dev.BydeClient.mixin.modules.KeystrokesModule;
import instance.bydeclient.dev.BydeClient.mixin.modules.ModuleManager;
import instance.bydeclient.dev.BydeClient.mixin.modules.PingModule;
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
    }
}
