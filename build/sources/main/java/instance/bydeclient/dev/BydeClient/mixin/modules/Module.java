package instance.bydeclient.dev.BydeClient.mixin.modules;

/**
 * Базовый класс для всех модов
 */
public abstract class Module {
    private String name;
    private String description;
    private boolean enabled;

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onUpdate();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled && !this.enabled) {
            this.enabled = true;
            onEnable();
        } else if (!enabled && this.enabled) {
            this.enabled = false;
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }
}
