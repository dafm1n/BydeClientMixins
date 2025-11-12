package instance.bydeclient.dev.BydeClient.mixin.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер для управления всеми модами
 */
public class ModuleManager {
    private static ModuleManager instance;
    private Map<String, Module> modules = new HashMap<>();
    private List<Module> moduleList = new ArrayList<>();

    private KeystrokesModule keystrokesModule;
    private AutoGGModule autoGGModule;
    private FPSBoostModule fpsBoostModule;
    private AutoSprintModule autoSprintModule;
    private BlockOverlayModule blockOverlayModule;
    private NametagsModule nametagsModule;
    private PingModule pingModule;
    private ItemPhysicsModule itemPhysicsModule;

    private ModuleManager() {
        initializeModules();
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    private void initializeModules() {
        // Создаём все модули
        keystrokesModule = new KeystrokesModule();
        autoGGModule = new AutoGGModule();
        fpsBoostModule = new FPSBoostModule();
        autoSprintModule = new AutoSprintModule();
        blockOverlayModule = new BlockOverlayModule();
        nametagsModule = new NametagsModule();
        pingModule = new PingModule();
        itemPhysicsModule = new ItemPhysicsModule();

        // Добавляем в список
        addModule(keystrokesModule);
        addModule(autoGGModule);
        addModule(fpsBoostModule);
        addModule(autoSprintModule);
        addModule(blockOverlayModule);
        addModule(nametagsModule);
        addModule(pingModule);
        addModule(itemPhysicsModule);
    }

    private void addModule(Module module) {
        modules.put(module.getName(), module);
        moduleList.add(module);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public KeystrokesModule getKeystrokesModule() {
        return keystrokesModule;
    }

    public AutoGGModule getAutoGGModule() {
        return autoGGModule;
    }

    public FPSBoostModule getFPSBoostModule() {
        return fpsBoostModule;
    }

    public AutoSprintModule getAutoSprintModule() {
        return autoSprintModule;
    }

    public BlockOverlayModule getBlockOverlayModule() {
        return blockOverlayModule;
    }

    public NametagsModule getNametagsModule() {
        return nametagsModule;
    }

    public PingModule getPingModule() {
        return pingModule;
    }

    public ItemPhysicsModule getItemPhysicsModule() {
        return itemPhysicsModule;
    }

    public List<Module> getModules() {
        return new ArrayList<>(moduleList);
    }

    public void updateAll() {
        for (Module module : moduleList) {
            if (module.isEnabled()) {
                module.onUpdate();
            }
        }
    }

    public void toggleModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.toggle();
        }
    }

    public void enableModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.setEnabled(true);
        }
    }

    public void disableModule(String name) {
        Module module = getModule(name);
        if (module != null) {
            module.setEnabled(false);
        }
    }

    public void disableAll() {
        for (Module module : moduleList) {
            module.setEnabled(false);
        }
    }

    public void printModuleStatus() {
    }
}
