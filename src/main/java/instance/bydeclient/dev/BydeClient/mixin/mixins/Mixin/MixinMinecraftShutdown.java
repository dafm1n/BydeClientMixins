package instance.bydeclient.dev.BydeClient.mixin.mixins.Mixin;

import instance.bydeclient.dev.BydeClient.mixin.utils.DiscordRPCManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin для обработки выхода из игры и отключения Discord RPC
 */
@Mixin(Minecraft.class)
public class MixinMinecraftShutdown {

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        DiscordRPCManager.shutdown();
        System.out.println("[BydeClient] Клиент завершает работу");
    }
}
