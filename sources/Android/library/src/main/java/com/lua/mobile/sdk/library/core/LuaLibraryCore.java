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

package com.lua.mobile.sdk.library.core;

import android.content.Context;
import android.support.annotation.NonNull;
import com.lua.mobile.sdk.core.LuaCore;
import com.lua.mobile.sdk.library.clazz.JSONArrayBridge;
import com.lua.mobile.sdk.library.clazz.JSONObjectBridge;
import com.lua.mobile.sdk.library.clazz.ListBridge;
import com.lua.mobile.sdk.library.clazz.MapBridge;
import com.lua.mobile.sdk.library.module.CommonModule;
import com.lua.mobile.sdk.library.module.LogModule;
import com.lua.mobile.sdk.library.module.TasksModule;
import com.lua.mobile.sdk.library.module.ToastModule;

public class LuaLibraryCore extends LuaCore {

    public LuaLibraryCore(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void registerBuildInModules() {
        super.registerBuildInModules();

        mScriptEngine.registerModule(new LogModule());
        mScriptEngine.registerModule(new TasksModule());
        mScriptEngine.registerModule(new ToastModule(mContext));
        mScriptEngine.registerModule(new CommonModule(mContext));
    }

    @Override
    protected void registerBuildInClasses() {
        super.registerBuildInClasses();

        mScriptEngine.registerPlainDataClass(new MapBridge());
        mScriptEngine.registerPlainDataClass(new ListBridge());
        mScriptEngine.registerPlainDataClass(new JSONArrayBridge());
        mScriptEngine.registerPlainDataClass(new JSONObjectBridge());
    }
}
