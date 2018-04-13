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

import android.support.annotation.NonNull;
import android.util.Log;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;

import java.lang.reflect.Field;

public class ReflectPlainDataBridge<T> extends LuaPlainDataBridge<T> {

    private static final String TAG = "BridgeReflectPlainData";


    private Class<T> mDataClass;


    public ReflectPlainDataBridge(Class<T> clazz) {
        mDataClass = clazz;
    }

    @Override
    public Class<T> getDataClass() {
        return mDataClass;
    }

    @NonNull
    @Override
    public T createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        try {
            return mDataClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, String.format("Cannot new instance for: %s.",
                    mDataClass.getSimpleName()), e);
        }

        return null;
    }

    @Override
    public Object getData(T object, String name) {
        try {
            Field field = mDataClass.getField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            Log.e(TAG, String.format("Cannot get field: %s.%s. ",
                    mDataClass.getSimpleName(), name), e);
        }

        return null;
    }

    @Override
    public void setData(T object, String name, Object data) {
        try {
            Field field = mDataClass.getField(name);
            field.setAccessible(true);
            field.set(object, name);
        } catch (Exception e) {
            Log.e(TAG, String.format("Cannot set field: %s.%s = %s. ",
                    mDataClass.getSimpleName(), name, String.valueOf(data)), e);
        }
    }
}
