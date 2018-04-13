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

#include "Log.h"

namespace {

    const char* const LOG_MODULE = "Log";

    void callLogFunction(lua_State* ls, const char* function,
                         const char* const tag, const char* message) {

        lua_getglobal(ls, LOG_MODULE);
        lua_getfield(ls, -1, function);
        lua_remove(ls, -2);

        lua_pushstring(ls, tag);
        lua_pushstring(ls, message);
        lua_call(ls, 2, 0);
    }

} // anonymous namespace ends here.


void Log::v(lua_State* ls, const char* const tag, const char* message) {
    callLogFunction(ls, "v", tag, message);
}

void Log::d(lua_State* ls, const char* const tag, const char* message) {
    callLogFunction(ls, "d", tag, message);
}

void Log::i(lua_State* ls, const char* const tag, const char* message) {
    callLogFunction(ls, "i", tag, message);
}

void Log::w(lua_State* ls, const char* const tag, const char* message) {
    callLogFunction(ls, "w", tag, message);
}

void Log::e(lua_State* ls, const char* const tag, const char* message) {
    callLogFunction(ls, "e", tag, message);
}
