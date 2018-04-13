/**
 * Android LuaMobileSdk for Android framework project.
 *
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lua.mobile.sdk.engine.script;

public class LuaType {

    public static final int NONE = -1;

    public static final int NIL = 0;
    public static final int BOOLEAN = 1;
    public static final int LIGHTUSERDATA = 2;  // Lua
    public static final int NUMBER = 3;
    public static final int STRING = 4;
    public static final int TABLE = 5;
    public static final int FUNCTION = 6;
    public static final int USERDATA = 7;       // Lua
    public static final int THREAD = 8;         // Lua


    /**
     * Bridged types.
     */
    public static final int JAVA_OBJECT = 1001;
    public static final int BRIDGE_FUNCTION = 1002;


    public static boolean isNull(int type) {
        return type == NONE || type == NIL;
    }

    public static boolean isBoolean(int type) {
        return type == BOOLEAN;
    }

    public static boolean isNumber(int type) {
        return type == NUMBER;
    }

    public static boolean isString(int type) {
        return type == STRING;
    }

    public static boolean isTable(int type) {
        return type == TABLE;
    }

    public static boolean isFunction(int type) {
        return type == FUNCTION;
    }

    public static boolean isUserData(int type) {
        return type == USERDATA;
    }

    public static boolean isJavaObject(int type) {
        return type == JAVA_OBJECT;
    }

    public static boolean isBridgedFunction(int type) {
        return type == BRIDGE_FUNCTION;
    }


    public static Number convertLuaNumber(Object obj, Class<?> retType) {
        if (obj == null) {
            return 0;
        }

        Double value = (Double) obj;
        if (retType.isPrimitive()) {
            if (retType == Integer.TYPE) {
                return value.intValue();
            } else if (retType == Long.TYPE) {
                return value.longValue();
            } else if (retType == Float.TYPE) {
                return value.floatValue();
            } else if (retType == Double.TYPE) {
                return value;
            } else if (retType == Byte.TYPE) {
                return value.byteValue();
            } else if (retType == Short.TYPE) {
                return value.shortValue();
            }
        } else if (retType.isAssignableFrom(Number.class)) {
            if (retType.isAssignableFrom(Integer.class)) {
                return value.intValue();
            } else if (retType.isAssignableFrom(Long.class)) {
                return value.longValue();
            } else if (retType.isAssignableFrom(Float.class)) {
                return value.floatValue();
            } else if (retType.isAssignableFrom(Double.class)) {
                return value;
            } else if (retType.isAssignableFrom(Byte.class)) {
                return value.byteValue();
            } else if (retType.isAssignableFrom(Short.class)) {
                return value.shortValue();
            }
        }

        return 0;
    }
}
