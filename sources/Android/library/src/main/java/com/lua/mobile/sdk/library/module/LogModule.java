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

package com.lua.mobile.sdk.library.module;

import android.util.Log;
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;

public class LogModule extends LuaModuleBridge {

    private static final String TAG = "ArkLogModule";
    private String[] log = new String[2];


    @Override
    public String getBridgeName() {
        return "Log";
    }

    @LuaMethod
    public void v(Object tag, Object message) {
        normalizeLog(tag, message);
        Log.v(log[0], log[1]);
    }

    @LuaMethod
    public void d(Object tag, Object message) {
        normalizeLog(tag, message);
        Log.d(log[0], log[1]);
    }

    @LuaMethod
    public void i(Object tag, Object message) {
        normalizeLog(tag, message);
        Log.i(log[0], log[1]);
    }

    @LuaMethod
    public void w(Object tag, Object message) {
        normalizeLog(tag, message);
        Log.w(log[0], log[1]);
    }

    @LuaMethod
    public void e(Object tag, Object message) {
        normalizeLog(tag, message);
        Log.e(log[0], log[1]);
    }

    private void normalizeLog(Object tag, Object message) {
        if (message != null) {
            if (tag != null) {
                log[0] = String.valueOf(tag);
            } else {
                log[0] = TAG;
            }

            log[1] = String.valueOf(message);
        } else {
            log[0] = TAG;
            log[1] = String.valueOf(tag);
        }
    }
}
