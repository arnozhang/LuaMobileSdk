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
import android.util.Log;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.InvokeParamsExtractor;
import com.lua.mobile.sdk.engine.bridge.LuaAbstractBridge;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.bridge.LuaClassBridge;
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.lua.LuaDeclares;
import com.lua.mobile.sdk.engine.lua.LuaState;
import com.lua.mobile.sdk.engine.script.LuaType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LuaAbstractClassRegister extends AbstractRegister {

    private static final String TAG = "LuaClassRegister";


    protected LuaState mLuaState;


    public LuaAbstractClassRegister(LuaScriptEngine scriptEngine) {
        super(scriptEngine);
        mLuaState = scriptEngine.getLuaState();
    }

    /**
     * stack:  {ClassName}, {meta}
     */
    public <T> void registerClassPrepare(@NonNull final LuaClassBridge<T> bridge) {
        String name = bridge.getBridgeName();
        mScriptEngine.addBridgedClass(bridge.getDataClass(), name);

        // temp = {}
        mLuaState.newTable();

        // _G.ClassName = temp
        mLuaState.pushIndexValueToTop(-1);
        mLuaState.setGlobal(name);

        // register  vars.
        //
        // -> public final  var
        //
        Field[] fields = bridge.getClass().getFields();
        registerStaticVariableInternal(fields, bridge);

        // meta = {}
        mLuaState.newTable();

        // _G.ClassName.metatable = meta
        mLuaState.pushIndexValueToTop(-1);
        mLuaState.setMetaTable(-3);

        // meta.__java_object_type = JAVA_OBJECT
        mLuaState.pushNumber(LuaType.JAVA_OBJECT);
        mLuaState.setField(-2, LuaDeclares.__java_object_type);

        // register meta methods.
        // meta.__java_meta_funcs = {}
        mLuaState.newTable();
        bindMetaMethods(-2, bridge);
        mLuaState.setField(-2, LuaDeclares.__java_meta_funcs);

        afterBindMetaMethods(-1);
    }

    protected <T> void bindMetaMethods(int tableIndex, @NonNull final LuaClassBridge<T> bridge) {
        // meta
        final String name = bridge.getBridgeName();
        LuaBridgedFunction call = new LuaBridgedFunction(mScriptEngine) {

            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Exception {
                // instance = new()
                // instance.metatable = meta
                //
                T instance = bridge.createInstance(mScriptEngine, args);
                mLuaState.pushJavaObjectWithMeta(instance, name);

                return 1;
            }
        };

        // meta.__java_meta_funcs.__call = call
        mLuaState.pushBridgedFunction(call);
        call.setFunctionName(name + "." + LuaDeclares.__call);
        mLuaState.setField(tableIndex, LuaDeclares.__call);
    }

    protected void afterBindMetaMethods(int tableIndex) {
        // meta.__call = call
        mLuaState.bindMetaCall(tableIndex);
    }

    protected void registerStaticVariableInternal(Field[] fields, LuaAbstractBridge bridge) {
        registerStaticVariableInternal((LuaScriptEngine) mScriptEngine, fields, bridge);
    }

    /**
     * register  final variables.
     *
     * stack[-1]   -   Module / Class
     */
    public static void registerStaticVariableInternal(
            LuaScriptEngine scriptEngine, Field[] fields, LuaAbstractBridge bridge) {

        LuaState state = scriptEngine.getLuaState();
        for (Field field : fields) {
            if (field.isAnnotationPresent(LuaMethod.class)) {
                continue;
            }

            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)
                    || !Modifier.isFinal(modifiers)
                    || !Modifier.isStatic(modifiers)) {
                continue;
            }

            // _G.ModuleName.fieldName = field
            try {
                state.push(field.get(bridge));
                state.setField(-2, field.getName());
            } catch (IllegalAccessException e) {
                Log.e(TAG, "registerStaticVariableInternal", e);
            }
        }
    }

    protected void registerBridgedFunctionInternal(Method[] methods, LuaAbstractBridge bridge) {
        registerBridgedFunctionInternal((LuaScriptEngine) mScriptEngine, methods, bridge);
    }

    protected static void registerBridgedFunctionInternal(
            LuaScriptEngine scriptEngine, Method[] methods, LuaAbstractBridge bridge) {

        LuaState state = scriptEngine.getLuaState();

        for (final Method method : methods) {
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers)
                    || !method.isAnnotationPresent(LuaMethod.class)) {
                continue;
            }

            String name = method.getName();
            LuaBridgedFunction function = createBridgedFunction(scriptEngine, method, bridge);

            // meta.method = function
            function.setFunctionName(bridge.getBridgeName() + "." + name);
            state.pushBridgedFunction(function);
            state.setField(-2, name);
        }
    }

    private static LuaBridgedFunction createBridgedFunction(
            LuaScriptEngine scriptEngine,
            @NonNull final Method method, @NonNull final Object bridge) {

        Class<?> retClazz = method.getReturnType();
        final boolean hasReturnType = retClazz != null && retClazz != void.class && retClazz != Void.class;
        final InvokeParamsExtractor extractor = InvokeParamsExtractor.createParamsExtractor(method);

        final LuaState mLuaState = scriptEngine.getLuaState();
        return new LuaBridgedFunction(scriptEngine) {
            @Override
            protected LuaBridgeArgs createBridgeArgs() {
                return new LuaBridgeArgs(mScriptEngine);
            }

            @Override
            protected int invokeInternal(LuaBridgeArgs bridgeArgs) throws Throwable {
                Object returnValue = null;

                try {
                    if (extractor == null) {
                        // R function()
                        returnValue = method.invoke(bridge);
                    } else {
                        returnValue = method.invoke(bridge, extractor.extractParams(bridgeArgs));
                    }
                } catch (Throwable e) {
                    Log.e(TAG, String.format("Invoke bridge member-method FAILED! target = %s, method = %s",
                            bridge.getClass().getSimpleName(), method.getName()), e);
                }

                if (hasReturnType) {
                    mLuaState.push(returnValue);
                    return 1;
                }

                return 0;
            }
        };
    }
}
