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
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;

public class CommonModule extends LuaModuleBridge {

    private static final String TAG = "CommonUtilsModule";

    private Context mContext;


    public static final int platform_android = 1;
    public static final int platform_ios = 2;


    public static final boolean isAndroid = true;
    public static final boolean isiOS = false;
    public static final int platform = platform_android;


    public CommonModule(Context context) {
        mContext = context;
    }

    @Override
    public String getBridgeName() {
        return "Common";
    }

    @LuaMethod
    public int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @LuaMethod
    public int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }
}
