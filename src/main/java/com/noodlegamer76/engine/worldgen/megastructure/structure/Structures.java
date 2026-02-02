package com.noodlegamer76.engine.worldgen.megastructure.structure;

import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.SphereStructure;
import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.dungeon.SurfaceLayerStructure;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

//Temporary class for definitions
public class Structures {
    public static final List<StructureDefinition> STRUCTURES = new ArrayList<>();

    static {
        StructureDefinition def = new StructureDefinition();

        //SurfaceLayerStructure layer = new SurfaceLayerStructure(0, 1024, 1024, Blocks.MAGENTA_CONCRETE.defaultBlockState(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);

        //def.addStructure(layer);

        STRUCTURES.add(def);
    }
}
