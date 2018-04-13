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

package com.lua.mobile.sdk.engine.lua;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.args.LuaHoldBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.script.LuaType;

import java.lang.reflect.Proxy;

public class LuaObject {

    private static final int LUA_MULTRET = -1;


    private Integer mRef = 0;
    private LuaState mLuaState;
    private LuaScriptEngine mScriptEngine;


    public LuaObject(@NonNull LuaScriptEngine scriptEngine, int index) {
        mScriptEngine = scriptEngine;
        initialize();
        registerGlobalValue(index);
    }

    public LuaObject(@NonNull LuaScriptEngine scriptEngine, String globalVarName) {
        mScriptEngine = scriptEngine;
        initialize();

        mLuaState.getGlobal(globalVarName);
        registerGlobalValue(-1);
        mLuaState.pop(1);
    }

    public LuaObject(@NonNull LuaObject parent, String fieldVarName) throws Exception {
        this(parent, (Object) fieldVarName);
    }

    public LuaObject(@NonNull LuaObject parent, int fieldIndex) throws Exception {
        this(parent, Integer.valueOf(fieldIndex));
    }

    public LuaObject(@NonNull LuaObject parent, LuaObject field) throws Exception {
        this(parent, (Object) field);
    }

    private LuaObject(@NonNull LuaObject parent, Object obj) throws Exception {
        mScriptEngine = parent.getScriptEngine();
        initialize();

        if (!parent.isTable() && !parent.isUserData()) {
            throw new Exception("Cannot access field " + String.valueOf(obj) + " in not Table or UserData!");
        }

        // | parent-ref | fieldVarName |   ...>   | parent-ref | field-value |
        parent.pushSelf();
        mLuaState.push(obj);
        mLuaState.getTableField(-2);

        // | field-value|
        mLuaState.remove(-2);
        registerGlobalValue(-1);

        mLuaState.pop(1);
    }

    public LuaScriptEngine getScriptEngine() {
        return mScriptEngine;
    }

    private void initialize() {
        mLuaState = ((LuaScriptEngine) mScriptEngine).getLuaState();
    }

    @Override
    protected void finalize() throws Throwable {
        if (!mLuaState.isClosed()) {
            mLuaState.unRefInRegistryIndex(mRef);
        }

        super.finalize();
    }

    public LuaState getLuaState() {
        return mLuaState;
    }

    private void registerGlobalValue(int index) {
        mLuaState.pushIndexValueToTop(index);
        mRef = mLuaState.refInRegistryIndex();
    }

    public void pushSelf() {
        if (mRef == -1) {
            mLuaState.pushNil();
        } else {
            mLuaState.rawGetIInRegistryIndex(mRef);
        }
    }

    public int getType() {
        pushSelf();
        int type = mLuaState.getType(-1);
        mLuaState.pop(1);

        return type;
    }

    public boolean toBoolean() {
        pushSelf();
        boolean value = mLuaState.toBoolean(-1);
        mLuaState.pop(-1);
        return value;
    }

    public int toInt() {
        pushSelf();
        int value = mLuaState.toIntFixed(-1);
        mLuaState.pop(-1);
        return value;
    }

    public double toNumber() {
        pushSelf();
        double value = mLuaState.toNumber(-1);
        mLuaState.pop(-1);
        return value;
    }

    public String _toString() {
        pushSelf();
        String value = mLuaState.toString(-1);
        mLuaState.pop(-1);
        return value;
    }

    public Object toJavaObject() {
        pushSelf();
        Object value = mLuaState.toJavaObject(-1);
        mLuaState.pop(-1);
        return value;
    }

    public LuaBridgedFunction toBridgedFunction() {
        pushSelf();
        Object value = mLuaState.toBridgedFunction(-1);
        mLuaState.pop(-1);
        return (LuaBridgedFunction) value;
    }

    public LuaObject getField(String field) throws Exception {
        return mLuaState.getLuaObject(this, field);
    }

