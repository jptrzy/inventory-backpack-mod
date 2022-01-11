package net.jptrzy.inventory.backpack.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.mixin.HandledScreenAccessor;
import net.jptrzy.inventory.backpack.mixin.ScreenAccessor;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BackpackScreen extends InventoryScreen {

    public static final Identifier BACKGROUND_TEXTURE = Main.id("textures/gui/backpack_inventory.png");

    public BackpackScreen(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(inventory.player);
        ((HandledScreenAccessor) this).setHandler(handler);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawEntity(i + 51, j + 75, 30, (float)(i + 51) - mouseX, (float)(j + 75 - 50) - mouseY, this.client.player);
        moveRecipeButton();
    }

    @Override
    public void init() {
        backgroundHeight = 224;
        super.init();
    }



    //TODO could be make more optimal
    public void moveRecipeButton(){
        for(Drawable widget: ((ScreenAccessor)this).getDrawables())
            if(widget instanceof TexturedButtonWidget && ((TexturedButtonWidget) widget).y == this.height / 2 - 22) //without last line it will "fly" to the infinity
                ((TexturedButtonWidget) widget).setPos(((TexturedButtonWidget) widget).x, ((TexturedButtonWidget) widget).y-29);
    }


}
