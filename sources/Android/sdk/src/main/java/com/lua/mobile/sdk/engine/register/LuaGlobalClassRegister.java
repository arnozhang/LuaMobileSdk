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

package com.lua.mobile.sdk.engine.register;

import android.support.annotation.NonNull;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaClassBridge;
import com.lua.mobile.sdk.engine.lua.LuaDeclares;

import java.lang.reflect.Method;

public class LuaGlobalClassRegister extends LuaAbstractClassRegister {

    public LuaGlobalClassRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
    }

    /**
     * _G.ClassName = bridge
     */
    public <T> void registerClass(@NonNull final LuaClassBridge<T> bridge) {
        super.registerClassPrepare(bridge);

        // meta.__index = index
        mLuaState.bindMetaClassFunctionIndex(-1);

        // register public methods.
        // meta.__java_member_funcs = {}
        mLuaState.newTable();

        Method[] methods = bridge.getClass().getMethods();
        LuaAbstractClassRegister.registerBridgedFunctionInternal(
                (LuaScriptEngine) mScriptEngine, methods, bridge);

        mLuaState.setField(-2, LuaDeclares.__java_member_funcs);

        mLuaState.pop(2);
    }
}
