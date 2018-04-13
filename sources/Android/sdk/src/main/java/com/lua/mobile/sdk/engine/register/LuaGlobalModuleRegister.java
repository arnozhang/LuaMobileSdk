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
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;
import com.lua.mobile.sdk.engine.lua.LuaState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LuaGlobalModuleRegister extends AbstractRegister {

    private LuaState mLuaState;
    private List<LuaModuleBridge> mModuleBridges = new ArrayList<>();


    public LuaGlobalModuleRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
        mLuaState = scriptEngine.getLuaState();
    }

    @Override
    public void destroy() {
        super.destroy();

        for (LuaModuleBridge module : mModuleBridges) {
            module.destroy();
        }
    }

    /**
     * _G.ModuleName = module
     */
    public void registerModule(@NonNull final LuaModuleBridge module) {
        mModuleBridges.add(module);

        // temp = {}
        mLuaState.newTable();

        // _G.ModuleName = temp
        mLuaState.pushIndexValueToTop(-1);
        mLuaState.setGlobal(module.getBridgeName());

        LuaScriptEngine engine = (LuaScriptEngine) mScriptEngine;

        // register public final static vars.
        Field[] fields = module.getClass().getDeclaredFields();
        LuaAbstractClassRegister.registerStaticVariableInternal(engine, fields, module);

        // register public methods.
        Method[] methods = module.getClass().getDeclaredMethods();
        LuaAbstractClassRegister.registerBridgedFunctionInternal(engine, methods, module);

        // pop temp
        mLuaState.pop(1);
    }
}
