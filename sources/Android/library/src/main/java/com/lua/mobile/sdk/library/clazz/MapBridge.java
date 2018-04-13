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

package com.lua.mobile.sdk.library.clazz;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;

import java.util.HashMap;
import java.util.Map;

public class MapBridge extends LuaPlainDataBridge<Map> {

    @Override
    public Class<Map> getDataClass() {
        return Map.class;
    }

    @Override
    public String getBridgeName() {
        return "Map";
    }

    @Override
    public int length(Map object) {
        return object.size();
    }

    @NonNull
    @Override
    public Map createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        return new HashMap();
    }

    @Override
    public Object getData(Map object, String name) {
        if (TextUtils.equals(name, "size")) {
            // array.size   -> JSONArray.size()
            //
            return length(object);
        }

        return object.get(name);
    }

    @Override
    public void setData(Map object, String name, Object data) {
        object.put(name, data);
    }
}
