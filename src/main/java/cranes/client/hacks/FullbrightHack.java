package cranes.client.hacks;

import net.minecraft.client.Minecraft;

public class FullbrightHack {
    private boolean enabled = false;
    private float brightness = 1000.0f;
    private final Minecraft mc = Minecraft.getInstance();

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            mc.options.gamma().set((double) brightness);
        } else {
            mc.options.gamma().set(1.0);
        }
        System.out.println("Fullbright: " + (enabled ? "Включён" : "Выключен"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        if (enabled) {
            mc.options.gamma().set((double) brightness);
        }
    }
}