package com.noodlegamer76.engine.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.core.component.components.GltfRenderer;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TestItem extends Item {

    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
       //if (!level.isClientSide) {
       //    GameObject object = new GameObject(InitEntities.GAME_OBJECT.get(), level);

       //    object.setPos(player.getX(), player.getY(), player.getZ());

       //    ModelRenderer modelRenderer = new ModelRenderer(object);
       //    object.addComponent(modelRenderer);

       //    //Block example
       //    BlockState blockState = Blocks.BAMBOO_BLOCK.defaultBlockState();
       //    ModelResourceLocation block = BlockModelShaper.stateToModelLocation(blockState);

       //    //Item example
       //    ModelResourceLocation item = new ModelResourceLocation("minecraft", "diamond", "inventory");

       //    modelRenderer.setModel(block);

       //    level.addFreshEntity(object);
       //}
        if (!level.isClientSide) {
            McGltf model = ModelStorage.getModel(ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "gltf/truck.glb"));
            Vec3 position = player.position();
            for (MeshData meshData : model.getMeshes()) {
                PoseStack poseStack = new PoseStack();
                poseStack.pushPose();

                poseStack.translate(position.x, position.y, position.z);
                poseStack.scale(100, 100, 100);

                GlbRenderer.addInstance(meshData, poseStack, -1);

                poseStack.popPose();
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }
}
