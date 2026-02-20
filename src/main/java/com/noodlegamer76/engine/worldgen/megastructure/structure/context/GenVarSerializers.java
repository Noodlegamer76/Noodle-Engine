package com.noodlegamer76.engine.worldgen.megastructure.structure.context;

import net.minecraft.nbt.CompoundTag;

public class GenVarSerializers {
    public static final GenVarSerializer<Integer> INT = GenVarSerializer.create(CompoundTag::putInt, CompoundTag::getInt);
    public static final GenVarSerializer<Double> DOUBLE = GenVarSerializer.create(CompoundTag::putDouble, CompoundTag::getDouble);
    public static final GenVarSerializer<Float> FLOAT = GenVarSerializer.create(CompoundTag::putFloat, CompoundTag::getFloat);
    public static final GenVarSerializer<Boolean> BOOLEAN = GenVarSerializer.create(CompoundTag::putBoolean, CompoundTag::getBoolean);
    public static final GenVarSerializer<String> STRING = GenVarSerializer.create(CompoundTag::putString, CompoundTag::getString);
    public static final GenVarSerializer<byte[]> BYTE_ARRAY = GenVarSerializer.create(CompoundTag::putByteArray, CompoundTag::getByteArray);
    public static final GenVarSerializer<Long> LONG = GenVarSerializer.create(CompoundTag::putLong, CompoundTag::getLong);
}
