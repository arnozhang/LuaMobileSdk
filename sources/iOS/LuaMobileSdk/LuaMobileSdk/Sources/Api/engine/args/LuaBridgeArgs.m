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

#import "LuaBridgeArgs.h"
#import "LuaState.h"
#import "LuaScriptEngine.h"
#import "LuaObject.h"


@implementation LuaBridgeArgs


- (instancetype)init:(LuaScriptEngine*)engine {
    if (self = [super init]) {
        mScriptEngine = engine;
        return [self init:engine ArgsInitializer:nil];
    }

    return self;
}

- (instancetype)init:(LuaScriptEngine*)engine
     ArgsInitializer:(id <LuaBridgeArgsInitializer>)initializer {

    if (self = [super init]) {
        mScriptEngine = engine;
        mInitializer = initializer ? initializer : self;
        mLuaState = [(LuaScriptEngine*) mScriptEngine getLuaState];
    }

    return self;
}

- (int)calcArgsCount:(int)argCount {
    return argCount - 1;
}

- (LuaScriptEngine*)getScriptEngine {
    return mScriptEngine;
}

- (BOOL)isEmpty {
    return [self getCount] <= 0;
}

- (BOOL)isNotEmpty {
    return ![self isEmpty];
}

- (BOOL)atLeastHas:(int)count {
    return [self getCount] >= count;
}

- (int)convertIndex:(int)index {
    return index + 2;
}

- (int)getCount {
    return [self calcArgsCount:[mLuaState getTop]];
}

- (int)getType:(int)index {
    return [mLuaState getType:[self convertIndex:index]];
}

- (LuaState*)getLuaState {
    return mLuaState;
}

- (BOOL)toBoolDirectly:(int)index {
    index = [mInitializer convertIndex:index];
    return [mLuaState toBoolDirectly:index];
}

- (double)toNumberDirectly:(int)index {
    index = [mInitializer convertIndex:index];
    return [mLuaState toNumberFixed:index];
}

- (NSString*)toStringDirectly:(int)index {
    index = [mInitializer convertIndex:index];
    return [mLuaState toStringFixed:index];
}

- (NSObject*)toObjectDirectly:(int)index {
    index = [mInitializer convertIndex:index];
    return [mLuaState toObjectFixed:index];
}

- (LuaObject*)getLuaObjectByIndex:(int)index {
    index = [mInitializer convertIndex:index];
    return [[LuaObject alloc] init:mScriptEngine Index:index];
}

- (LuaObject*)getLuaObject:(NSString*)globalVarName {
    return [[LuaObject alloc] init:mScriptEngine GlobalVarName:globalVarName];
}

- (LuaObject*)getLuaObject:(LuaObject*)object FieldIndex:(int)index {
    return [[LuaObject alloc] init:object FieldIndex:index];
}

- (LuaObject*)getLuaObject:(LuaObject*)object FieldName:(NSString*)fieldName {
    return [[LuaObject alloc] init:object FieldName:fieldName];
}

- (LuaObject*)getLuaObject:(LuaObject*)object LuaObject:(LuaObject*)field {
    return [[LuaObject alloc] init:object Field:field];
}

@end