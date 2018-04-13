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
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;

import java.util.ArrayList;
import java.util.List;

public class ListBridge extends LuaPlainDataBridge<List> {

    private static final String TAG = "ListBridge";


    @Override
    public Class<List> getDataClass() {
        return List.class;
    }

    @Override
    public String getBridgeName() {
        return "List";
    }

    @Override
    public int length(List object) {
        return object.size();
    }

    @NonNull
    @Override
    public List createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        int count = args.toIntFixed(0);
        return new ArrayList<Object>(count);
    }

    @Override
    public Object getData(List object, String name) {
        if (TextUtils.equals(name, "size")) {
            // array.size   -> JSONArray.size()
            //
            return length(object);
        }

        int index = parseIndex(name);
        if (index >= 0 && index < object.size()) {
            return object.get(index);
        }

        return null;
    }

    @Override
    public void setData(List object, String name, Object data) {
        int index = parseIndex(name);
        if (index >= 0 && index < object.size()) {
            object.set(index, data);
        }
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
