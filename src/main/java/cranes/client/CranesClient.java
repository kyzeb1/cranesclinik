package cranes.client;

import cranes.client.gui.CranesGui;
import cranes.client.hacks.ESPHack;
import cranes.client.hacks.FlightHack;
import cranes.client.hacks.FullbrightHack;
import cranes.client.hacks.KillAuraHack;
import cranes.client.hacks.XRayHack;
import cranes.client.render.WatermarkRenderer;
import cranes.client.render.XRayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.InputEvent; // Правильный импорт для InputEvent.Key

@Mod(CranesClient.MOD_ID)
public class CranesClient {
    public static final String MOD_ID = "cranesclient";
    private final FullbrightHack fullbright = new FullbrightHack();
    private final KillAuraHack killAura = new KillAuraHack();
    private final XRayHack xRay = new XRayHack();
    private final FlightHack flight = new FlightHack();
    private final ESPHack esp = new ESPHack();
    private final WatermarkRenderer watermarkRenderer = new WatermarkRenderer();
    private boolean guiKeyPressed = false;
    private Config config;

    public CranesClient() {
        this.config = Config.load();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new XRayRenderer(xRay));
        MinecraftForge.EVENT_BUS.register(watermarkRenderer);
        MinecraftForge.EVENT_BUS.register(esp);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        System.out.println("Cranes Client загружен!");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) { // Исправленный импорт
        if (Minecraft.getInstance().options.keyShift.consumeClick()) {
            if (!guiKeyPressed) {
                Minecraft.getInstance().setScreen(new CranesGui(fullbright, killAura, xRay, flight, esp, watermarkRenderer, config));
                guiKeyPressed = true;
            }
        } else {
            guiKeyPressed = false;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        killAura.tick();
    }
}