/**
 * Android LuaMobileSdk for iOS framework project.
 *
 * Copyright 2017 Arno Zhang <zyfgood12@163.com>
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

#import "LuaBridgeUtils.h"
#import "LuaDeclares.h"
#import "LuaType.h"
#import "LuaBridgedFunction.h"
#import "LuaBridgeLog.h"
#import "LuaState.h"
#import "LuaScriptEngine.h"


int object_luaGcInvoker(lua_State* ls) {
    if (!lua_isuserdata(ls, 1)) {
        return 0;
    }

    _ObjectInfo* info = lua_touserdata(ls, 1);
    if (info) {
        __unused  NSObject* object = (__bridge_transfer NSObject*) info->object;
    }

    return 0;
}

int object_luaCallInvoker(lua_State* ls) {
    if ([LuaBridgeUtils getBridgedType:ls Index:1] != LuaTypeBridgedFunction) {
        [LuaBridgeUtils luaError:ls Message:"Not a LuaBridgedFunction!! cannot invoke!"];
        return 0;
    }

    _ObjectInfo* info = lua_touserdata(ls, 1);
    LuaBridgedFunction* function = (__bridge LuaBridgedFunction*) info->object;
    NSObject* result = [function invoke];

    LuaScriptEngine* engine = (LuaScriptEngine*) [function getScriptEngine];
    [[engine getLuaState] push:result];
    return 1;
}

MethodInvokeWrapper* sdk_getInvokerWrapper(lua_State* ls) {
    int wrapperIndex = lua_upvalueindex(1);
    if (!lua_isuserdata(ls, wrapperIndex)) {
        return nil;
    }

    return lua_touserdata(ls, wrapperIndex);
}


@implementation LuaBridgeUtils


+ (int)getBridgedType:(lua_State*)ls Index:(int)index {
    if (!lua_isuserdata(ls, index)) {
        return 0;
    }

    if (!lua_getmetatable(ls, index)) {
        return 0;
    }

    lua_pushstring(ls, __oc_object_type);
    lua_rawget(ls, -2);

    if (lua_isnil(ls, -1)) {
        lua_pop(ls, 2);
        return 0;
    }

    int type = (int) lua_tointeger(ls, -1);
    lua_pop(ls, 2);
    return type;
}

+ (BOOL)pushObject:(lua_State*)ls Object:(NSObject*)object {
    [LuaBridgeUtils pushObjectPrepare:ls Object:object Type:LuaTypeObject];

    // userData.metatable = meta
    return lua_setmetatable(ls, -2) != 0;
}

+ (BOOL)pushBridgedFunction:(lua_State*)ls Function:(LuaBridgedFunction*)function {
    [LuaBridgeUtils pushObjectPrepare:ls Object:function Type:LuaTypeBridgedFunction];

    // meta.__call = object_luaCallInvoker
    lua_pushstring(ls, __call);
    lua_pushcfunction(ls, &object_luaCallInvoker);
    lua_rawset(ls, -3);

    // userData.metatable = meta
    return lua_setmetatable(ls, -2) != 0;
}

+ (void)luaError:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog e:ls Message:message];

    // notify error.
    lua_pushstring(ls, message);
    lua_error(ls);
}

+ (void)pushObjectPrepare:(lua_State*)ls Object:(NSObject*)object Type:(int)type {
    _ObjectInfo* info = lua_newuserdata(ls, sizeof(_ObjectInfo));
    info->type = type;
    info->object = (__bridge_retained void*) object;

    // meta = {}
    lua_newtable(ls);

    // meta.__gc = object_luaGcInvoker
    lua_pushstring(ls, __gc);
    lua_pushcfunction(ls, &object_luaGcInvoker);
    lua_rawset(ls, -3);

    // meta.__oc_object_type = type
    lua_pushstring(ls, __oc_object_type);
    lua_pushinteger(ls, type);
    lua_rawset(ls, -3);
}

+ (NSObject*)toObject:(lua_State*)ls Index:(int)index {
    int type = [LuaBridgeUtils getBridgedType:ls Index:index];
    if (type == 0) {
        return nil;
    }

    _ObjectInfo* info = lua_touserdata(ls, index);
    return (__bridge NSObject*) info->object;
}

+ (LuaBridgedFunction*)toBridgedFunction:(lua_State*)ls Index:(int)index {
    int type = [LuaBridgeUtils getBridgedType:ls Index:index];
    if (type != LuaTypeBridgedFunction) {
        return nil;
    }

    _ObjectInfo* info = lua_touserdata(ls, index);
    return (__bridge LuaBridgedFunction*) info->object;
}

@end