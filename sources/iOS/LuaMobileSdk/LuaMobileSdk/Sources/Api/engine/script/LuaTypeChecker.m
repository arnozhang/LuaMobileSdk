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

#import "LuaTypeChecker.h"
#import "LuaType.h"


@implementation LuaTypeChecker


- (BOOL)isNil:(int)index {
    return [LuaType isNil:[self getType:index]];
}

- (BOOL)isBool:(int)index {
    return [LuaType isBool:[self getType:index]];
}

- (BOOL)isNumber:(int)index {
    return [LuaType isNumber:[self getType:index]];
}

- (BOOL)isString:(int)index {
    return [LuaType isString:[self getType:index]];
}

- (BOOL)isTable:(int)index {
    return [LuaType isTable:[self getType:index]];
}

- (BOOL)isFunction:(int)index {
    return [LuaType isFunction:[self getType:index]];
}

- (BOOL)isUserData:(int)index {
    return [LuaType isUserData:[self getType:index]];
}

- (BOOL)isObject:(int)index {
    return [LuaType isObject:[self getType:index]];
}

- (BOOL)isBridgedFunction:(int)index {
    return [LuaType isBridgedFunction:[self getType:index]];
}

- (BOOL)isInteger:(int)index {
    return [LuaType isNumber:[self getType:index]];
}

- (int)getType:(int)index {
    return LuaTypeNone;
}

@end