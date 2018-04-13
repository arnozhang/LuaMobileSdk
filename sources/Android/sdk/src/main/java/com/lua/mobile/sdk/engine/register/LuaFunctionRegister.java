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
import android.text.TextUtils;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.lua.LuaState;

public class LuaFunctionRegister extends AbstractRegister {

    protected LuaState mLuaState;


    public LuaFunctionRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
        mLuaState = scriptEngine.getLuaState();
    }

    /**
     * _G.name = function
     */
    public void registerFunction(
            @NonNull String name, @NonNull LuaBridgedFunction function) {

        function.setFunctionName(name);
        mLuaState.pushBridgedFunction(function);
        mLuaState.setGlobal(name);
    }

    public boolean registerModuleFunction(
            String moduleName, @NonNull String name, LuaBridgedFunction function) {

        if (TextUtils.isEmpty(moduleName)) {
            registerFunction(name, function);
            return true;
        }

        mLuaState.getGlobal(moduleName);
        if (mLuaState.isNull(-1)) {
            // new module
            mLuaState.pop(1);
            mLuaState.newTable();

            // _T.moduleName = temp
            mLuaState.pushIndexValueToTop(-1);
            mLuaState.setGlobal(moduleName);
        } else if (!mLuaState.isTable(-1)) {
            return false;
        }

        // _G.moduleName.name = function
        function.setFunctionName(moduleName + "." + name);
        mLuaState.pushBridgedFunction(function);
        mLuaState.setField(-2, name);

        mLuaState.pop(1);
        return true;
    }
}
