package cranes.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WatermarkRenderer {
    private boolean enabled = true;
    private int color = 0xFF5555;
    private String position = "top-left";

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @SubscribeEvent
    public void onRenderScreen(ScreenEvent.Render event) {
        if (!enabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        String watermark = "Cranes Client v1.0";
        String fps = "FPS: " + Minecraft.getInstance().getFps();
        String ping = "Пинг: " + (mc.getConnection() != null && mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId()) != null ? mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId()).getLatency() : "N/A");
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        int x = position.contains("left") ? 5 : mc.getWindow().getGuiScaledWidth() - mc.font.width(watermark) - 5;
        int y = position.contains("top") ? 5 : mc.getWindow().getGuiScaledHeight() - 40;

        guiGraphics.drawString(mc.font, watermark, x, y, color);
        guiGraphics.drawString(mc.font, fps, x, y + 10, 0xFFFFFF);
        guiGraphics.drawString(mc.font, ping, x, y + 20, 0xFFFFFF);
        guiGraphics.drawString(mc.font, dateTime, x, y + 30, 0xFFFFFF);
    }
}