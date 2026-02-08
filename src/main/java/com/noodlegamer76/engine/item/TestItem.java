package com.noodlegamer76.engine.item;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.core.component.components.MeshRenderer;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.entity.InitEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 1; i++) {
                Vec3 offset = new Vec3((Math.random() - 0.5) * 7, (Math.random() - 0.5) * 7, (Math.random() - 0.5) * 7);
                GameObject object = new GameObject(InitEntities.GAME_OBJECT.get(), level);
                object.setPos(player.getX() + offset.x, player.getY() + offset.y, player.getZ() + offset.z);

                ResourceLocation location = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "gltf/truck.glb");

                MeshRenderer renderer = new MeshRenderer(object);
                renderer.setModelLocation(location);
                object.addComponent(renderer);

                object.setNoGravity(true);

                level.addFreshEntity(object);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }
}
