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

#include <lua.hpp>

#include "BridgeDeclares.h"
#include "Utils.h"
#include "LuaMetaTable.h"


EXTERN_C_EXPORT jlong JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_luaOpen(
        JNIEnv* env) {

    BridgedJavaClassInfo::initBridgedJavaClassInfo(env);

    lua_State* ls = luaL_newstate();
    return reinterpret_cast<jlong>(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_luaClose(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    lua_close(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openBase(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_base(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openString(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_string(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openTable(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_table(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openMath(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_math(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openDebug(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_debug(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openPackage(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_package(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openUtf8(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_utf8(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openCoroutine(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaopen_coroutine(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_openLibs(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    luaL_openlibs(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushBoolean(
        JNIEnv* env, jobject instance, jint value) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pushboolean(ls, value);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushBytes(
        JNIEnv* env, jobject instance, jbyteArray bytes_) {

    jbyte* bytes = env->GetByteArrayElements(bytes_, NULL);

    auto ls = Utils::getLuaState(env, instance);
    if (!bytes) {
        lua_pushnil(ls);
    } else {
        lua_pushlightuserdata(ls, bytes);
    }

    env->ReleaseByteArrayElements(bytes_, bytes, 0);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushNil(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pushnil(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushInteger(
        JNIEnv* env, jobject instance, jint integer) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pushinteger(ls, integer);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushNumber(
        JNIEnv* env, jobject instance, jdouble number) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pushnumber(ls, number);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushString(
        JNIEnv* env, jobject instance, jstring str_) {

    auto str = env->GetStringUTFChars(str_, 0);
    auto ls = Utils::getLuaState(env, instance);
    lua_pushstring(ls, str);

    env->ReleaseStringUTFChars(str_, str);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushLString(
        JNIEnv* env, jobject instance, jbyteArray bytes_, jint length) {

    jbyte* bytes = env->GetByteArrayElements(bytes_, NULL);
    auto ls = Utils::getLuaState(env, instance);
    lua_pushlstring(ls, (const char*) bytes, (size_t) length);

    env->ReleaseByteArrayElements(bytes_, bytes, 0);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getType(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    int type = lua_type(ls, index);
    if (type == LUA_TUSERDATA) {
        int bridgedType = Utils::getBridgedType(ls, index);
        if (bridgedType != 0) {
            return bridgedType;
        }
    }

    return type;
}

EXTERN_C_EXPORT jstring JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getTypeName(
        JNIEnv* env, jobject instance, jint type) {

    auto ls = Utils::getLuaState(env, instance);
    auto name = lua_typename(ls, type);

    return env->NewStringUTF(name);
}

EXTERN_C_EXPORT jboolean JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_isInteger(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return (jboolean) lua_isinteger(ls, index);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_doFile(
        JNIEnv* env, jobject instance, jstring file_) {

    const char* file = env->GetStringUTFChars(file_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_dofile(ls, file);

    env->ReleaseStringUTFChars(file_, file);
    return ret;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_doString(
        JNIEnv* env, jobject instance, jstring str_) {

    const char* str = env->GetStringUTFChars(str_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_dostring(ls, str);

    env->ReleaseStringUTFChars(str_, str);
    return ret;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_doBuffer(
        JNIEnv* env, jobject instance, jbyteArray buffer_, size_t length, jstring name_) {

    jbyte* buffer = env->GetByteArrayElements(buffer_, NULL);
    const char* name = env->GetStringUTFChars(name_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_loadbuffer(ls, (const char*) buffer, length, name);

    env->ReleaseByteArrayElements(buffer_, buffer, 0);
    env->ReleaseStringUTFChars(name_, name);

    return ret;
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getGlobal(
        JNIEnv* env, jobject instance, jstring name_) {

    const char* name = env->GetStringUTFChars(name_, 0);

    auto ls = Utils::getLuaState(env, instance);
    lua_getglobal(ls, name);

    env->ReleaseStringUTFChars(name_, name);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_setGlobal(
        JNIEnv* env, jobject instance, jstring name_) {

    const char* name = env->GetStringUTFChars(name_, 0);

    auto ls = Utils::getLuaState(env, instance);
    lua_setglobal(ls, name);

    env->ReleaseStringUTFChars(name_, name);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pop(
        JNIEnv* env, jobject instance, jint count) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pop(ls, count);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushIndexValueToTop(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    lua_pushvalue(ls, index);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_ref(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return luaL_ref(ls, index);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_unRef(
        JNIEnv* env, jobject instance, jint index, jint ref) {

    auto ls = Utils::getLuaState(env, instance);
    luaL_unref(ls, index, ref);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_refInRegistryIndex(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    return luaL_ref(ls, LUA_REGISTRYINDEX);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_unRefInRegistryIndex(
        JNIEnv* env, jobject instance, jint ref) {

    auto ls = Utils::getLuaState(env, instance);
    luaL_unref(ls, LUA_REGISTRYINDEX, ref);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_rawGetI(
        JNIEnv* env, jobject instance, jint index, jint ref) {

    auto ls = Utils::getLuaState(env, instance);
    lua_rawgeti(ls, index, ref);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_rawGetIInRegistryIndex(
        JNIEnv* env, jobject instance, jint ref) {

    auto ls = Utils::getLuaState(env, instance);
    lua_rawgeti(ls, LUA_REGISTRYINDEX, ref);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getTableField(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    lua_gettable(ls, index);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_setTableField(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    lua_settable(ls, index);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getFiled(
        JNIEnv* env, jobject instance, jint index, jstring key_) {

    const char* key = env->GetStringUTFChars(key_, 0);

    auto ls = Utils::getLuaState(env, instance);
    lua_getfield(ls, index, key);

    env->ReleaseStringUTFChars(key_, key);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_setField(
        JNIEnv* env, jobject instance, jint index, jstring key_) {

    const char* key = env->GetStringUTFChars(key_, 0);

    auto ls = Utils::getLuaState(env, instance);
    lua_setfield(ls, index, key);

    env->ReleaseStringUTFChars(key_, key);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_insert(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    lua_insert(ls, index);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_remove(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    lua_remove(ls, index);
}

EXTERN_C_EXPORT jboolean JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toBooleanDirectly(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return (jboolean) lua_toboolean(ls, index);
}

EXTERN_C_EXPORT jdouble JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toNumberDirectly(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return lua_tonumber(ls, index);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toIntegerDirectly(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return (jint) lua_tointeger(ls, index);
}

EXTERN_C_EXPORT jstring JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toStringDirectly(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    auto str = lua_tostring(ls, index);

    return env->NewStringUTF(str);
}

EXTERN_C_EXPORT jboolean JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushJavaObject(
        JNIEnv* env, jobject instance, jobject object) {

    auto ls = Utils::getLuaState(env, instance);
    if (object == 0) {
        lua_pushnil(ls);
        return 1;
    }

    return (jboolean) Utils::pushJavaObject(ls, env, object);
}

EXTERN_C_EXPORT jboolean JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushJavaObjectWithMeta(
        JNIEnv* env, jobject instance, jobject object, jstring metaTableName_) {

    const char* metaTableName = env->GetStringUTFChars(metaTableName_, 0);

    auto ls = Utils::getLuaState(env, instance);

    // userData = object
    jobject globalRef = env->NewGlobalRef(object);
    jobject* userData = (jobject*) lua_newuserdata(ls, sizeof(jobject));
    *userData = globalRef;

    bool ret = false;
    // userData.meta = _G.metaTableName
    lua_getglobal(ls, metaTableName);
    if (lua_getmetatable(ls, -1)) {
        // obj | Class | meta_Class
        ret = lua_setmetatable(ls, -3) != 0;
    }

    lua_pop(ls, 1);
    env->ReleaseStringUTFChars(metaTableName_, metaTableName);

    return (jboolean) ret;
}

EXTERN_C_EXPORT jboolean JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pushBridgedFunctionInternal(
        JNIEnv* env, jobject instance, jobject function) {

    auto ls = Utils::getLuaState(env, instance);
    if (function == 0) {
        lua_pushnil(ls);
        return 1;
    }

    return (jboolean) Utils::pushJavaFunction(ls, env, function);
}

EXTERN_C_EXPORT jobject JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toJavaObjectDirectly(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    if (!Utils::isJavaObject(ls, index)) {
        Utils::luaError(ls, "Cannot cast to JavaObject!");
        return nullptr;
    }

    jobject* pObj = (jobject*) lua_touserdata(ls, index);
    return pObj ? *pObj : nullptr;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toUserDataDirectly(
        JNIEnv* env, jobject instance, jint index) {

    // TODO:
    auto ls = Utils::getLuaState(env, instance);
    return nullptr;
}

EXTERN_C_EXPORT jobject JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_toBridgedFunctionInternal(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    if (!Utils::isJavaFunction(ls, index)) {
        Utils::luaError(ls, "Cannot cast to BridgedFunction!");
        return nullptr;
    }

    jobject* pObj = (jobject*) lua_touserdata(ls, index);
    return pObj ? *pObj : nullptr;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getTop(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    return lua_gettop(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_call(
        JNIEnv* env, jobject instance, jint argCount, jint resultCount) {

    auto ls = Utils::getLuaState(env, instance);
    lua_call(ls, argCount, resultCount);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_pcall(
        JNIEnv* env, jobject instance, jint argCount, jint resultCount, jint errorFunc) {

    auto ls = Utils::getLuaState(env, instance);
    return lua_pcall(ls, argCount, resultCount, errorFunc);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_luaError__(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    lua_error(ls);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_luaError__Ljava_lang_String_2(
        JNIEnv* env, jobject instance, jstring error_) {

    auto ls = Utils::getLuaState(env, instance);

    const char* error = env->GetStringUTFChars(error_, 0);
    Utils::luaError(ls, error);

    env->ReleaseStringUTFChars(error_, error);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_luaGc(
        JNIEnv* env, jobject instance, jint what, jint data) {

    auto ls = Utils::getLuaState(env, instance);
    lua_gc(ls, what, data);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_newTable(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    lua_newtable(ls);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getLuaVersion(
        JNIEnv* env, jobject instance) {

    auto ls = Utils::getLuaState(env, instance);
    const lua_Number* ptr = lua_version(ls);
    return (jint) (ptr ? *ptr : 0);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_newMetaTable(
        JNIEnv* env, jobject instance, jstring name_) {

    const char* name = env->GetStringUTFChars(name_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_newmetatable(ls, name);

    env->ReleaseStringUTFChars(name_, name);

    return ret;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getMetaTable(
        JNIEnv* env, jobject instance, jstring name_) {

    const char* name = env->GetStringUTFChars(name_, 0);

    auto ls = Utils::getLuaState(env, instance);
    int ret = luaL_getmetatable(ls, name);

    env->ReleaseStringUTFChars(name_, name);
    return ret;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_setMetaTable(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);
    return lua_setmetatable(ls, index);
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_getMetaField(
        JNIEnv* env, jobject instance, jint index, jstring field_) {

    const char* field = env->GetStringUTFChars(field_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_getmetafield(ls, index, field);

    env->ReleaseStringUTFChars(field_, field);

    return ret;
}

EXTERN_C_EXPORT jint JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_callMeta(
        JNIEnv* env, jobject instance, jint index, jstring method_) {

    const char* method = env->GetStringUTFChars(method_, 0);

    auto ls = Utils::getLuaState(env, instance);
    auto ret = luaL_callmeta(ls, index, method);

    env->ReleaseStringUTFChars(method_, method);

    return ret;
}

void
_bindMetaMethod(
        JNIEnv* env, jobject instance, int index,
        const char* methodName, lua_CFunction function) {

    auto ls = Utils::getLuaState(env, instance);

    lua_pushvalue(ls, index);

    lua_pushstring(ls, methodName);
    lua_pushcfunction(ls, function);
    lua_rawset(ls, -3);

    lua_pop(ls, 1);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_bindMetaCall(
        JNIEnv* env, jobject instance, jint index) {

    _bindMetaMethod(env, instance, index, LuaMetaTable::__call,
                    &LuaMetaTable::meta_luaCallInvoker);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_bindMetaLen(
        JNIEnv* env, jobject instance, jint index) {

    _bindMetaMethod(env, instance, index, LuaMetaTable::__len,
                    &LuaMetaTable::meta_luaLenInvoker);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_bindMetaIndex(
        JNIEnv* env, jobject instance, jint index) {

    _bindMetaMethod(env, instance, index, LuaMetaTable::__index,
                    &LuaMetaTable::meta_luaIndexInvoker);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_bindMetaNewIndex(
        JNIEnv* env, jobject instance, jint index) {

    _bindMetaMethod(env, instance, index, LuaMetaTable::__newindex,
                    &LuaMetaTable::meta_luaNewIndexInvoker);
}

EXTERN_C_EXPORT void JNICALL
Java_com_lua_mobile_sdk_engine_lua_LuaState_bindMetaClassFunctionIndex(
        JNIEnv* env, jobject instance, jint index) {

    auto ls = Utils::getLuaState(env, instance);

    lua_pushvalue(ls, index);
    lua_pushstring(ls, LuaMetaTable::__index);
    lua_pushcfunction(ls, &LuaMetaTable::meta_luaClassFunctionIndexInvoker);
    lua_rawset(ls, -3);

    lua_pop(ls, 1);
}

