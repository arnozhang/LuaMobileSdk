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
import com.lua.mobile.sdk.engine.lua.LuaState;

public class LuaConstVariableRegister extends AbstractRegister {

    private LuaState mLuaState;


    public LuaConstVariableRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
        mLuaState = scriptEngine.getLuaState();
    }


    /**
     * _G.name = object
     */
    public void registerVariable(@NonNull String name, Object object) {
        mLuaState.push(object);
        mLuaState.setGlobal(name);
    }

    /**
     * _G.moduleName.varName = object
     */
    public boolean registerModuleVariable(
            String moduleName, @NonNull String varName, Object object) {

        if (TextUtils.isEmpty(moduleName)) {
            registerVariable(varName, object);
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

        // _G.moduleName.varName = object
        mLuaState.push(object);
        mLuaState.setField(-2, varName);

        mLuaState.pop(1);
        return true;
    }
}
