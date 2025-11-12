package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import instance.bydeclient.dev.BydeClient.mixin.gui.ModuleGuiScreen;
import instance.bydeclient.dev.BydeClient.mixin.utils.DiscordRPCManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin для обработки клавиш в главном классе Minecraft
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {
    private static boolean gKeyPressed = false;
    private static boolean discordInitialized = false;
    private static int discordUpdateCounter = 0;

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onRunTick(CallbackInfo ci) {
        // Инициализируем Discord RPC при первом запуске
        if (!discordInitialized) {
            DiscordRPCManager.init();
            discordInitialized = true;
        }

        // Обновляем Discord RPC каждые 15 тиков (примерно каждые 0.75 секунды)
        discordUpdateCounter++;
        if (discordUpdateCounter >= 15) {
            DiscordRPCManager.update();
            discordUpdateCounter = 0;
        }

        // Проверяем нажатие клавиши G для открытия GUI модулей
        boolean gKeyDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

        if (gKeyDown && !gKeyPressed) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null && mc.thePlayer != null) {
                mc.displayGuiScreen(new ModuleGuiScreen());
            }
            gKeyPressed = true;
        } else if (!gKeyDown) {
            gKeyPressed = false;
        }
    }
}
