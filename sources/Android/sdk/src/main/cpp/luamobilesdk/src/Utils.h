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

#pragma once

#include <jni.h>
#include <lua.hpp>


namespace Utils {

    void initializeLuaEnv(JNIEnv* env, lua_State* ls);

    lua_State* getLuaState(JNIEnv* env, jobject pointer);

    void attachJNIEnvToState(JNIEnv* env, lua_State* ls);

    JNIEnv* getJniEnvFromState(lua_State* ls);

    inline bool checkParamsCount(lua_State* ls, int count) {
        return lua_gettop(ls) == count;
    }

    int getBridgedType(lua_State* ls, int index);

    bool isJavaObject(lua_State* ls, int index);

    bool isJavaFunction(lua_State* ls, int index);

    bool pushJavaObject(lua_State* ls, JNIEnv* env, jobject object);

    bool pushJavaFunction(lua_State* ls, JNIEnv* env, jobject object);

    void handleJavaException(JNIEnv* env, lua_State* ls, jobject exception);

    void luaError(lua_State* ls, const char* const error);
};
