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

package com.lua.mobile.sdk.engine.bridge;

import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;

public abstract class LuaBridgedFunction {

    protected LuaScriptEngine mScriptEngine;
    private String mFunctionName;


    public LuaBridgedFunction() {
        this(null);
    }

    public LuaBridgedFunction(LuaScriptEngine scriptEngine) {
        mScriptEngine = scriptEngine;
    }

    public LuaBridgedFunction setScriptEngine(LuaScriptEngine scriptEngine) {
        mScriptEngine = scriptEngine;
        return this;
    }

    public int invoke() throws Throwable {
        return invokeInternal(createBridgeArgs());
    }

    protected LuaBridgeArgs createBridgeArgs() {
        return new LuaBridgeArgs(mScriptEngine);
    }

    protected abstract int invokeInternal(LuaBridgeArgs args) throws Throwable;

    public void setFunctionName(String functionName) {
        mFunctionName = functionName;
    }

    public String getFunctionName() {
        return mFunctionName;
    }
}
