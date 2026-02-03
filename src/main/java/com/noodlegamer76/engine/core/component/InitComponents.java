package com.noodlegamer76.engine.core.component;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.components.GltfRenderer;
import com.noodlegamer76.engine.core.component.components.ModelRenderer;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.event.NoodleEngineRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitComponents {
    public static final DeferredRegister<ComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(NoodleEngineRegistries.COMPONENT_TYPE, NoodleEngine.MODID);

    public static final RegistryObject<ComponentType<?>> MODEL_RENDERER = COMPONENT_TYPES.register("model_renderer",
            () -> new ComponentType<>(ModelRenderer::new));

    public static final RegistryObject<ComponentType<?>> GLTF_RENDERER = COMPONENT_TYPES.register("gltf_renderer",
            () -> new ComponentType<>(GltfRenderer::new));

    @FunctionalInterface
    public interface ComponentSupplier<T extends Component> {
        T create(GameObject var1);
    }

    public static ComponentType<?> getComponentType(ResourceLocation id) {
        return InitComponents.COMPONENT_TYPES.getEntries().stream()
                .filter(holder -> holder.getId().equals(id))
                .map(RegistryObject::get)
                .findFirst()
                .orElse(null);
    }
}
