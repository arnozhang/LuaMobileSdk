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
import com.lua.mobile.sdk.engine.JavaObjectPushInterceptor;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;

public class LuaState extends LuaObjectGetter {

    static {
        System.loadLibrary("luamobilesdk");
    }


    private long mStateId;
    private boolean mClosed = true;
    private JavaObjectPushInterceptor mPushInterceptor;
    private LuaScriptEngine mScriptEngine;


    public LuaState(LuaScriptEngine scriptEngine) {
        mScriptEngine = scriptEngine;
        mStateId = luaOpen();
        mClosed = mStateId != 0;

        openBase();
        openMath();
        openString();
        openTable();
        openUtf8();
    }

    public void close() {
        if (!mClosed) {
            luaClose();
            mClosed = true;
        }
    }

    public boolean isClosed() {
        return mClosed;
    }

    public long getStateId() {
        return mStateId;
    }

    @Override
    public LuaState getLuaState() {
        return this;
    }

    @Override
    public LuaScriptEngine getScriptEngine() {
        return mScriptEngine;
    }

    public void setPushInterceptor(JavaObjectPushInterceptor interceptor) {
        mPushInterceptor = interceptor;
    }

    public void push(Object object) {
        if (object == null) {
            pushNil();
        } else if (object instanceof Integer) {
            pushInteger((Integer) object);
        } else if (object instanceof Number) {
            pushNumber(((Number) object).doubleValue());
        } else if (object instanceof String) {
            pushString((String) object);
        } else if (object instanceof Boolean) {
            pushBoolean((Boolean) object ? 1 : 0);
        } else if (object instanceof byte[]) {
            pushBytes((byte[]) object);
        } else if (object instanceof LuaObject) {
            ((LuaObject) object).pushSelf();
        } else if (object instanceof LuaBridgedFunction) {
            pushBridgedFunction((LuaBridgedFunction) object);
        } else {
            if (mPushInterceptor == null || !mPushInterceptor.interceptPush(mScriptEngine, object)) {
                pushJavaObject(object);
            }
        }
    }

    /**
     * create new env.
     */
    protected native synchronized long luaOpen();

    protected native synchronized void luaClose();

    public native synchronized int getLuaVersion();

    /**
     * open features.
     */
    public native synchronized void openBase();

    public native synchronized void openString();

    public native synchronized void openTable();

    public native synchronized void openMath();

    public native synchronized void openDebug();

    public native synchronized void openPackage();

    public native synchronized void openUtf8();

    public native synchronized void openCoroutine();

    public native synchronized void openLibs();

    /**
     * error & gc.
     */
    public native synchronized void luaError();

    public native synchronized void luaError(@NonNull String error);

    public native synchronized void luaGc(int what, int data);

    /**
     * new something.
     */
    public native synchronized void newTable();

    /**
     * push values.
     */
    public native synchronized void pushBoolean(int value);

    public native synchronized void pushBytes(byte[] bytes);

    public native synchronized void pushNil();

    public native synchronized void pushInteger(int integer);

    public native synchronized void pushNumber(double number);

    public native synchronized void pushString(String str);

    public native synchronized void pushLString(byte[] bytes, int length);

    public native synchronized boolean pushJavaObject(Object object);

    public native synchronized boolean pushJavaObjectWithMeta(Object object, String metaTableName);

    private native synchronized boolean pushBridgedFunctionInternal(@NonNull LuaBridgedFunction function);

    public native synchronized void pushIndexValueToTop(int index);

    public void pushBridgedFunction(@NonNull LuaBridgedFunction function) {
        function.setScriptEngine(mScriptEngine);
        pushBridgedFunctionInternal(function);
    }

    /**
     * lua stack.
     */
    public native synchronized int getTop();

    public native synchronized void pop(int count);

    public native synchronized int ref(int index);

    public native synchronized void unRef(int index, int ref);

    public native synchronized int refInRegistryIndex();

    public native synchronized void unRefInRegistryIndex(int ref);

    /**
     * get value.
     */
    public native synchronized String toStringDirectly(int index);

    public native synchronized boolean toBooleanDirectly(int index);

    public native synchronized double toNumberDirectly(int index);

    public native synchronized int toIntegerDirectly(int index);

    public native synchronized Object toJavaObjectDirectly(int index);

    public native synchronized byte[] toUserDataDirectly(int index);

    @Override
    public LuaBridgedFunction toBridgedFunctionDirectly(int index) {
        return (LuaBridgedFunction) toBridgedFunctionInternal(index);
    }

    public native synchronized Object toBridgedFunctionInternal(int index);

    /**
     * check type.
     */
    public native synchronized int getType(int index);

    public native synchronized String getTypeName(int type);

    public native synchronized boolean isInteger(int index);

    public native synchronized void rawGetI(int index, int ref);

    public native synchronized void rawGetIInRegistryIndex(int ref);

    /**
     * get table[key] -> stack-top.
     *
     *       -N             -1
     *      index
     *        |
     * | table-ref | ... | key |   ...>  | table-ref | ... | table[key] |
     */
    public native synchronized void getTableField(int index);

    /**
     * table[key] = value.
     *
     *      -N              -2    -1
     *     index
     *       |
     * | table-ref | ... | key | value |    ...>  | table-ref | ... |
     */
    public native synchronized void setTableField(int index);

    /**
     * Equals:
     *
     * push(key);
     * getTableField(index - 1);
     */
    public native synchronized void getFiled(int index, @NonNull String key);

    /**
     * Equals:
     *
     * push(key);
     * setTableField(index - 1);
     */
    public native synchronized void setField(int index, @NonNull String key);

    public native synchronized void insert(int index);

    public native synchronized void remove(int index);

    /**
     * load script.
     */
    public native synchronized int doFile(@NonNull String file);

    public native synchronized int doString(@NonNull String str);

    public native synchronized int doBuffer(@NonNull byte[] buffer, int length, String name);

    public int doBuffer(@NonNull byte[] buffer, String name) {
        return doBuffer(buffer, buffer.length, name);
    }

    public int doBuffer(@NonNull byte[] buffer) {
        int ret = doBuffer(buffer, "internal");
        if (ret != 0) {
            pop(1);
            return ret;
        }

        return pcall(0, 0, 0);
    }

    /**
     * get global vars.
     */
    public native synchronized void getGlobal(@NonNull String name);

    public native synchronized void setGlobal(@NonNull String name);

    /**
     * lua function call.
     */
    public native synchronized void call(int argCount, int resultCount);

    public native synchronized int pcall(int argCount, int resultCount, int errorFunc);

    /**
     * metatable.
     */
    public native synchronized int newMetaTable(@NonNull String name);

    public native synchronized int getMetaTable(@NonNull String name);

    public native synchronized int setMetaTable(int index);

    public native synchronized int getMetaField(int index, @NonNull String field);

    public native synchronized int callMeta(int index, @NonNull String method);

    public native synchronized void bindMetaCall(int index);

    public native synchronized void bindMetaLen(int index);

    public native synchronized void bindMetaIndex(int index);

    public native synchronized void bindMetaNewIndex(int index);

    public native synchronized void bindMetaClassFunctionIndex(int index);
}
