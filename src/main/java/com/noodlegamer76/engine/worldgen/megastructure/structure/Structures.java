package com.noodlegamer76.engine.worldgen.megastructure.structure;

import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.bridge.BridgeStructure;
import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.castle.WallStructure;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

//Temporary class for definitions
public class Structures {
    private static final Structures instance = new Structures();

    public static Structures getInstance() {
        return instance;
    }

    private final List<StructureDefinition> structures = new ArrayList<>();
    private int nextId;

    public void setupStructures(MinecraftServer server) {
        //StructureDefinition<EmptyContext> testDef = new StructureDefinition<>(EmptyContext::new);
        //TestStructure<EmptyContext> test = new TestStructure<>(0, ResourceLocation.withDefaultNamespace("village/plains/houses/plains_small_house_1"));
        //testDef.addStructure(test);
        //addDefinition(testDef);

        //StructureDefinition<EmptyContext> ancientDef = new StructureDefinition<>(EmptyContext::new);
        //TestStructure<EmptyContext> ancient = new TestStructure<>(0, ResourceLocation.withDefaultNamespace("end_city/ship"));
        //ancientDef.addStructure(ancient);
        //addDefinition(ancientDef);

        //StructureDefinition<EmptyContext> pathDef  = new StructureDefinition<>(EmptyContext::new);
        //BlockState path = Blocks.DIRT_PATH.defaultBlockState();

        //SurfaceLayerStructure<EmptyContext> surfaceLayer = new SurfaceLayerStructure<>(0, 15, 15, path, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        //        (pos, ctx, random, instance) -> {

        //            ServerLevel level = ctx.level().getLevel();
        //    DensityFunction.SinglePointContext singlePoint = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
        //    double noise = level.getChunkSource().randomState().sampler().erosion().compute(singlePoint);
        //    BlockState state = ctx.level().getBlockState(pos);
        //    return noise > -0.0025 && noise < 0.0025 &&
        //            state.is(BlockTags.DIRT);
        //        });
        //
        //pathDef.addStructure(surfaceLayer);
        //addDefinition(pathDef);

        StructureDefinition bridgeDef = new StructureDefinition();
        structures.add(bridgeDef);

        BridgeStructure bridge = new BridgeStructure(0);
        bridge.addRule(((ctx, random, structure) -> {
            ChunkPos chunkPos = new ChunkPos(ctx.origin());
            return chunkPos.x == 0;
        }));
        bridgeDef.addStructure(bridge);

        StructureDefinition castleStructure = new StructureDefinition();
        structures.add(castleStructure);

        WallStructure wallStructure = new WallStructure(0);
        castleStructure.addStructure(wallStructure);
    }

    public void addDefinition(StructureDefinition definition) {
        structures.add(definition);
    }

    //Debug method
    public void clearDefinitions() {
        structures.clear();
    }

    public List<StructureDefinition> getStructures() {
        return structures;
    }

    public int nextId() {
        return nextId++;
    }
}
