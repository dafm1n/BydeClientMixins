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
 */
@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        // Открываем GUI модулей по нажатию G
        if (keyCode == Keyboard.KEY_G) {
            net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new ModuleGuiScreen());
            ci.cancel();
        }
    }
}
