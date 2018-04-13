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

#import <Foundation/Foundation.h>
#import "lua.h"
#import "LuaTypeChecker.h"
#import "LuaObjectGetter.h"


@class LuaBridgedFunction;
@class LuaScriptEngine;
@protocol ObjectPushInterceptor;


/**
 * Lua 底层核心 LuaState 包装.
 */
@interface LuaState : LuaObjectGetter


- (instancetype)init:(LuaScriptEngine*)engine;

- (void)open;
- (void)close;
- (BOOL)isClosed;
- (int)getLuaVersion;

- (void)setPushInterceptor:(id <ObjectPushInterceptor>)interceptor;

/**
 * open features.
 */
- (void)openBase;
- (void)openMath;
- (void)openString;
- (void)openTable;
- (void)openUtf8;

/**
 * error & gc.
 */
- (void)luaError;
- (void)luaError:(NSString*)error;
- (void)luaGc:(int)what Data:(int)data;

/**
 * new something.
 */
- (void)newTable;

/**
 * push values.
 */
- (void)push:(NSObject*)object;
- (void)pushNil;
- (void)pushBool:(BOOL)value;
- (void)pushInt:(int)value;
- (void)pushNumber:(double)value;
- (void)pushString:(NSString*)value;
- (void)pushCString:(const char*)value;
- (void)pushObject:(NSObject*)object;
- (void)pushIndexValueToTop:(int)index;
- (void)pushCFunction:(lua_CFunction)function;
- (void)pushBridgedFunction:(LuaBridgedFunction*)function;
- (void)pushCClosure:(lua_CFunction)closure UpValueCount:(int)upValueCount;

/**
 * lua stack.
 */
- (int)getTop;
- (void)pop:(int)count;
- (int)ref:(int)index;
- (void)unRef:(int)index Ref:(int)ref;
- (int)refInRegistryIndex;
- (void)unRefInRegistryIndex:(int)index;
- (int)getUpValueIndex:(int)index;

/**
 * check type.
 */
- (int)getType:(int)index;
- (NSString*)getTypeName:(int)index;

/**
 * raw set.
 */
- (void)rawGet:(int)index;
- (void)rawSet:(int)index;
- (void)rawGetI:(int)index Ref:(int)ref;
- (void)rawSetI:(int)index Ref:(int)ref;
- (void)rawGetIInRegistryIndex:(int)ref;

/**
 * fields.
 */
- (void)getField:(int)index FieldName:(NSString*)fieldName;
- (void)setField:(int)index FieldName:(NSString*)fieldName;

/**
 * tables.
 */
- (void)getTableField:(int)index;
- (void)setTableField:(int)index;
- (void)getFiled:(int)index Key:(NSString*)key;
- (void)setField:(int)index Key:(NSString*)key;
- (void)getFiled:(int)index CStrKey:(const char*)key;
- (void)setField:(int)index CStrKey:(const char*)key;
- (void)insert:(int)index;
- (void)remove:(int)index;

/**
 * light data.
 */
- (void*)newUserData:(size_t)size;

/**
 * load scripts.
 */
- (int)doFile:(NSString*)file;
- (int)doString:(NSString*)str;
- (int)doBuffer:(NSData*)data;
- (int)doBuffer:(char*)buffer Length:(size_t)length Name:(NSString*)name;

/**
 * get global vars.
 */
- (void)getGlobal:(NSString*)name;
- (void)setGlobal:(NSString*)name;

/**
 * lua function call.
 */
- (int)call:(int)argCount ResultCount:(int)resultCount;
- (int)pcall:(int)argCount ResultCount:(int)resultCount ErrorFunc:(int)errorFunc;

/**
 * metatable.
 */
- (int)newMetaTable:(NSString*)name;
- (int)getMetaTableWithName:(NSString*)name;
- (int)getMetaTable:(int)index;
- (int)setMetaTable:(int)index;
- (int)getMetaField:(int)index Field:(NSString*)field;
- (int)callMeta:(int)index Method:(NSString*)method;

/**
 * push object. attach metadata.
 */
- (void)pushObjectWithMeta:(NSObject*)object ClassName:(NSString*)className;

@end
