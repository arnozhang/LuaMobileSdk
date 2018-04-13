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

#import "LuaState.h"
#import "lualib.h"
#import "lauxlib.h"
#import "LuaType.h"
#import "LuaBridgeUtils.h"
#import "BoolObject.h"
#import "LogWrapper.h"
#import "LuaScriptEngine.h"
#import "LuaBridgedFunction.h"


static NSString* const TAG = @"LuaState";


static int sdk_callFinished(lua_State* ls, int status, lua_KContext extra) {
    if (status != LUA_OK && status != LUA_YIELD) {
        // error
        const char* message = lua_tostring(ls, -2);
        logE(TAG, [NSString stringWithUTF8String:message]);

        lua_pushboolean(ls, 0);      // status
        lua_pushvalue(ls, -2);       // error message

        // return status, message
        return 2;
    } else {
        // success call return results
        return lua_gettop(ls) - (int) extra;
    }
}


@implementation LuaState {
    lua_State* ls;
    BOOL mClosed;
    __weak LuaScriptEngine* mScriptEngine;
    id <ObjectPushInterceptor> mPushInterceptor;
}


- (instancetype)init:(LuaScriptEngine*)engine {
    if (self = [super init]) {
        mScriptEngine = engine;

        [self open];
        [self openBase];
        [self openMath];
        [self openString];
        [self openTable];
        [self openUtf8];
    }

    return self;
}

- (void)open {
    ls = luaL_newstate();
    mClosed = ls == NULL;
}

- (void)close {
    if (!mClosed) {
        lua_close(ls);
        mClosed = YES;
    }
}

- (BOOL)isClosed {
    return mClosed;
}

- (int)getLuaVersion {
    const lua_Number* ptr = lua_version(ls);
    return (int) (ptr ? *ptr : 0);
}

- (void)setPushInterceptor:(id <ObjectPushInterceptor>)interceptor {
    mPushInterceptor = interceptor;
}

- (void)openBase {
    luaopen_base(ls);
}

- (void)openMath {
    luaopen_math(ls);
}

- (void)openString {
    luaopen_string(ls);
}

- (void)openTable {
    luaopen_table(ls);
}

- (void)openUtf8 {
    luaopen_utf8(ls);
}

- (void)luaError {
    lua_error(ls);
}

- (void)luaError:(NSString*)error {
    lua_pushstring(ls, error.UTF8String);
    lua_error(ls);
}

- (void)luaGc:(int)what Data:(int)data {
    lua_gc(ls, what, data);
}

- (void)newTable {
    lua_newtable(ls);
}

- (void)push:(NSObject*)object {
    if (object == nil) {
        [self pushNil];
    } else if ([object isKindOfClass:[NSNumber class]]) {
        NSNumber* number = (NSNumber*) object;
        [self pushNumber:number.doubleValue];
    } else if ([object isKindOfClass:[NSString class]]) {
        [self pushString:(NSString*) object];
    } else if ([object isKindOfClass:[BoolObject class]]) {
        BoolObject* boolean = (BoolObject*) object;
        [self pushBool:boolean.value];
    } else if ([object isKindOfClass:[LuaBridgedFunction class]]) {
        [self pushBridgedFunction:(LuaBridgedFunction*) object];
    } else {
        if (mPushInterceptor == nil || ![mPushInterceptor interceptPush:mScriptEngine Object:object]) {
            [self pushObject:object];
        }
    }
}

- (void)pushNil {
    lua_pushnil(ls);
}

- (void)pushBool:(BOOL)value {
    lua_pushboolean(ls, value);
}

- (void)pushInt:(int)value {
    lua_pushinteger(ls, value);
}

- (void)pushNumber:(double)value {
    lua_pushnumber(ls, value);
}

- (void)pushString:(NSString*)value {
    if (value) {
        lua_pushstring(ls, value.UTF8String);
    } else {
        lua_pushnil(ls);
    }
}

- (void)pushCString:(const char*)value {
    lua_pushstring(ls, value);
}

- (void)pushObject:(NSObject*)object {
    if (!object) {
        [self pushNil];
        return;
    }

    [LuaBridgeUtils pushObject:ls Object:object];
}

- (void)pushBridgedFunction:(LuaBridgedFunction*)function {
    if (!function) {
        [self pushNil];
        return;
    }

    [function setScriptEngine:mScriptEngine];
    [LuaBridgeUtils pushBridgedFunction:ls Function:function];
}

- (void)pushCFunction:(lua_CFunction)function {
    lua_pushcfunction(ls, function);
}

- (void)pushCClosure:(lua_CFunction)closure UpValueCount:(int)upValueCount {
    lua_pushcclosure(ls, closure, upValueCount);
}

- (void)pushIndexValueToTop:(int)index {
    lua_pushvalue(ls, index);
}

- (int)getTop {
    return lua_gettop(ls);
}

- (void)pop:(int)count {
    lua_pop(ls, count);
}

- (int)ref:(int)index {
    return luaL_ref(ls, index);
}

- (void)unRef:(int)index Ref:(int)ref {
    luaL_unref(ls, index, ref);
}

- (int)refInRegistryIndex {
    return [self ref:LUA_REGISTRYINDEX];
}

- (void)unRefInRegistryIndex:(int)index {
    [self unRef:index Ref:LUA_REGISTRYINDEX];
}

- (int)getUpValueIndex:(int)index {
    return lua_upvalueindex(index);
}

- (LuaState*)getLuaState {
    return self;
}

