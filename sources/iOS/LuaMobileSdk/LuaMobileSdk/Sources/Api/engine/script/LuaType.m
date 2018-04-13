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

#import "LuaType.h"


@implementation LuaType


+ (BOOL)isNil:(int)type {
    return type == LuaTypeNil || type == LuaTypeNone;
}

+ (BOOL)isBool:(int)type {
    return type == LuaTypeBool;
}

+ (BOOL)isNumber:(int)type {
    return type == LuaTypeNumber;
}

+ (BOOL)isString:(int)type {
    return type == LuaTypeString;
}

+ (BOOL)isTable:(int)type {
    return type == LuaTypeTable;
}

+ (BOOL)isFunction:(int)type {
    return type == LuaTypeFunction;
}

+ (BOOL)isUserData:(int)type {
    return type == LuaTypeUserData;
}

+ (BOOL)isLightData:(int)type {
    return type == LuaTypeLightData;
}

+ (BOOL)isObject:(int)type {
    return type == LuaTypeObject;
}

+ (BOOL)isBridgedFunction:(int)type {
    return type == LuaTypeBridgedFunction;
}

@end