package com.noodlegamer76.engine.client;

import net.minecraft.client.renderer.ShaderInstance;

//shaders load after the materials, so This is a wrapper to prevent timing issues
public class ShaderReference {
    public ShaderInstance shader;
}
