package cranes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

public class CranesGui extends Screen {

    private String selectedTab = "Main";
    private String selectedCategory = "Visual";
    private boolean isSubscribed = false;
    private String subscriptionExpiry = "N/A";
    private final Set<String> xRayBlocks = new HashSet<>();

    public CranesGui() {
        super(Component.literal("Cranes Client"));

        checkSubscription();
    }

    private void checkSubscription() {

    }

    private void purchaseSubscription() {

    }

    @Override
    protected void init() {
        renderCheats(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xCC000000, 0xCC333333);
        guiGraphics.drawCenteredString(this.font, "Cranes Client", this.width / 2, 10, 0xFF5555);
        guiGraphics.fill(5, 30, 115, this.height - 5, 0xFF222222);
        guiGraphics.fillGradient(120, 30, this.width - 5, this.height - 5, 0xFF333333, 0xFF444444);

        if (selectedTab.equals("Main")) {
            renderCheats(guiGraphics);
        } else {
            renderSettings(guiGraphics);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderCheats(GuiGraphics guiGraphics) {
        clearWidgets();



        if (guiGraphics == null) return;

        if (!isSubscribed) {
            guiGraphics.drawString(this.font, "Пожалуйста, купите подписку, чтобы использовать клиент!", 130, 40, 0xFF5555);
            guiGraphics.drawString(this.font, "Подписка истекла или не куплена", 130, 50, 0xFF5555);
            return;
        }

        guiGraphics.drawString(this.font, "Подписка активна до: " + subscriptionExpiry, 130, 40, 0x55FF55);

        if (selectedCategory.equals("Visual")) {
            guiGraphics.drawString(this.font, "Визуальные", 130, 40, 0xFF5555);


        } else if (selectedCategory.equals("Combat")) {
            guiGraphics.drawString(this.font, "Боевые", 130, 40, 0xFF5555);
        } else if (selectedCategory.equals("Render")) {
            guiGraphics.drawString(this.font, "Рендеринг", 130, 40, 0xFF5555);


        } else if (selectedCategory.equals("Movement")) {
            guiGraphics.drawString(this.font, "Движение", 130, 40, 0xFF5555);

        } else if (selectedCategory.equals("ESP")) {

        }
    }

    private void renderSettings(GuiGraphics guiGraphics) {
        clearWidgets();



        if (guiGraphics == null) return;

        guiGraphics.drawString(this.font, "Настройки", 130, 40, 0xFF5555);
        guiGraphics.drawString(this.font, "Клавиша GUI: Right Shift", 130, 60, 0xFFFFFF);
        

        guiGraphics.drawString(this.font, "Настройки водяного знака", 130, 110, 0xFF5555);
        guiGraphics.drawString(this.font, "Цвет:", 130, 130, 0xFFFFFF);


        guiGraphics.drawString(this.font, "Позиция:", 130, 170, 0xFFFFFF);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}