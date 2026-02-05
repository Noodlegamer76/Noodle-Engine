package com.noodlegamer76.engine.client.glitf.material;

import com.noodlegamer76.engine.event.ShaderRegistry;
import net.minecraft.resources.ResourceLocation;

public class Materials {
    public static final McMaterial DEFAULT = new MaterialBuilder(ShaderRegistry.pbr, "temp")
            .texture(ResourceLocation.withDefaultNamespace("textures/block/dirt.png"))
            .build();
}
