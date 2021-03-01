package com.megacrit.cardcrawl.android.mods.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class Types {
    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Class<Map> mapClass, Type[] types) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return types;
            }

            @Override
            public Type getRawType() {
                return mapClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
