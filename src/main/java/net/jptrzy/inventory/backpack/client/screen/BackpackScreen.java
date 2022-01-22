package net.jptrzy.inventory.backpack.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.mixin.HandledScreenAccessor;
import net.jptrzy.inventory.backpack.mixin.ScreenAccessor;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BackpackScreen extends InventoryScreen {

    public static final Identifier BACKGROUND_TEXTURE = Main.id("textures/gui/backpack_inventory.png");
    public static final Identifier BACKGROUND_TEXTURE_OVERLAY = Main.id("textures/gui/backpack_inventory_overlay.png");
    public static final Identifier ENDER_BACKGROUND_TEXTURE_OVERLAY = Main.id("textures/gui/ender_backpack_inventory_overlay.png");

    private boolean color = false;
    private boolean isEnder;

    private float r = 1;
    private float g = 1;
    private float b = 1;

    public BackpackScreen(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(inventory.player);
//        super.tick();
        ((HandledScreenAccessor) this).setHandler(handler);
        checkColor();
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        if(!color){
            checkColor();
        }
    }

    public void checkColor(){
        ItemStack itemStack = Utils.getBackpack(MinecraftClient.getInstance().player);
        if(itemStack == null){
            color = false;
            Main.LOGGER.warn("error");
            return;
        }
        setColor(itemStack);
    }

    protected void setColor(ItemStack stack){
        isEnder = Utils.isEnderBackpack(stack);
        if(isEnder){
            r = 1;
            g = 1;
            b = 1;
            return;
        }

        int i = ((BackpackItem) stack.getItem()).getColor(stack);


        r = (float)(i >> 16 & 255) / 255.0F;
        g = (float)(i >> 8 & 255) / 255.0F;
        b = (float)(i & 255) / 255.0F;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        moveRecipeButton();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r, g, b, 1);
        RenderSystem.setShaderTexture(0, isEnder ? ENDER_BACKGROUND_TEXTURE_OVERLAY : BACKGROUND_TEXTURE_OVERLAY);
        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        drawEntity(this.x + 51, this.y + 75, 30, (float)(this.x + 51) - mouseX, (float)(this.y + 75 - 50) - mouseY, this.client.player);
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
