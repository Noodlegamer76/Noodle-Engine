package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.ComponentType;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GltfRenderer extends Component implements RenderableComponent{
    private final SyncedVar<ResourceLocation> modelLocation = new SyncedVar<>(this, ResourceLocation.fromNamespaceAndPath("", ""), GameObjectSerializers.RESOURCE_LOCATION);

    Map<McMaterial, List<GltfVbo>> vboMap = new HashMap<>();
    private boolean modelLoaded = false;

    public GltfRenderer(GameObject gameObject) {
        super(InitComponents.GLTF_RENDERER, gameObject);
    }

    @Override
    public List<SyncedVar<?>> getSyncedData() {
        return List.of(
                modelLocation
        );
    }

    public void setModel(ResourceLocation location) {
        modelLocation.setValue(location, true);
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      if (entity.level().isClientSide) {
          if (!modelLoaded && !modelLocation.getValue().getPath().isEmpty()) {
              McGltf model = ModelStorage.getModel(modelLocation.getValue());
              for (MeshData data: model.getMeshModelToMeshData().values()) {
                  for (Map.Entry<McMaterial, GltfVbo> entry: data.getPrimitiveBuffers().entrySet()) {
                      if (!vboMap.containsKey(entry.getKey())) {
                          vboMap.put(entry.getKey(), new ArrayList<>());
                      }
                      vboMap.get(entry.getKey()).add(entry.getValue());
                  }
              }
              modelLoaded = true;
          }
      }
    }
}
