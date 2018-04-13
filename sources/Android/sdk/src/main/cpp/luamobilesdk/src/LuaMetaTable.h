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

#include "BridgeDeclares.h"


namespace LuaMetaTable {

    static const char* const __gc = "__gc";
    static const char* const __call = "__call";
    static const char* const __len = "__len";
    static const char* const __index = "__index";
    static const char* const __newindex = "__newindex";

    static const char* const __java_object_type = "__java_object_type";
    static const char* const __java_meta_funcs = "__java_meta_funcs";
    static const char* const __java_member_funcs = "__java_member_funcs";


    int object_luaCallInvoker(lua_State* ls);

    int object_luaGcInvoker(lua_State* ls);

    int meta_luaCallInvoker(lua_State* ls);

    int meta_luaLenInvoker(lua_State* ls);

    int meta_luaIndexInvoker(lua_State* ls);

    int meta_luaNewIndexInvoker(lua_State* ls);

    int meta_luaClassFunctionIndexInvoker(lua_State* ls);

    int meta_callBridgedFunction(lua_State* ls, const char* const funcName);

    int meta_memberFunctionInvokeClosure(lua_State* ls);
}
