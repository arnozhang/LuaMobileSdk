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

public abstract class LuaTypeChecker {

    public boolean isNull(int index) {
        return LuaType.isNull(getType(index));
    }

    public boolean isBoolean(int index) {
        return LuaType.isBoolean(getType(index));
    }

    public boolean isNumber(int index) {
        return LuaType.isNumber(getType(index));
    }

    public boolean isString(int index) {
        return LuaType.isString(getType(index));
    }

    public boolean isTable(int index) {
        return LuaType.isTable(getType(index));
    }

    public boolean isFunction(int index) {
        return LuaType.isFunction(getType(index));
    }

    public boolean isUserData(int index) {
        return LuaType.isUserData(getType(index));
    }

    public boolean isJavaObject(int index) {
        return LuaType.isJavaObject(getType(index));
    }

    public boolean isBridgedFunction(int index) {
        return LuaType.isBridgedFunction(getType(index));
    }

    public abstract boolean isInteger(int index);

    public abstract int getType(int index);
}
