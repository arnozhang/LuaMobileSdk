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

package com.lua.mobile.sdk.engine.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.args.LuaPlainDataBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.bridge.LuaClassBridge;
import com.lua.mobile.sdk.engine.bridge.LuaPlainDataBridge;
import com.lua.mobile.sdk.engine.lua.LuaDeclares;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LuaPlainDataClassRegister extends LuaAbstractClassRegister {

    private static final String TAG = "LuaPlainDataClassRegister";


    public LuaPlainDataClassRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
    }

    /**
     * _G.ClassName = bridge
     *
     * a = ClassName()
     * #a                       ->      a.meta.__len(a)
     * local value = a.field    ->      a.meta.__index(a, field)
     * a.field = 10             ->      a.meta.__newindex(a, field, 10)
     */
    public <T> void registerPlainDataClass(@NonNull final LuaPlainDataBridge<T> bridge) {
        super.registerClassPrepare(bridge);
        mLuaState.pop(2);
    }

    @Override
    protected <T> void bindMetaMethods(int tableIndex, @NonNull LuaClassBridge<T> bridge) {
        super.bindMetaMethods(tableIndex, bridge);

        LuaPlainDataBridge<T> plainData = (LuaPlainDataBridge<T>) bridge;
        String name = bridge.getBridgeName();

        // meta.__java_meta_funcs.__len = index
        LuaBridgedFunction len = createLenFunction(plainData);
        len.setFunctionName(name + "." + LuaDeclares.__len);
        mLuaState.pushBridgedFunction(len);
        mLuaState.setField(tableIndex, LuaDeclares.__len);

        // meta.__java_meta_funcs.__index = index
        LuaBridgedFunction index = createIndexFunction(plainData);
        index.setFunctionName(name + "." + LuaDeclares.__index);
        mLuaState.pushBridgedFunction(index);
        mLuaState.setField(tableIndex, LuaDeclares.__index);

        // meta.__java_meta_funcs.__newindex = newIndex
        LuaBridgedFunction newIndex = createNewIndexFunction(plainData);
        newIndex.setFunctionName(name + "." + LuaDeclares.__newindex);
        mLuaState.pushBridgedFunction(newIndex);
        mLuaState.setField(tableIndex, LuaDeclares.__newindex);
    }

    @Override
    protected void afterBindMetaMethods(int tableIndex) {
        super.afterBindMetaMethods(tableIndex);

        mLuaState.bindMetaLen(tableIndex);
        mLuaState.bindMetaIndex(tableIndex);
        mLuaState.bindMetaNewIndex(tableIndex);
    }

    private <T> LuaBridgedFunction createLenFunction(@NonNull final LuaPlainDataBridge<T> bridge) {
        return new LuaBridgedFunction(mScriptEngine) {

            @Override
            protected LuaBridgeArgs createBridgeArgs() {
                return new LuaPlainDataBridgeArgs(mScriptEngine);
            }

            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Throwable {
                if (!args.atLeastHas(1)) {
                    mLuaState.push(0);
                    return 1;
                }

                T object = (T) args.toJavaObject(0);
                mLuaState.push(bridge.length(object));

                return 1;
            }
        };
    }

    private <T> LuaBridgedFunction createIndexFunction(@NonNull final LuaPlainDataBridge<T> bridge) {
        return new LuaBridgedFunction(mScriptEngine) {

            @Override
            protected LuaBridgeArgs createBridgeArgs() {
                return new LuaPlainDataBridgeArgs(mScriptEngine);
            }

            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Throwable {
                if (!args.atLeastHas(2)) {
                    mLuaState.pushNil();
                    return 1;
                }

                String name = args.toString(1);
                if (TextUtils.isEmpty(name)) {
                    mLuaState.pushNil();
                    return 1;
                }

                Object data = null;
                T object = (T) args.toJavaObject(0);

                if (bridge.accessClassField()) {
                    Field field = null;
                    try {
                        field = bridge.getDataClass().getField(name);
                        if (field != null) {
                            int modifiers = field.getModifiers();
                            if (Modifier.isPublic(modifiers)) {
                                data = field.get(object);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, String.format("cannot access __index. class: %s, name: %s",
                                bridge.getDataClass().getSimpleName(), name), e);
                    }
                }

                if (data == null) {
                    data = bridge.getData(object, name);
                }

                if (data == null) {
                    mLuaState.pushNil();
                    return 1;
                }

                mLuaState.push(data);
                return 1;
            }
        };
    }

    private <T> LuaBridgedFunction createNewIndexFunction(@NonNull final LuaPlainDataBridge<T> bridge) {
        return new LuaBridgedFunction(mScriptEngine) {

            @Override
            protected LuaBridgeArgs createBridgeArgs() {
                return new LuaPlainDataBridgeArgs(mScriptEngine);
            }

            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Throwable {
                if (!args.atLeastHas(2)) {
                    return 0;
                }

                String name = args.toString(1);
                if (TextUtils.isEmpty(name)) {
                    mLuaState.pushNil();
                    return 1;
                }

                T object = (T) args.toJavaObject(0);
                Object data = null;
                if (args.atLeastHas(3)) {
                    data = args.toJavaObject(2);
                }

                if (bridge.accessClassField()) {
                    try {
                        Field field = bridge.getDataClass().getField(name);
                        if (field != null) {
                            int modifiers = field.getModifiers();
                            if (Modifier.isPublic(modifiers)) {
                                field.set(object, data);
                                return 0;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, String.format("cannot access __newindex. class: %s, name: %s, value: %s",
                                bridge.getDataClass().getSimpleName(), name, String.valueOf(data)), e);
                    }
                }

                bridge.setData(object, name, data);
                return 0;
            }
        };
    }
}