    public boolean isCallable() {
        int type = getType();
        return LuaType.isFunction(type) || LuaType.isTable(type) || LuaType.isUserData(type);
    }

    @Nullable
    public LuaBridgeArgs callWithMultiReturn(Object... args) {
        return callWithReturn(args, LUA_MULTRET);
    }

    @Nullable
    public LuaBridgeArgs callWithReturn(Object[] args, int retCount) {
        if (!isCallable()) {
            return null;
        }

        int top = mLuaState.getTop();
        pushSelf();

        int argsCount = 0;
        if (args != null) {
            argsCount = args.length;
            for (int i = 0; i < argsCount; ++i) {
                mLuaState.push(args[i]);
            }
        }

        int ret = mLuaState.pcall(argsCount, retCount, 0);
        if (ret != 0) {
            // Error.
            String error = "";
            if (mLuaState.isString(-1)) {
                error = mLuaState.toString(-1);
            }

            if (ret == LuaError.RuntimeError) {
                error = "<pcall> Lua Runtime Error: " + error;
            } else if (ret == LuaError.StackOverflowError) {
                error = "<pcall> Lua StackOverflow Error: " + error;
            } else if (ret == LuaError.MemoryError) {
                error = "<pcall> Lua Memory Error: " + error;
            } else if (ret == LuaError.ErrorHandlerError) {
                error = "<pcall> Lua ErrorHandler Error: " + error;
            } else {
                error = "<pcall> Lua error: ret = " + ret + ", error = " + error;
            }

            throw new RuntimeException(error);
        }

        int currTop = mLuaState.getTop();
        if (retCount == LUA_MULTRET) {
            retCount = currTop - top;
        }

        if (currTop - top < retCount) {
            throw new RuntimeException("Result count is not valid: " + retCount);
        }

        LuaHoldBridgeArgs results = new LuaHoldBridgeArgs(mScriptEngine, retCount);
        mLuaState.pop(retCount);

        return results;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T toTableProxy(Class<T> clazz) {
        if (!isTable()) {
            return null;
        }

        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{clazz}, new LuaTableProxyInvoker(this));
    }

    @Nullable
    public LuaBridgeArgs call(Object... args) {
        return callWithReturn(args, 0);
    }

    public boolean isNull() {
        return LuaType.isNull(getType());
    }

    public boolean isBoolean() {
        return LuaType.isBoolean(getType());
    }

    public boolean isNumber() {
        return LuaType.isNumber(getType());
    }

    public boolean isString() {
        return LuaType.isString(getType());
    }

    public boolean isTable() {
        return LuaType.isTable(getType());
    }

    public boolean isFunction() {
        return LuaType.isFunction(getType());
    }

    public boolean isUserData() {
        return LuaType.isUserData(getType());
    }

    public boolean isJavaObject() {
        return LuaType.isJavaObject(getType());
    }

    public boolean isBridgedFunction() {
        return LuaType.isBridgedFunction(getType());
    }

    @Override
    public String toString() {
        switch (getType()) {
            case LuaType.NIL:
                return "Nil";
            case LuaType.NONE:
                return "None";
            case LuaType.BOOLEAN:
                return String.valueOf(toBoolean());
            case LuaType.NUMBER:
                return String.valueOf(toNumber());
            case LuaType.STRING:
                return _toString();
            case LuaType.JAVA_OBJECT:
                return String.valueOf(toJavaObject());
            case LuaType.BRIDGE_FUNCTION:
                return String.valueOf(toBridgedFunction());
            case LuaType.FUNCTION:
                return "<lua> Function";
            case LuaType.TABLE:
                return "<lua> Table";
            case LuaType.USERDATA:
                return "<lua> UserData";
            case LuaType.LIGHTUSERDATA:
                return "<lua> LightData";
            case LuaType.THREAD:
                return "<lua> Thread";
            default:
                break;
        }

        return super.toString();
    }
}
