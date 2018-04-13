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

package com.lua.mobile.sdk.engine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.bridge.LuaClassBridge;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;
import com.lua.mobile.sdk.engine.lua.LuaObject;
import com.lua.mobile.sdk.engine.lua.LuaState;
import com.lua.mobile.sdk.engine.register.LuaConstVariableRegister;
import com.lua.mobile.sdk.engine.register.LuaFunctionRegister;
import com.lua.mobile.sdk.engine.register.LuaGlobalClassRegister;
import com.lua.mobile.sdk.engine.register.LuaGlobalModuleRegister;
import com.lua.mobile.sdk.engine.register.LuaPlainDataClassRegister;
import com.lua.mobile.sdk.engine.register.RegisterClassManager;

import java.util.HashMap;
import java.util.Map;

public class LuaScriptEngine implements JavaObjectPushInterceptor, RegisterClassManager {

    protected Context mContext;
    protected LuaState mLuaState;
    protected LuaFunctionRegister mFunctionRegister;
    protected LuaConstVariableRegister mConstVariableRegister;
    protected LuaGlobalModuleRegister mGlobalModuleRegister;
    protected LuaGlobalClassRegister mGlobalClassRegister;
    protected LuaPlainDataClassRegister mPlainDataClassRegister;
    protected Map<Class<?>, String> mBridgedClassNames = new HashMap<>();


    public void initialize(Context context) {
        mContext = context;
        mLuaState = new LuaState(this);
        mLuaState.setPushInterceptor(this);

        createRegisters();
    }

    public void close() {
        mFunctionRegister.destroy();
        mConstVariableRegister.destroy();
        mGlobalClassRegister.destroy();
        mGlobalModuleRegister.destroy();
        mPlainDataClassRegister.destroy();

        mLuaState.close();
    }

    protected void createRegisters() {
        mFunctionRegister = new LuaFunctionRegister(this);
        mConstVariableRegister = new LuaConstVariableRegister(this);
        mGlobalModuleRegister = new LuaGlobalModuleRegister(this);
        mGlobalClassRegister = new LuaGlobalClassRegister(this);
        mPlainDataClassRegister = new LuaPlainDataClassRegister(this);
    }

    public Context getContext() {
        return mContext;
    }

    public LuaState getLuaState() {
        return mLuaState;
    }

    public RegisterClassManager getRegisterClassManager() {
        return this;
    }

    @Override
    public void addBridgedClass(Class<?> clazz, String bridgedClassName) {
        mBridgedClassNames.put(clazz, bridgedClassName);
    }

    @Override
    public String getBridgedClassName(Class<?> clazz) {
        String className = mBridgedClassNames.get(clazz);
        if (!TextUtils.isEmpty(className)) {
            return className;
        }

        for (Map.Entry<Class<?>, String> entry : mBridgedClassNames.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                mBridgedClassNames.put(clazz, entry.getValue());
                return entry.getValue();
            }
        }

        return null;
    }

    public void registerVariable(@NonNull String name, Object object) {
        mConstVariableRegister.registerVariable(name, object);
    }

    public boolean registerModuleVariable(
            String moduleName, @NonNull String varName, Object object) {

        return mConstVariableRegister.registerModuleVariable(moduleName, varName, object);
    }

    public void registerFunction(
            @NonNull String name, @NonNull LuaBridgedFunction function) {

        mFunctionRegister.registerFunction(name, function);
    }

    public boolean registerModuleFunction(
            String moduleName, @NonNull String name, LuaBridgedFunction function) {
        return mFunctionRegister.registerModuleFunction(moduleName, name, function);
    }

    public void registerModule(@NonNull final LuaModuleBridge module) {
        mGlobalModuleRegister.registerModule(module);
    }

    public <T> void registerPlainDataClass(@NonNull final LuaPlainDataBridge<T> bridge) {
        mPlainDataClassRegister.registerPlainDataClass(bridge);
    }

    public <T> void registerClass(@NonNull final LuaClassBridge<T> bridge) {
        mGlobalClassRegister.registerClass(bridge);
    }

    @Override
    public boolean interceptPush(LuaScriptEngine scriptEngine, Object object) {
        String metaClass = getBridgedClassName(object.getClass());
        if (!TextUtils.isEmpty(metaClass)) {
            interceptPushInternal(object, metaClass);
            return true;
        }

        return false;
    }

    public void interceptPushInternal(Object object, String metaClass) {
        mLuaState.pushJavaObjectWithMeta(object, metaClass);
    }

    public LuaObject getModuleFunction(String moduleName, String functionName) {
        mLuaState.getGlobal(moduleName);
        if (!mLuaState.isTable(-1)) {
            mLuaState.pop(1);
            return null;
        }

        mLuaState.getFiled(-1, functionName);
        LuaObject function = mLuaState.getLuaObject(-1);
        mLuaState.pop(2);

        return function;
    }

    public int doScriptFile(@NonNull String file) {
        return mLuaState.doFile(file);
    }

    public int doScriptString(@NonNull String str) {
        return mLuaState.doString(str);
    }

    public int doScriptBuffer(byte[] script) {
        return mLuaState.doBuffer(script);
    }

    public int doScriptBuffer(@NonNull byte[] buffer, int length) {
        return mLuaState.doBuffer(buffer, length, "internal");
    }
}
