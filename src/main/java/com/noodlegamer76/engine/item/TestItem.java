package com.noodlegamer76.engine.item;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.components.MeshRenderer;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.gui.structure.StructureEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TestItem extends Item {

    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
       //if (!level.isClientSide) {
       //    GameObject object = new GameObject(InitEntities.GAME_OBJECT.get(), level);
//
       //    object.setPos(player.getX(), player.getY(), player.getZ());
//
       //    MeshRenderer meshRenderer = new MeshRenderer(object);
       //    ResourceLocation location = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "gltf/bird.glb");
       //    meshRenderer.setModelLocation(location);
//
       //    object.addComponent(meshRenderer);
//
       //    level.addFreshEntity(object);
       //}
       // if (level instanceof ServerLevel serverLevel) {
       //     for (int i = 0; i < 1; i++) {
       //         Structures.getInstance().clearDefinitions();
       //         Structures.getInstance().setupStructures(serverLevel.getServer());
       //     }
       // }
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new StructureEditorScreen());
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }
}
