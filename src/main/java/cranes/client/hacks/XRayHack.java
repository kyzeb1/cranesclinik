package cranes.client.hacks;

import java.util.HashSet;
import java.util.Set;

public class XRayHack {
    private boolean enabled = false;
    private Set<String> blocks = new HashSet<>();

    public void toggle() {
        enabled = !enabled;
        System.out.println("XRay: " + (enabled ? "Включён" : "Выключен"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<String> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<String> blocks) {
        this.blocks = blocks;
    }
}