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

package com.lua.mobile.sdk.test.base;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import com.lua.mobile.sdk.core.LuaCore;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.lua.LuaState;

public class BaseTest {

    protected static final String TAG = "LuaTest";


    protected LuaCore mCore;
    protected LuaState mLuaState;
    protected LuaScriptEngine mEngine;


    public BaseTest() {
        mCore = createLuaCore(getContext());
        mEngine = mCore.getScriptEngine();
        mLuaState = mEngine.getLuaState();

        initialize();
    }

    protected void initialize() {
        mEngine.registerVariable("TAG", TAG);
    }

    protected LuaCore createLuaCore(Context context) {
        return new LuaCore(context);
    }

    protected Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
