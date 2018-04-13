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

#include <lua.hpp>


namespace Log {

    static const char* const TAG = "LuaLogger";


    void v(lua_State* ls, const char* const tag, const char* message);

    void d(lua_State* ls, const char* const tag, const char* message);

    void i(lua_State* ls, const char* const tag, const char* message);

    void w(lua_State* ls, const char* const tag, const char* message);

    void e(lua_State* ls, const char* const tag, const char* message);

    inline void v(lua_State* ls, const char* const message) {
        v(ls, TAG, message);
    }

    inline void d(lua_State* ls, const char* const message) {
        d(ls, TAG, message);
    }

    inline void i(lua_State* ls, const char* const message) {
        i(ls, TAG, message);
    }

    inline void w(lua_State* ls, const char* const message) {
        w(ls, TAG, message);
    }

    inline void e(lua_State* ls, const char* const message) {
        e(ls, TAG, message);
    }
};
