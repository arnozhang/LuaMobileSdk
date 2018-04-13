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

import android.content.Context;
import android.widget.Toast;
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;

public class ToastModule extends LuaModuleBridge {

    private Context mContext;


    public ToastModule(Context context) {
        mContext = context;
    }

    @Override
    public String getBridgeName() {
        return "Toast";
    }

    @LuaMethod
    public void show(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @LuaMethod
    public void showLong(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
