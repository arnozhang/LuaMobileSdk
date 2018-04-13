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
#import "LuaTypeChecker.h"


@class LuaState;
@class LuaScriptEngine;
@class LuaObject;


/**
 * Lua 对象获取器.
 */
@interface LuaObjectGetter : LuaTypeChecker


- (LuaState*)getLuaState;
- (LuaScriptEngine*)getScriptEngine;

- (int)getType:(int)index;

/**
 * get values directly
 *
 * 直接获取,可能失败. 子类需重写它们.
 */
- (BOOL)toBoolDirectly:(int)index;
- (double)toNumberDirectly:(int)index;
- (NSString*)toStringDirectly:(int)index;
- (NSObject*)toObjectDirectly:(int)index;

/**
 * get values.
 */
- (BOOL)toBool:(int)index;
- (double)toNumber:(int)index;
- (NSString*)toString:(int)index;
- (NSObject*)toObject:(int)index;

- (BOOL)toBool:(int)index Optional:(BOOL)opt;
- (double)toNumber:(int)index Optional:(double)opt;
- (NSString*)toString:(int)index Optional:(NSString*)opt;
- (NSObject*)toObject:(int)index Optional:(NSObject*)opt;


/**
 * with fixed value.
 */
- (BOOL)toBoolFixed:(int)index;
- (BOOL)toBoolFixed:(int)index Optional:(BOOL)opt;

- (double)toNumberFixed:(int)index;
- (double)toNumberFixed:(int)index Optional:(double)opt;

- (NSString*)toStringFixed:(int)index;
- (NSString*)toStringFixed:(int)index Optional:(NSString*)opt;

/**
 * force convert to object
 *
 * null                     - nil
 * BOOL                     - BoolObject*
 * int/long/float/double    - NSNumber
 * string                   - NSString*
 * object                   - NSObject*
 */
- (NSObject*)toObjectFixed:(int)index;
- (NSObject*)toObjectFixed:(int)index Optional:(NSObject*)opt;

/**
 * other built-in raw type
 */
- (int)toIntFixed:(int)index;
- (int)toIntFixed:(int)index Optional:(int)opt;

- (long)toLongFixed:(int)index;
- (long)toLongFixed:(int)index Optional:(long)opt;

- (float)toFloatFixed:(int)index;
- (float)toFloatFixed:(int)index Optional:(float)opt;

/**
 * LuaObject
 */
- (LuaObject*)objectForKeyedSubscript:(NSString*)globalVarName;

- (LuaObject*)getLuaObjectByIndex:(int)index;
- (LuaObject*)getLuaObject:(NSString*)globalVarName;
- (LuaObject*)getLuaObject:(LuaObject*)object FieldIndex:(int)index;
- (LuaObject*)getLuaObject:(LuaObject*)object FieldName:(NSString*)fieldName;
- (LuaObject*)getLuaObject:(LuaObject*)object LuaObject:(LuaObject*)field;

@end