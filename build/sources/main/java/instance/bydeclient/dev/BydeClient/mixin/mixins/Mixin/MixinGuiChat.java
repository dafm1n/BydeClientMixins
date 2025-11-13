package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import instance.bydeclient.dev.BydeClient.mixin.gui.ModuleGuiScreen;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin для обработки клавиш в чате
 * GUI модулей теперь открывается только на Right Shift (не в чате)
 */
@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        // Проверяем если нажата Right Shift - не открываем GUI в чате
        if (keyCode == Keyboard.KEY_RSHIFT) {
            ci.cancel();
        }
    }
}
