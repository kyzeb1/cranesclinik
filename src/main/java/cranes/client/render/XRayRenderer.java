package cranes.client.render;

import cranes.client.hacks.XRayHack;
import net.minecraft.client.Minecraft;

public class XRayRenderer {
    private final XRayHack xRay;
    private final Minecraft mc = Minecraft.getInstance();

    public XRayRenderer(XRayHack xRay) {
        this.xRay = xRay;
    }

    // Временно отключаем рендеринг XRay, так как RenderLevelStageEvent больше не существует
    public void onRenderLevel() {
        if (!xRay.isEnabled()) return;
        if (mc.level == null || mc.player == null) return;

        // Заглушка для XRay
        System.out.println("XRay рендеринг активен, но функционал не реализован в версии 1.21.4");
    }
}