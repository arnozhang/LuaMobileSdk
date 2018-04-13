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

package com.lua.mobile.sdk.engine.args;

import android.support.annotation.NonNull;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.engine.lua.LuaObject;
import com.lua.mobile.sdk.engine.lua.LuaObjectGetter;
import com.lua.mobile.sdk.engine.lua.LuaState;

public class LuaBridgeArgs extends LuaObjectGetter implements LuaBridgeArgsInitializer {

    protected LuaState mLuaState;
    protected LuaScriptEngine mScriptEngine;
    protected LuaBridgeArgsInitializer mInitializer;


    public LuaBridgeArgs(LuaScriptEngine scriptEngine) {
        this(scriptEngine, null);
    }

    public LuaBridgeArgs(LuaScriptEngine scriptEngine, LuaBridgeArgsInitializer initializer) {
        mScriptEngine = scriptEngine;
        mLuaState = ((LuaScriptEngine) scriptEngine).getLuaState();
        if (initializer == null) {
            initializer = this;
        }

        mInitializer = initializer;
    }

    public LuaState getLuaState() {
        return mLuaState;
    }

    public LuaScriptEngine getScriptEngine() {
        return mScriptEngine;
    }

    protected int calcArgsCount(int argCount) {
        return argCount - 1;
    }

    @Override
    public int convertIndex(int index) {
        return index + 2;
    }

    @Override
    public int getCount() {
        return calcArgsCount(mLuaState.getTop());
    }

    @Override
    public int getType(int index) {
        return mLuaState.getType(convertIndex(index));
    }

    public boolean isEmpty() {
        return getCount() <= 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean atLeastHas(int count) {
        return getCount() >= count;
    }

    @Override
    public boolean isInteger(int index) {
        return mLuaState.isInteger(index);
    }

    @Override
    public boolean toBooleanDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toBooleanDirectly(index);
    }

    @Override
    public double toNumberDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toNumberDirectly(index);
    }

    @Override
    public byte[] toUserDataDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toUserDataDirectly(index);
    }

    @Override
    public String toStringDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toStringDirectly(index);
    }

    @Override
    public Object toJavaObjectDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toJavaObjectDirectly(index);
    }

    @Override
    public LuaBridgedFunction toBridgedFunctionDirectly(int index) {
        index = mInitializer.convertIndex(index);
        return mLuaState.toBridgedFunctionDirectly(index);
    }

    @Override
    public LuaObject getLuaObject(int index) {
        index = mInitializer.convertIndex(index);
        return new LuaObject(mScriptEngine, index);
    }

    @Override
    public LuaObject getLuaObject(@NonNull String globalVarName) {
        return new LuaObject(mScriptEngine, globalVarName);
    }

    @Override
    public LuaObject getLuaObject(
            LuaObject object, @NonNull String fieldVarName) throws Exception {
        return new LuaObject((LuaObject) object, fieldVarName);
    }

    @Override
    public LuaObject getLuaObject(
            LuaObject object, int index) throws Exception {
        return new LuaObject((LuaObject) object, index);
    }

    @Override
    public LuaObject getLuaObject(
            LuaObject object, @NonNull LuaObject field) throws Exception {
        return new LuaObject((LuaObject) object, (LuaObject) field);
    }
}