- (LuaScriptEngine*)getScriptEngine {
    return mScriptEngine;
}

- (BOOL)toBoolDirectly:(int)index {
    return lua_toboolean(ls, index) != 0;
}

- (void*)toUserDataDirectly:(int)index {
    return lua_touserdata(ls, index);
}

- (double)toNumberDirectly:(int)index {
    return lua_tonumber(ls, index);
}

- (NSString*)toStringDirectly:(int)index {
    const char* str = lua_tostring(ls, index);
    return str ? [NSString stringWithUTF8String:str] : @"";
}

- (NSObject*)toObjectDirectly:(int)index {
    return [LuaBridgeUtils toObject:ls Index:index];
}

- (LuaBridgedFunction*)toBridgedFunctionDirectly:(int)index {
    return [LuaBridgeUtils toBridgedFunction:ls Index:index];
}

- (int)getType:(int)index {
    int type = [LuaBridgeUtils getBridgedType:ls Index:index];
    if (type == 0) {
        type = lua_type(ls, index);
    }

    return type;
}

- (NSString*)getTypeName:(int)index {
    const char* str = lua_typename(ls, index);
    return str ? [NSString stringWithUTF8String:str] : @"";
}

- (void)rawGet:(int)index {
    lua_rawget(ls, index);
}

- (void)rawSet:(int)index {
    lua_rawset(ls, index);
}

- (void)rawGetI:(int)index Ref:(int)ref {
    lua_rawgeti(ls, index, ref);
}

- (void)rawSetI:(int)index Ref:(int)ref {
    lua_rawseti(ls, index, ref);
}

- (void)rawGetIInRegistryIndex:(int)ref {
    [self rawGetI:LUA_REGISTRYINDEX Ref:ref];
}

- (void)getField:(int)index FieldName:(NSString*)fieldName {
    lua_getfield(ls, index, fieldName.UTF8String);
}

- (void)setField:(int)index FieldName:(NSString*)fieldName {
    lua_setfield(ls, index, fieldName.UTF8String);
}

- (void)getTableField:(int)index {
    lua_gettable(ls, index);
}

- (void)setTableField:(int)index {
    lua_settable(ls, index);
}

- (void)getFiled:(int)index Key:(NSString*)key {
    lua_getfield(ls, index, key.UTF8String);
}

- (void)setField:(int)index Key:(NSString*)key {
    lua_setfield(ls, index, key.UTF8String);
}

- (void)getFiled:(int)index CStrKey:(const char*)key {
    lua_getfield(ls, index, key);
}

- (void)setField:(int)index CStrKey:(const char*)key {
    lua_setfield(ls, index, key);
}

- (void)insert:(int)index {
    lua_insert(ls, index);
}

- (void)remove:(int)index {
    lua_remove(ls, index);
}

- (void*)newUserData:(size_t)size {
    return lua_newuserdata(ls, size);
}

- (int)doFile:(NSString*)file {
    return luaL_dofile(ls, file.UTF8String);
}

- (int)doString:(NSString*)str {
    return luaL_dostring(ls, str.UTF8String);
}

- (int)doBuffer:(NSData*)data {
    return [self doBuffer:data.bytes Length:data.length Name:@"internal"];
}

- (int)doBuffer:(char*)buffer Length:(size_t)length Name:(NSString*)name {
    int ret = luaL_loadbuffer(ls, buffer, length, name.UTF8String);
    if (ret != 0) {
        [self pop:1];
        return ret;
    }

    [self call:0 ResultCount:0];
    return ret;
}

- (void)getGlobal:(NSString*)name {
    lua_getglobal(ls, name.UTF8String);
}

- (void)setGlobal:(NSString*)name {
    lua_setglobal(ls, name.UTF8String);
}

- (int)call:(int)argCount ResultCount:(int)resultCount {
    return lua_pcallk(ls, argCount, resultCount, 0, 0, sdk_callFinished);
}

- (int)pcall:(int)argCount ResultCount:(int)resultCount ErrorFunc:(int)errorFunc {
    return lua_pcallk(ls, argCount, resultCount, errorFunc, 0, sdk_callFinished);
}

- (int)newMetaTable:(NSString*)name {
    return luaL_newmetatable(ls, name.UTF8String);
}

- (int)getMetaTableWithName:(NSString*)name {
    return luaL_getmetatable(ls, name.UTF8String);
}

- (int)getMetaTable:(int)index {
    return lua_getmetatable(ls, index);
}

- (int)setMetaTable:(int)index {
    return lua_setmetatable(ls, index);
}

- (int)getMetaField:(int)index Field:(NSString*)field {
    return luaL_getmetafield(ls, index, field.UTF8String);
}

- (int)callMeta:(int)index Method:(NSString*)method {
    return luaL_callmeta(ls, index, method.UTF8String);
}

- (void)pushObjectWithMeta:(NSObject*)object ClassName:(NSString*)className {
    [self pushObject:object];

    // attach with meta
    [self getGlobal:className];
    if ([self getMetaTable:-1]) {
        // obj Class meta
        //  -3  -2    -1
        [self setMetaTable:-3];
    }

    [self pop:1];
}

- (BOOL)isObject:(int)index {
    return [LuaBridgeUtils getBridgedType:ls Index:index] == LuaTypeObject;
}

- (BOOL)isBridgedFunction:(int)index {
    return [LuaBridgeUtils getBridgedType:ls Index:index] == LuaTypeBridgedFunction;
}

@end