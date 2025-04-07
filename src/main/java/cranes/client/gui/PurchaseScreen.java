package cranes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class PurchaseScreen extends Screen {
    private final Screen parent;
    private final Consumer<Void> onPurchase;

    public PurchaseScreen(Screen parent, Consumer<Void> onPurchase) {        super(Component.literal("Покупка подписки"));
        this.parent = parent;
        this.onPurchase = onPurchase;
    }

    @Override
    protected void init() {
        addRenderableWidget(new CustomButton(
                this.width / 2 - 50, this.height / 2 - 10, 100, 20,
                Component.literal("Купить ($10)"),
                button -> {
                    onPurchase.accept(null);
                    Minecraft.getInstance().setScreen(parent);
                }
        ));
        addRenderableWidget(new CustomButton(
                this.width / 2 - 50, this.height / 2 + 20, 100, 20,
                Component.literal("Отмена"),
                button -> Minecraft.getInstance().setScreen(parent)
        ));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.font, "Покупка подписки", this.width / 2, 20, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}