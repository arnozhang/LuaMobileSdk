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
import com.alibaba.fastjson.JSONObject;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;

public class JSONObjectBridge extends LuaPlainDataBridge<JSONObject> {

    private static final String BRIDGE_CLASS_NAME = "";


    @Override
    public String getBridgeName() {
        return "JSONObject";
    }

    @Override
    public Class<JSONObject> getDataClass() {
        return JSONObject.class;
    }

    @NonNull
    @Override
    public JSONObject createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        if (args.atLeastHas(1) && args.isString(0)) {
            return JSONObject.parseObject(args.toString(0));
        }

        return new JSONObject();
    }

    @Override
    public boolean accessClassField() {
        return false;
    }

    @Override
    public int length(JSONObject object) {
        return object != null ? object.size() : 0;
    }

    @Override
    public Object getData(JSONObject object, String name) {
        return object != null ? object.get(name) : null;
    }

    @Override
    public void setData(JSONObject object, String name, Object value) {
        if (object != null) {
            object.put(name, value);
        }
    }
}
