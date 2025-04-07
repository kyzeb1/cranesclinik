package cranes.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class FlightHack {
    private boolean enabled = false;
    private float speed = 0.5f;
    private final Minecraft mc = Minecraft.getInstance();

    public void toggle() {
        enabled = !enabled;
        if (mc.player != null) {
            mc.player.getAbilities().mayfly = enabled;
            if (!enabled) {
                mc.player.getAbilities().flying = false;
            }
        }
        System.out.println("Flight: " + (enabled ? "Включён" : "Выключен"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        if (enabled && mc.player != null) {
            mc.player.getAbilities().setFlyingSpeed(speed);
        }
    }
}