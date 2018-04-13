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

package com.lua.mobile.sdk.engine.args;

import com.lua.mobile.sdk.engine.LuaScriptEngine;

public class LuaPlainDataBridgeArgs extends LuaBridgeArgs {

    public LuaPlainDataBridgeArgs(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
    }

    @Override
    protected int calcArgsCount(int argCount) {
        return argCount;
    }

    @Override
    public int convertIndex(int i) {
        return i + 1;
    }
}
