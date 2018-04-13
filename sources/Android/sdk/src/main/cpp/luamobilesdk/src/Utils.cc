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

#include "Utils.h"
#include "Log.h"
#include "BridgeDeclares.h"
#include "LuaMetaTable.h"


namespace {

    const char* const JNI_ENV_FIELD_TAG = "__JNIEnv_tag";

} // anonymous namespace ends here.


void Utils::initializeLuaEnv(JNIEnv* env, lua_State* ls) {
}

lua_State* Utils::getLuaState(JNIEnv* env, jobject instance) {
    auto clazz = env->GetObjectClass(instance);
    auto fieldId = env->GetFieldID(clazz, "mStateId", "J");
    auto ls = reinterpret_cast<lua_State*>(env->GetLongField(instance, fieldId));

    attachJNIEnvToState(env, ls);
    initializeLuaEnv(env, ls);

    return ls;
}

void Utils::attachJNIEnvToState(JNIEnv* env, lua_State* ls) {
    lua_pushstring(ls, JNI_ENV_FIELD_TAG);
    lua_rawget(ls, LUA_REGISTRYINDEX);

    JNIEnv** ppEnv = nullptr;
    if (!lua_isnil(ls, -1)) {
        ppEnv = (JNIEnv**) lua_touserdata(ls, -1);
        *ppEnv = env;
        lua_pop(ls, 1);
    } else {
        lua_pop(ls, 1);

        ppEnv = (JNIEnv**) lua_newuserdata(ls, sizeof(JNIEnv*));
        *ppEnv = env;

        lua_pushstring(ls, JNI_ENV_FIELD_TAG);
        lua_insert(ls, -2);
        lua_rawset(ls, LUA_REGISTRYINDEX);
    }
}

JNIEnv* Utils::getJniEnvFromState(lua_State* ls) {
    lua_pushstring(ls, JNI_ENV_FIELD_TAG);
    lua_rawget(ls, LUA_REGISTRYINDEX);

    if (!lua_isuserdata(ls, -1)) {
        lua_pop(ls, 1);
        return nullptr;
    }

    JNIEnv** ppEnv = (JNIEnv**) lua_touserdata(ls, -1);
    lua_pop(ls, 1);

    return ppEnv ? *ppEnv : nullptr;
}

int Utils::getBridgedType(lua_State* ls, int index) {
    if (!lua_isuserdata(ls, index)) {
        return 0;
    }

    if (!lua_getmetatable(ls, index)) {
        return 0;
    }

    lua_pushstring(ls, LuaMetaTable::__java_object_type);
    lua_rawget(ls, -2);

    if (lua_isnil(ls, -1)) {
        lua_pop(ls, 2);
        return 0;
    }

    int type = (int) lua_tointeger(ls, -1);
    lua_pop(ls, 2);
    return type;
}

bool Utils::isJavaObject(lua_State* ls, int index) {
    return Utils::getBridgedType(ls, index) == BridgedType::JavaObject;
}

bool Utils::isJavaFunction(lua_State* ls, int index) {
    return Utils::getBridgedType(ls, index) == BridgedType::JavaFunction;
}

bool Utils::pushJavaObject(lua_State* ls, JNIEnv* env, jobject object) {
    // userData = object
    jobject globalRef = env->NewGlobalRef(object);
    jobject* userData = (jobject*) lua_newuserdata(ls, sizeof(jobject));
    *userData = globalRef;

    // meta = {}
    lua_newtable(ls);

    // meta.__gc = LuaMetaTable::object_luaGcInvoker
    lua_pushstring(ls, LuaMetaTable::__gc);
    lua_pushcfunction(ls, &LuaMetaTable::object_luaGcInvoker);
    lua_rawset(ls, -3);

    // meta.__java_object_type = JavaObject
    lua_pushstring(ls, LuaMetaTable::__java_object_type);
    lua_pushinteger(ls, BridgedType::JavaObject);
    lua_rawset(ls, -3);

    // userData.metatable = meta
    return lua_setmetatable(ls, -2) != 0;
}

bool Utils::pushJavaFunction(lua_State* ls, JNIEnv* env, jobject object) {
    // userData = function
    jobject globalRef = env->NewGlobalRef(object);
    jobject* userData = (jobject*) lua_newuserdata(ls, sizeof(jobject));
    *userData = globalRef;

    // meta = {}
    lua_newtable(ls);

    // meta.__call = LuaMetaTable::object_luaCallInvoker
    lua_pushstring(ls, LuaMetaTable::__call);
    lua_pushcfunction(ls, &LuaMetaTable::object_luaCallInvoker);
    lua_rawset(ls, -3);

    // meta.__gc = LuaMetaTable::object_luaGcInvoker
    lua_pushstring(ls, LuaMetaTable::__gc);
    lua_pushcfunction(ls, &LuaMetaTable::object_luaGcInvoker);
    lua_rawset(ls, -3);

    // meta.__java_object_type = JavaFunction
    lua_pushstring(ls, LuaMetaTable::__java_object_type);
    lua_pushinteger(ls, BridgedType::JavaFunction);
    lua_rawset(ls, -3);

    // userData.metatable = meta
    return lua_setmetatable(ls, -2) != 0;
}

void Utils::handleJavaException(JNIEnv* env, lua_State* ls, jobject exception) {
    if (!exception) {
        return;
    }

    // handle exception.
    if (BridgedJavaClassInfo::method::Throwable_printStackTrace) {
        env->ExceptionClear();
        env->CallVoidMethod(exception, BridgedJavaClassInfo::method::Throwable_printStackTrace);
    } else {
        env->ExceptionDescribe();
        env->ExceptionClear();
    }

    jobject str = env->CallObjectMethod(
            exception, BridgedJavaClassInfo::method::Throwable_getMessage);
    if (!str && BridgedJavaClassInfo::method::Throwable_toString) {
        str = env->CallObjectMethod(exception, BridgedJavaClassInfo::method::Throwable_toString);
    }

    jstring jstr = (jstring) str;
    auto message = env->GetStringUTFChars(jstr, nullptr);
    Log::e(ls, message);

    lua_pushstring(ls, message);
    env->ReleaseStringUTFChars(jstr, message);
    lua_error(ls);
}

void Utils::luaError(lua_State* ls, const char* const error) {
    Log::e(ls, error);

    lua_pushstring(ls, error);
    lua_error(ls);
}
