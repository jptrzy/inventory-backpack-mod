package net.jptrzy.inventory.backpack.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class BackpackModel extends BipedEntityModel<LivingEntity> {

    public BackpackModel() {
        super(newParts());


        this.body.visible = true;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.head.visible = false;
        this.hat.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;
    }

    public void test(BipedEntityModel<LivingEntity> contextModel){

    }

    private static ModelPart newParts() {

        ModelData data = getModelData(Dilation.NONE, 0.0F);
        ModelPartData child = data.getRoot().getChild("body");

        child.addChild("base", ModelPartBuilder.create()
//                        .mirrored()
                        .uv(0, 0)
                        .cuboid(-3.5F, .5f, 2.2F, 7.0F, 10.0F, 4.0F),
                ModelTransform.NONE);

//        bb_main.setTextureOffset(0, 0).addBox(-3.5F, -23.0F, 2.2F, 7.0F, 10.0F, 4.0F, 0.0F, false);
        return data.getRoot().createPart(64, 64);
    }
}
