package com.noodlegamer76.engine.item;

import com.noodlegamer76.engine.NoodleEngine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, NoodleEngine.MODID);

    public static final RegistryObject<TestItem> TEST_ITEM = ITEMS.register("test_item",
            () -> new TestItem(new Item.Properties()));
}
