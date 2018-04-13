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
#import "LuaBridgedFunction.h"
#import "LuaObject.h"


@class LuaState;
@class LuaBridgeArgs;


/**
 * Lua 对象包装.
 *
 * 可获取 全局索引/全局命名/对象索引/对象命名字段 等等对象.
 */
@interface LuaObject : NSObject {
    LuaScriptEngine* mScriptEngine;
}


- (instancetype)init:(LuaScriptEngine*)engine Index:(int)index;
- (instancetype)init:(LuaScriptEngine*)engine GlobalVarName:(NSString*)globalVarName;
- (instancetype)init:(LuaObject*)parent FieldIndex:(int)fieldIndex;
- (instancetype)init:(LuaObject*)parent FieldName:(NSString*)fieldName;
- (instancetype)init:(LuaObject*)parent Field:(LuaObject*)field;

- (LuaState*)getLuaState;
- (LuaScriptEngine*)getScriptEngine;

- (void)dealloc;
- (void)pushSelf;


- (int)getType;

- (LuaObject*)objectForKeyedSubscript:(NSString*)fieldName;

/**
 * types
 */
- (BOOL)isNil;
- (BOOL)isBool;
- (BOOL)isNumber;
- (BOOL)isString;
- (BOOL)isTable;
- (BOOL)isFunction;
- (BOOL)isObject;

/**
 * convert values
 */
- (BOOL)toBool;
- (int)toInt;
- (double)toNumber;
- (NSString*)toString;
- (NSObject*)toObject;

/**
 * field
 */
- (LuaObject*)getField:(NSString*)field;

/**
 * call
 */
- (BOOL)isCallable;
- (LuaBridgeArgs*)call:(NSObject*)args, ... NS_REQUIRES_NIL_TERMINATION;
- (LuaBridgeArgs*)callWithMultiReturn:(NSObject*)args, ... NS_REQUIRES_NIL_TERMINATION;
- (LuaBridgeArgs*)callWithReturn:(int)retCount Args:(NSObject*)args, ...NS_REQUIRES_NIL_TERMINATION;
- (LuaBridgeArgs*)callWithReturn:(int)retCount FirstArg:(NSObject*)firstArg VaList:(va_list)ap;

@end