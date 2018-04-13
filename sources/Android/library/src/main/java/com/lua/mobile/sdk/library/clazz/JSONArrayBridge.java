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
import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;
import com.lua.mobile.sdk.engine.LuaScriptEngine;

public class JSONArrayBridge extends LuaPlainDataBridge<JSONArray> {

    private static final String TAG = "JSONArrayBridge";


    @Override
    public String getBridgeName() {
        return "JSONArray";
    }

    @Override
    public Class<JSONArray> getDataClass() {
        return JSONArray.class;
    }

    @NonNull
    @Override
    public JSONArray createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        if (args.atLeastHas(1) && args.isString(0)) {
            return JSONArray.parseArray(args.toString(0));
        }

        return new JSONArray();
    }

    @Override
    public boolean accessClassField() {
        return false;
    }

    @Override
    public int length(JSONArray object) {
        return object != null ? object.size() : 0;
    }

    @Override
    public Object getData(JSONArray array, String name) {
        if (TextUtils.equals(name, "size")) {
            // array.size   -> JSONArray.size()
            //
            return length(array);
        }

        if (array == null) {
            return null;
        }

        int index = parseIndex(name);
        if (index < 0 || index >= array.size()) {
            return null;
        }

        return array.get(index);
    }

    @Override
    public void setData(JSONArray array, String name, Object value) {
        if (array == null) {
            return;
        }

        int index = parseIndex(name);
        if (index < 0 || index >= array.size()) {
            return;
        }

        array.set(index, value);
    }

    private static int parseIndex(String name) {
        try {
            return (int) Double.parseDouble(name);
        } catch (Exception e) {
            Log.e(TAG, "parseIndex", e);
        }

        return -1;
    }
}
