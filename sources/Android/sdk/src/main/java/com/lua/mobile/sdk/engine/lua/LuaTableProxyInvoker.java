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

import com.lua.mobile.sdk.engine.script.LuaType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LuaTableProxyInvoker implements InvocationHandler {

    private LuaObject mLuaObject;


    public LuaTableProxyInvoker(LuaObject luaObject) {
        mLuaObject = luaObject;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        LuaObject memberFunc = mLuaObject.getField(method.getName());
        if (memberFunc.isNull()) {
            return null;
        }

        Class resultType = method.getReturnType();
        if (resultType == void.class || resultType == Void.class) {
            memberFunc.call(args);
            return null;
        }

        Object ret = memberFunc.callWithReturn(args, 1);
        if (ret instanceof Double) {
            return LuaType.convertLuaNumber(ret, resultType);
        }

        return ret;
    }
}
