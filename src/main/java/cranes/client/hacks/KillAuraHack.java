package cranes.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class KillAuraHack {
    private boolean enabled = false;
    private float range = 4.0f;
    private boolean attackPlayers = true;
    private boolean attackMobs = true;
    private final Minecraft mc = Minecraft.getInstance();

    public void toggle() {
        enabled = !enabled;
        System.out.println("KillAura: " + (enabled ? "Включён" : "Выключен"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public boolean isAttackPlayers() {
        return attackPlayers;
    }

    public void setAttackPlayers(boolean attackPlayers) {
        this.attackPlayers = attackPlayers;
    }

    public boolean isAttackMobs() {
        return attackMobs;
    }

    public void setAttackMobs(boolean attackMobs) {
        this.attackMobs = attackMobs;
    }

    public void tick() {
        if (!enabled || mc.level == null || mc.player == null) return;

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity == mc.player) continue;
            if (mc.player.distanceTo(entity) > range) continue;

            if ((entity instanceof Player && attackPlayers) || (entity instanceof Monster && attackMobs)) {
                mc.player.attack(entity);
            }
        }
    }
}