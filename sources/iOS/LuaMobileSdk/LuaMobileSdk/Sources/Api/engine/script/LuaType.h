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


/**
 * Lua 值类型
 */
typedef enum {
    LuaTypeNone = -1,
    LuaTypeNil = 0,
    LuaTypeBool = 1,
    LuaTypeLightData = 2,           // Lua
    LuaTypeNumber = 3,
    LuaTypeString = 4,
    LuaTypeTable = 5,
    LuaTypeFunction = 6,
    LuaTypeUserData = 7,            // Lua
    LuaTypeThread = 8,              // Lua

    LuaTypeObject = 1001,
    LuaTypeBridgedFunction = 1002
} LuaTypeEnum;


/**
 * Lua Type 检测.
 */
@interface LuaType : NSObject


+ (BOOL)isNil:(int)type;

+ (BOOL)isBool:(int)type;

+ (BOOL)isNumber:(int)type;

+ (BOOL)isString:(int)type;

+ (BOOL)isTable:(int)type;

+ (BOOL)isFunction:(int)type;

+ (BOOL)isUserData:(int)type;

+ (BOOL)isLightData:(int)type;

+ (BOOL)isObject:(int)type;

+ (BOOL)isBridgedFunction:(int)type;

@end