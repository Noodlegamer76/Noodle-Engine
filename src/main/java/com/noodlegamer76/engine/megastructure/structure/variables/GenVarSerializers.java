package com.noodlegamer76.engine.megastructure.structure.variables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class GenVarSerializers {
    public static final GenVarSerializer<Object> ANY_OBJECT = new GenVarSerializer<>() {
        @Override
        public JsonElement serialize(Object value) {
            throw new UnsupportedOperationException("Local Object vars are not serialized");
        }

        @Override
        public Object deserialize(JsonElement element) {
            throw new UnsupportedOperationException("Local Object vars are not serialized");
        }

        @Override
        public Class<Object> getHandledClass() {
            return Object.class;
        }
    };

    public static final GenVarSerializer<Integer> INT = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Integer value) { return new JsonPrimitive(value); }
        @Override public Integer deserialize(JsonElement element) { return element.getAsInt(); }
        @Override public Class<Integer> getHandledClass() { return Integer.class; }
    };

    public static final GenVarSerializer<Double> DOUBLE = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Double value) { return new JsonPrimitive(value); }
        @Override public Double deserialize(JsonElement element) { return element.getAsDouble(); }
        @Override public Class<Double> getHandledClass() { return Double.class; }
    };

    public static final GenVarSerializer<Float> FLOAT = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Float value) { return new JsonPrimitive(value); }
        @Override public Float deserialize(JsonElement element) { return element.getAsFloat(); }
        @Override public Class<Float> getHandledClass() { return Float.class; }
    };

    public static final GenVarSerializer<Boolean> BOOLEAN = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Boolean value) { return new JsonPrimitive(value); }
        @Override public Boolean deserialize(JsonElement element) { return element.getAsBoolean(); }
        @Override public Class<Boolean> getHandledClass() { return Boolean.class; }
    };

    public static final GenVarSerializer<String> STRING = new GenVarSerializer<>() {
        @Override public JsonElement serialize(String value) { return new JsonPrimitive(value); }
        @Override public String deserialize(JsonElement element) { return element.getAsString(); }
        @Override public Class<String> getHandledClass() { return String.class; }
    };

    public static final GenVarSerializer<Long> LONG = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Long value) { return new JsonPrimitive(value); }
        @Override public Long deserialize(JsonElement element) { return element.getAsLong(); }
        @Override public Class<Long> getHandledClass() { return Long.class; }
    };

    public static final GenVarSerializer<Vec3> VEC3 = new GenVarSerializer<>() {
        @Override
        public JsonElement serialize(Vec3 value) {
            JsonObject obj = new JsonObject();
            obj.addProperty("x", value.x);
            obj.addProperty("y", value.y);
            obj.addProperty("z", value.z);
            return obj;
        }

        @Override
        public Vec3 deserialize(JsonElement element) {
            JsonObject obj = element.getAsJsonObject();
            return new Vec3(
                    obj.get("x").getAsDouble(),
                    obj.get("y").getAsDouble(),
                    obj.get("z").getAsDouble()
            );
        }

        @Override
        public Class<Vec3> getHandledClass() { return Vec3.class; }
    };

    public static final GenVarSerializer<ResourceLocation> RESOURCE_LOCATION = new GenVarSerializer<>() {
        @Override public JsonElement serialize(ResourceLocation value) { return new JsonPrimitive(value.toString()); }
        @Override public ResourceLocation deserialize(JsonElement element) { return ResourceLocation.tryParse(element.getAsString()); }
        @Override public Class<ResourceLocation> getHandledClass() { return ResourceLocation.class; }
    };

    public static final GenVarSerializer<BlockPos> BLOCK_POS = new GenVarSerializer<>() {
        @Override
        public JsonElement serialize(BlockPos value) {
            JsonObject obj = new JsonObject();
            obj.addProperty("x", value.getX());
            obj.addProperty("y", value.getY());
            obj.addProperty("z", value.getZ());
            return obj;
        }

        @Override
        public BlockPos deserialize(JsonElement element) {
            JsonObject obj = element.getAsJsonObject();
            return new BlockPos(
                    obj.get("x").getAsInt(),
                    obj.get("y").getAsInt(),
                    obj.get("z").getAsInt()
            );
        }

        @Override
        public Class<BlockPos> getHandledClass() { return BlockPos.class; }
    };

    public static final GenVarSerializer<Direction> DIRECTION = new GenVarSerializer<>() {
        @Override public JsonElement serialize(Direction value) { return new JsonPrimitive(value.name()); }
        @Override public Direction deserialize(JsonElement element) { return Direction.valueOf(element.getAsString()); }
        @Override public Class<Direction> getHandledClass() { return Direction.class; }
    };
}