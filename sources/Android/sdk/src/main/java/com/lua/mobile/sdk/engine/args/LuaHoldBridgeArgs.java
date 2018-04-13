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

import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;

public class LuaHoldBridgeArgs extends LuaBridgeArgs {

    protected static class ArgsInfo {
        int type;
        Object object;
    }


    protected ArgsInfo[] mHoldArgs;
    protected int mCount;


    public LuaHoldBridgeArgs(LuaScriptEngine scriptEngine, int count) {
        super(scriptEngine);

        mCount = count;
        mHoldArgs = new ArgsInfo[count];
        for (int i = 0; i < count; ++i) {
            int index = mInitializer.convertIndex(i);

            ArgsInfo info = new ArgsInfo();
            info.type = mLuaState.getType(index);
            info.object = mLuaState.toJavaObjectFixed(index);
            mHoldArgs[i] = info;
        }
    }

    @Override
    public int convertIndex(int index) {
        return index - mCount;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public int getType(int index) {
        return mHoldArgs[index].type;
    }

    @Override
    public boolean toBooleanDirectly(int index) {
        Object object = mHoldArgs[index].object;
        return object instanceof Boolean && (boolean) object;
    }

    @Override
    public double toNumberDirectly(int index) {
        Object object = mHoldArgs[index].object;
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        return 0;
    }

    @Override
    public String toStringDirectly(int index) {
        Object object = mHoldArgs[index].object;
        if (object instanceof String) {
            return (String) object;
        }

        return null;
    }

    @Override
    public Object toJavaObjectDirectly(int index) {
        return mHoldArgs[index].object;
    }

    @Override
    public LuaBridgedFunction toBridgedFunctionDirectly(int index) {
        Object object = mHoldArgs[index].object;
        return object instanceof LuaBridgedFunction ? (LuaBridgedFunction) object : null;
    }
}
