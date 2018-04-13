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

#include "LuaMetaTable.h"
#include "Utils.h"


int LuaMetaTable::object_luaCallInvoker(lua_State* ls) {
    if (!Utils::isJavaFunction(ls, 1)) {
        Utils::luaError(ls, "Not a BridgedFunction!! cannot invoke!");
        return 0;
    }

    JNIEnv* env = Utils::getJniEnvFromState(ls);
    if (!env) {
        Utils::luaError(ls, "Cannot get JNIEnv from lua_State!");
        return 0;
    }

    jobject* pObj = (jobject*) lua_touserdata(ls, 1);
    int ret = env->CallIntMethod(
            *pObj, BridgedJavaClassInfo::method::BridgedFunction_invoke);
    jthrowable exception = env->ExceptionOccurred();
    Utils::handleJavaException(env, ls, exception);

    return ret;
}

int LuaMetaTable::object_luaGcInvoker(lua_State* ls) {
    int type = Utils::getBridgedType(ls, 1);
    if (type != BridgedType::JavaObject && type != BridgedType::JavaFunction) {
        return 0;
    }

    jobject* pObj = (jobject*) lua_touserdata(ls, 1);
    auto env = Utils::getJniEnvFromState(ls);
    if (env && pObj) {
        env->DeleteGlobalRef(*pObj);
    }

    return 0;
}

int LuaMetaTable::meta_callBridgedFunction(lua_State* ls, const char* const funcName) {
    if (!lua_istable(ls, 1) && !lua_isuserdata(ls, 1)) {
        Utils::luaError(ls, "Not a Table or UserData!! cannot invoke meta-method!");
        return 0;
    }

    JNIEnv* env = Utils::getJniEnvFromState(ls);
    if (!env) {
        Utils::luaError(ls, "Cannot get JNIEnv from lua_State!");
        return 0;
    }

    if (!lua_getmetatable(ls, 1)) {
        lua_pop(ls, 1);
        lua_pushnil(ls);
        return 1;
    }

    if (!lua_istable(ls, -1) && !lua_isuserdata(ls, -1)) {
        lua_pop(ls, 1);
        lua_pushnil(ls);
        return 1;
    }

    // arg1, arg2, ... obj.meta, obj.meta.__java_meta_funcs, javaBridgeFunc
    lua_getfield(ls, -1, LuaMetaTable::__java_meta_funcs);
    lua_pushstring(ls, funcName);
    lua_rawget(ls, -2);

    jobject* pObj = (jobject*) lua_touserdata(ls, -1);
    lua_pop(ls, 3);

    int ret = 0;
    if (pObj) {
        ret = env->CallIntMethod(
                *pObj, BridgedJavaClassInfo::method::BridgedFunction_invoke);
        jthrowable exception = env->ExceptionOccurred();
        Utils::handleJavaException(env, ls, exception);
    }

    return ret;
}

int LuaMetaTable::meta_luaCallInvoker(lua_State* ls) {
    return meta_callBridgedFunction(ls, LuaMetaTable::__call);
}

int LuaMetaTable::meta_luaLenInvoker(lua_State* ls) {
    return meta_callBridgedFunction(ls, LuaMetaTable::__len);
}

int LuaMetaTable::meta_luaIndexInvoker(lua_State* ls) {
    return meta_callBridgedFunction(ls, LuaMetaTable::__index);
}

int LuaMetaTable::meta_luaNewIndexInvoker(lua_State* ls) {
    return meta_callBridgedFunction(ls, LuaMetaTable::__index);
}

int LuaMetaTable::meta_luaClassFunctionIndexInvoker(lua_State* ls) {
    // a.__index = function(a, name) ... end
    //                      1   2
    //
    // obj -> upvalue(1)
    lua_pushvalue(ls, 1);

    // name -> upvalue(2)
    lua_pushvalue(ls, 2);
    lua_pushcclosure(ls, &LuaMetaTable::meta_memberFunctionInvokeClosure, 2);

    return 1;
}

int LuaMetaTable::meta_memberFunctionInvokeClosure(lua_State* ls) {
    auto env = Utils::getJniEnvFromState(ls);
    if (!env) {
        return 0;
    }

    int argsCount = lua_gettop(ls);

    int objIndex = lua_upvalueindex(1);
    int nameIndex = lua_upvalueindex(2);

    if (!lua_istable(ls, objIndex) && !lua_isuserdata(ls, objIndex)) {
        return 0;
    }

    if (!lua_getmetatable(ls, objIndex)) {
        return 0;
    }

    // arg1, arg2, ... obj.meta, obj.meta.__java_member_funcs
    lua_getfield(ls, -1, LuaMetaTable::__java_member_funcs);

    // arg1, arg2, ... obj.meta, obj.meta.__java_member_funcs, javaBridgeFunc
    lua_pushvalue(ls, nameIndex);
    lua_rawget(ls, -2);

    jobject* pObj = (jobject*) lua_touserdata(ls, -1);
    lua_pop(ls, 3);

    int ret = 0;
    if (pObj) {
        // obj
        lua_pushvalue(ls, objIndex);
        lua_insert(ls, 1);

        // placeholder
        lua_pushnil(ls);
        lua_insert(ls, 1);

        // nil, obj, arg1, arg2, ...
        ret = env->CallIntMethod(
                *pObj, BridgedJavaClassInfo::method::BridgedFunction_invoke);
        jthrowable exception = env->ExceptionOccurred();
        Utils::handleJavaException(env, ls, exception);

        // balance stack. remove placeholder and args.
        lua_remove(ls, 1);
        lua_remove(ls, 1);
    }

    return ret;
}
