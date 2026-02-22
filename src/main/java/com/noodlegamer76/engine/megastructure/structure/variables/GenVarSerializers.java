package com.noodlegamer76.engine.megastructure.structure.variables;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class GenVarSerializers {
    public static final GenVarSerializer<Integer> INT = GenVarSerializer.create(CompoundTag::putInt, CompoundTag::getInt);
    public static final GenVarSerializer<Double> DOUBLE = GenVarSerializer.create(CompoundTag::putDouble, CompoundTag::getDouble);
    public static final GenVarSerializer<Float> FLOAT = GenVarSerializer.create(CompoundTag::putFloat, CompoundTag::getFloat);
    public static final GenVarSerializer<Boolean> BOOLEAN = GenVarSerializer.create(CompoundTag::putBoolean, CompoundTag::getBoolean);
    public static final GenVarSerializer<String> STRING = GenVarSerializer.create(CompoundTag::putString, CompoundTag::getString);
    public static final GenVarSerializer<byte[]> BYTE_ARRAY = GenVarSerializer.create(CompoundTag::putByteArray, CompoundTag::getByteArray);
    public static final GenVarSerializer<Long> LONG = GenVarSerializer.create(CompoundTag::putLong, CompoundTag::getLong);
    public static final GenVarSerializer<Vec3> VEC3 = GenVarSerializer.create(
            ((tag, name, value) -> {
                tag.putDouble(name + "_x", value.x);
                tag.putDouble(name + "_y", value.y);
                tag.putDouble(name + "_z", value.z);
            }),
            ((tag, name) -> {
                return new Vec3(tag.getDouble(name + "_x"), tag.getDouble(name + "_y"), tag.getDouble(name + "_z"));
            })
    );
    public static final GenVarSerializer<ResourceLocation> RESOURCE_LOCATION = GenVarSerializer.create(
            ((tag, name, value) -> tag.putString(name, value.toString())),
            ((tag, name) -> ResourceLocation.tryParse(tag.getString(name)))
    );
    public static final GenVarSerializer<BlockPos> BLOCK_POS = GenVarSerializer.create(
            ((tag, name, value) -> {
                tag.putInt(name + "_x", value.getX());
                tag.putInt(name + "_y", value.getY());
                tag.putInt(name + "_z", value.getZ());
            }),
            ((tag, name) -> new BlockPos(tag.getInt(name + "_x"), tag.getInt(name + "_y"), tag.getInt(name + "_z")))
    );
}
