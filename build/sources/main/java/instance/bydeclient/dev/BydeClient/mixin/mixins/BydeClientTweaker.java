package instance.bydeclient.dev.BydeClient.mixin.mixins;

import instance.bydeclient.dev.BydeClient.mixin.modules.ModuleManager;
import instance.bydeclient.dev.BydeClient.mixin.utils.DiscordRPCManager;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BydeClientTweaker implements ITweaker {

    private static final String CUSTOM_NICK = "De1fy"; // ← ваш желаемый ник
    private final List<String> launchArgs = new ArrayList<>();

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        // 1. Подменяем --username
        List<String> modifiedArgs = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            if ("--username".equals(arg) && i + 1 < args.size()) {
                modifiedArgs.add(arg);
                modifiedArgs.add(CUSTOM_NICK);
                i++; // пропускаем оригинальное имя
            } else {
                modifiedArgs.add(arg);
            }
        }

        this.launchArgs.addAll(modifiedArgs);

        // 2. Безопасно добавляем gameDir, assetsDir, version (они МОГУТ быть null!)
        if (gameDir != null) {
            launchArgs.add("--gameDir");
            launchArgs.add(gameDir.getAbsolutePath());
        }
        if (assetsDir != null) {
            launchArgs.add("--assetsDir");
            launchArgs.add(assetsDir.getAbsolutePath());
        }
        if (profile != null) {
            launchArgs.add("--version");
            launchArgs.add(profile);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        try {
            URL jarLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation();
            classLoader.addURL(jarLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.bydeclient.json");

        MixinEnvironment env = MixinEnvironment.getDefaultEnvironment();
        env.setObfuscationContext("notch");
        env.setSide(MixinEnvironment.Side.CLIENT);

        // Инициализируем ModuleManager
        ModuleManager.getInstance();
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return launchArgs.toArray(new String[0]);
    }
}