package com.noodlegamer76.engine.megastructure.structure.variables;

import com.google.gson.JsonElement;

public interface GenVarSerializer<T> {
    JsonElement serialize(T value);
    T deserialize(JsonElement element);
    Class<T> getHandledClass();
}