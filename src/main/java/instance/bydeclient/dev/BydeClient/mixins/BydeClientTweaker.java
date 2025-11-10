package instance.bydeclient.dev.BydeClient.mixins;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BydeClientTweaker implements ITweaker {

    private List<String> LaunchArgs = new ArrayList<>();

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.LaunchArgs.addAll(args);
        addArgs("gameDir", gameDir);
        addArgs("assetsDir", assetsDir);
        addArgs("version", profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {

        MixinBootstrap.init();

        MixinEnvironment e = MixinEnvironment.getDefaultEnvironment();
        e.addConfiguration("mixins.bydeclient.json");

        if(e.getObfuscationContext() ==null) e.setObfuscationContext("notch");
        e.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }

    @Override
    public String[] getLaunchArguments() {
        return LaunchArgs.toArray(new String[0]);
    }

    private void addArgs(String label, Object value) {
        this.LaunchArgs.add("--"+ label);
        this.LaunchArgs.add(value instanceof String ? (String)value : value instanceof File ? ((File)value).getAbsolutePath() : ".");

    }
}
