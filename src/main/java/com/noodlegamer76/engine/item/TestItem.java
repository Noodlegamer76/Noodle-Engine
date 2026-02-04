package com.noodlegamer76.engine.item;

import com.noodlegamer76.engine.core.component.components.ModelRenderer;
import com.noodlegamer76.engine.core.component.components.RigidBody;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.physics.PhysicsEngine;
import com.noodlegamer76.engine.physics.snapshot.BodyHandle;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
            Vec3 spawnPos = new Vec3(
                    player.getX(),
                    Math.max(player.getY(), 1.0),
                    player.getZ()
            ).add(0, 0.5, 0);

            BodyHandle handle = PhysicsEngine.getInstance().testAdd(spawnPos, serverLevel);

            GameObject object = new GameObject(InitEntities.GAME_OBJECT.get(), level);

            ModelRenderer modelRenderer = new ModelRenderer(object);
            BlockState blockState = Blocks.SPRUCE_FENCE_GATE.defaultBlockState();
            ModelResourceLocation block = BlockModelShaper.stateToModelLocation(blockState);

            modelRenderer.setModel(block);

            object.addComponent(modelRenderer);

            RigidBody rigidBody = new RigidBody(object);

            rigidBody.setHandle(handle);

            object.addComponent(rigidBody);

            object.setNoGravity(true);

            level.addFreshEntity(object);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }
}
