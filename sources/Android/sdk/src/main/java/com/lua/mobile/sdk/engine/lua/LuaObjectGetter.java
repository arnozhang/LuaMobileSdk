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

package com.lua.mobile.sdk.engine.lua;

import android.support.annotation.NonNull;
import android.util.Log;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.script.LuaType;
import com.lua.mobile.sdk.engine.script.LuaTypeChecker;

public abstract class LuaObjectGetter extends LuaTypeChecker {

    private static final String TAG = "LuaObjectGetter";


    public abstract LuaState getLuaState();

    public abstract LuaScriptEngine getScriptEngine();

    /**
     * get values directly
     *
     * 直接获取,可能失败. 子类需重写它们.
     */
    public abstract boolean toBooleanDirectly(int index);

    public abstract double toNumberDirectly(int index);

    public abstract byte[] toUserDataDirectly(int index);

    public abstract String toStringDirectly(int index);

    public abstract Object toJavaObjectDirectly(int index);

    public abstract LuaBridgedFunction toBridgedFunctionDirectly(int index);

    /**
     * get values.
     */
    public boolean toBoolean(int index) {
        return toBooleanDirectly(index);
    }

    public double toNumber(int index) {
        return toNumberDirectly(index);
    }

    public byte[] toUserData(int index) {
        return toUserDataDirectly(index);
    }

    public String toString(int index) {
        return toStringDirectly(index);
    }

    public Object toJavaObject(int index) {
        return toJavaObjectDirectly(index);
    }

    public LuaBridgedFunction toBridgedFunction(int index) {
        return toBridgedFunctionDirectly(index);
    }

    public boolean toBoolean(int index, boolean opt) {
        if (isBoolean(index)) {
            return toBoolean(index);
        }

        return opt;
    }

    public double toNumber(int index, double opt) {
        if (isNumber(index)) {
            return toNumber(index);
        }

        return opt;
    }

    public byte[] toUserData(int index, byte[] opt) {
        if (isUserData(index)) {
            return toUserData(index);
        }

        return opt;
    }

    public String toString(int index, String opt) {
        if (isString(index)) {
            return toString(index);
        }

        return opt;
    }

    public Object toJavaObject(int index, Object opt) {
        if (isJavaObject(index)) {
            return toJavaObject(index);
        }

        return opt;
    }

    public LuaBridgedFunction toBridgedFunction(int index, LuaBridgedFunction opt) {
        if (isBridgedFunction(index)) {
            return toBridgedFunction(index);
        }

        return opt;
    }


    /**
     * with fixed value.
     */
    public boolean toBooleanFixed(int index) {
        return toBooleanFixed(index, false);
    }

    public boolean toBooleanFixed(int index, boolean opt) {
        final int type = getType(index);
        if (type == LuaType.BOOLEAN) {
            return toBoolean(index);
        } else if (type == LuaType.NUMBER) {
            return toNumber(index) != 0;
        }

        return opt;
    }

    public double toNumberFixed(int index) {
        return toNumberFixed(index, 0);
    }

    public double toNumberFixed(int index, double opt) {
        final int type = getType(index);
        if (type == LuaType.NUMBER) {
            return toNumber(index);
        } else if (type == LuaType.STRING) {
            String str = toString(index);
            try {
                return Double.parseDouble(str);
            } catch (Exception e) {
                Log.e(TAG, String.format("Cannot convert to number: %s", str));
            }
        } else if (type == LuaType.BOOLEAN) {
            return toBoolean(index) ? 1 : 0;
        }

        return opt;
    }

    public byte[] toUserDataFixed(int index) {
        return toUserDataFixed(index, null);
    }

    public byte[] toUserDataFixed(int index, byte[] opt) {
        final int type = getType(index);
        if (type == LuaType.USERDATA) {
            return toUserData(index);
        }

        return opt;
    }

    public String toStringFixed(int index) {
        return toStringFixed(index, null);
    }

    public String toStringFixed(int index, String opt) {
        final int type = getType(index);
        if (LuaType.isNull(type)) {
            return opt;
        } else if (type == LuaType.STRING) {
            return toString(index);
        } else if (type == LuaType.NUMBER) {
            return String.valueOf(toNumber(index));
        } else if (type == LuaType.BOOLEAN) {
            return toBoolean(index) ? "TRUE" : "FALSE";
        }

        return opt;
    }

    /**
     * force convert to object
     *
     * null                     null
     * BOOL                     Boolean
     * int/long/float/double    Double
     * string                   String
     * object                   Object
     */
    public Object toJavaObjectFixed(int index) {
        return toJavaObjectFixed(index, null);
    }

    public Object toJavaObjectFixed(int index, Object opt) {
        final int type = getType(index);
        if (LuaType.isNull(type)) {
            return null;
        } else if (type == LuaType.NUMBER) {
            return toNumber(index);
        } else if (type == LuaType.STRING) {
            return toString(index);
        } else if (type == LuaType.JAVA_OBJECT) {
            return toJavaObject(index);
        } else if (type == LuaType.BRIDGE_FUNCTION) {
            return toBridgedFunction(index);
        } else if (type == LuaType.BOOLEAN) {
            return toBoolean(index);
        } else if (type == LuaType.FUNCTION || type == LuaType.TABLE || type == LuaType.USERDATA) {
            return getLuaObject(index);
        }

        return opt;
    }

    public LuaBridgedFunction toBridgedFunctionFixed(int index) {
        return toBridgedFunctionFixed(index, null);
    }

    public LuaBridgedFunction toBridgedFunctionFixed(int index, LuaBridgedFunction opt) {
        final int type = getType(index);
        if (type == LuaType.BRIDGE_FUNCTION) {
            return toBridgedFunction(index);
        }

        return opt;
    }

    /**
     * other built-in raw type
     */
    public int toIntFixed(int index) {
        return toIntFixed(index, 0);
    }

    public int toIntFixed(int index, int opt) {
        return (int) toNumberFixed(index, opt);
    }

    public long toLongFixed(int index) {
        return toLongFixed(index, 0);
    }

    public long toLongFixed(int index, long opt) {
        return (long) toNumberFixed(index, opt);
    }

    public float toFloatFixed(int index) {
        return toFloatFixed(index, 0);
    }

    public float toFloatFixed(int index, float opt) {
        return (float) toNumberFixed(index, opt);
    }

    /**
     * LuaObject
     */
    public LuaObject getLuaObject(int index) {
        return new LuaObject(getScriptEngine(), index);
    }

    public LuaObject getLuaObject(@NonNull String globalVarName) {
        return new LuaObject(getScriptEngine(), globalVarName);
    }

    public LuaObject getLuaObject(LuaObject object, @NonNull String fieldVarName) throws Exception {
        if (object.getScriptEngine() != getScriptEngine()) {
            throw new Exception("Cannot access field in not-same Lua state!");
        }

        return new LuaObject((LuaObject) object, fieldVarName);
    }

    public LuaObject getLuaObject(LuaObject object, int index) throws Exception {
        if (object.getScriptEngine() != getScriptEngine()) {
            throw new Exception("Cannot access field in not-same Lua state!");
        }

        return new LuaObject((LuaObject) object, index);
    }

    public LuaObject getLuaObject(LuaObject object, @NonNull LuaObject field) throws Exception {
        if (object.getScriptEngine() != getScriptEngine()) {
            throw new Exception("Cannot access field in not-same Lua state!");
        }

        return new LuaObject((LuaObject) object, (LuaObject) field);
    }
}
