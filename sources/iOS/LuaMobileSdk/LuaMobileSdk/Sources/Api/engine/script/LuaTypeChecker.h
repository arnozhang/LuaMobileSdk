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
 * Lua 某个索引处 Type 检测.
 */
@interface LuaTypeChecker : NSObject


- (BOOL)isNil:(int)index;

- (BOOL)isBool:(int)index;

- (BOOL)isNumber:(int)index;

- (BOOL)isString:(int)index;

- (BOOL)isTable:(int)index;

- (BOOL)isFunction:(int)index;

- (BOOL)isUserData:(int)index;

- (BOOL)isObject:(int)index;

- (BOOL)isBridgedFunction:(int)index;

- (BOOL)isInteger:(int)index;

- (int)getType:(int)index;

@end