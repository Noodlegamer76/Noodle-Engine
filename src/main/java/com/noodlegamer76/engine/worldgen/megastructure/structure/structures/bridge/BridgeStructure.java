package com.noodlegamer76.engine.worldgen.megastructure.structure.structures.bridge;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.worldgen.megastructure.Node;
import com.noodlegamer76.engine.worldgen.megastructure.structure.Structure;
import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.worldgen.megastructure.structure.context.GenVar;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BridgeStructure extends Structure {
    public BridgeStructure(int priority) {
        super(priority);
    }

    @Override
    public int getMaxSize() {
        return 63;
    }

    @Override
    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node n, RandomSource random, StructureInstance instance) {
        BlockPos origin = ctx.origin();
        BlockPos pos = origin.east();

        MinecraftServer server = ctx.level().getServer();

        if (server == null) return;

        ResourceLocation bridgeSupportLoc = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "castle/supports/bridge_support_1");
        ResourceLocation bridgeSupportTopLoc = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "castle/supports/bridge_support_1_top");
        ResourceLocation bridgeLoc = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "castle/bridge/top_1");

        StructureTemplate bridgeSupport = server
                .getStructureManager()
                .get(bridgeSupportLoc)
                .orElse(null);

        StructureTemplate bridgeSupportTop = server
                .getStructureManager()
                .get(bridgeSupportTopLoc)
                .orElse(null);

        StructureTemplate bridge = server
                .getStructureManager()
                .get(bridgeLoc)
                .orElse(null);

        int bridgeHeight = 200;

        StructurePlaceSettings settings = new StructurePlaceSettings();

        for (int i = pos.getY(); i < bridgeHeight;) {
            pos = new BlockPos(n.getX(), i, n.getZ());
            AABB aabb = new AABB(pos, pos.offset(13, 5, 13));
            if (pos.getY() + 5 < bridgeHeight) {
                StructurePlacer placer = new StructurePlacer(aabb, pos, bridgeSupport, settings);
                instance.addPlacer(placer);
            }
            else {
                StructurePlacer placer = new StructurePlacer(aabb, pos, bridgeSupportTop, settings);
                instance.addPlacer(placer);
            }
            i += 5;
        }

        for (int i = n.getZ(); i < n.getZ() + getMaxSize() + 15;) {
            pos = new BlockPos(n.getX(), bridgeHeight, i);
            AABB aabb = new AABB(pos.east(), pos.offset(18 - 2, 7, 13));
            StructurePlacer placer = new StructurePlacer(aabb, pos, bridge, settings);
            instance.addPlacer(placer);
            i += 13;
        }
    }

    @Override
    public List<GenVar<?>> getGenVariables() {
        return List.of();
    }
}
