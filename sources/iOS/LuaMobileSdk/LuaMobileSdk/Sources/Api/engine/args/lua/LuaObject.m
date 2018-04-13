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

#import "lauxlib.h"
#import "LuaState.h"
#import "LuaType.h"
#import "LuaObject.h"
#import "LuaDeclares.h"
#import "LuaHoldedBridgeArgs.h"
#import "LuaScriptEngine.h"


@implementation LuaObject {

    LuaState* mLuaState;
    int mRef;
}


- (instancetype)init {
    [NSException raise:@"LuaObject.init" format:@"LuaObject must init with LuaState"];
    return nil;
}

- (instancetype)init:(LuaScriptEngine*)engine Index:(int)index {
    if (self = [super init]) {
        mScriptEngine = engine;
        [self initialize];
        [self registerGlobalValue:index];
    }

    return self;
}

- (instancetype)init:(LuaScriptEngine*)engine GlobalVarName:(NSString*)globalVarName {
    if (self = [super init]) {
        mScriptEngine = engine;
        [self initialize];

        [mLuaState getGlobal:globalVarName];
        [self registerGlobalValue:-1];
        [mLuaState pop:1];
    }

    return self;
}

- (instancetype)init:(LuaObject*)parent FieldIndex:(int)fieldIndex {
    if (self = [super init]) {
        return [self init:parent Obj:@(fieldIndex)];
    }

    return self;
}

- (instancetype)init:(LuaObject*)parent FieldName:(NSString*)fieldName {
    if (self = [super init]) {
        return [self init:parent Obj:fieldName];
    }

    return self;
}

- (LuaScriptEngine*)getScriptEngine {
    return mScriptEngine;
}

- (instancetype)init:(LuaObject*)parent Field:(LuaObject*)field {
    if (self = [super init]) {
        return [self init:parent Obj:field];
    }

    return self;
}

- (instancetype)init:(LuaObject*)parent Obj:(NSObject*)obj {
    if (self = [super init]) {
        mScriptEngine = [parent getScriptEngine];
        [self initialize];
    }

    if (![parent isTable]) {
        return nil;
    }

    // | parent-ref | fieldVarName |   ...>   | parent-ref | field-value |
    [parent pushSelf];
    [mLuaState push:obj];
    [mLuaState getTableField:-2];

    // | field-value|
    [mLuaState remove:-2];
    [self registerGlobalValue:-1];

    [mLuaState pop:1];

    return self;
}

- (void)initialize {
    mLuaState = [(LuaScriptEngine*) mScriptEngine getLuaState];
}

- (void)registerGlobalValue:(int)index {
    [mLuaState pushIndexValueToTop:index];
    mRef = [mLuaState refInRegistryIndex];
}

- (void)dealloc {
    if (![mLuaState isClosed]) {
        [mLuaState unRefInRegistryIndex:mRef];
    }
}

- (LuaState*)getLuaState {
    return mLuaState;
}

- (void)pushSelf {
    if (mRef == LUA_REFNIL) {
        [mLuaState pushNil];
    } else {
        [mLuaState rawGetIInRegistryIndex:mRef];
    }
}

- (int)getType {
    [self pushSelf];
    int type = [mLuaState getType:-1];
    [mLuaState pop:1];

    return type;
}

- (LuaObject*)objectForKeyedSubscript:(NSString*)fieldName {
    return [[LuaObject alloc] init:self FieldName:fieldName];
}

- (BOOL)toBool {
    [self pushSelf];
    BOOL value = [mLuaState toBool:-1];
    return value;
}

- (int)toInt {
    [self pushSelf];
    int value = (int) [mLuaState toNumber:-1];
    return value;
}

- (double)toNumber {
    [self pushSelf];
    double value = [mLuaState toNumber:-1];
    return value;
}

- (NSString*)toString {
    [self pushSelf];
    NSString* value = [mLuaState toString:-1];
    return value;
}

- (NSObject*)toObject {
    [self pushSelf];
    NSObject* value = [mLuaState toObject:-1];
    return value;
}

- (LuaObject*)getField:(NSString*)field {
    return [mLuaState getLuaObject:self FieldName:field];
}

- (BOOL)isCallable {
    int type = [self getType];
    return [LuaType isFunction:type] || [LuaType isTable:type] || [LuaType isUserData:type];
}

- (LuaBridgeArgs*)callWithReturn:(int)retCount FirstArg:(NSObject*)firstArg VaList:(va_list)ap {
    if ([mLuaState isClosed] || ![self isCallable]) {
        return nil;
    }

    int top = [mLuaState getTop];
    [self pushSelf];

    int argsCount = 0;
    if (firstArg) {
        ++argsCount;
        [mLuaState push:firstArg];

        while (YES) {
            NSObject* object = va_arg(ap, NSObject*);
            if (!object) {
                break;
            }

            [mLuaState push:object];
            ++argsCount;
        }
    }

    int ret = [mLuaState pcall:argsCount ResultCount:retCount ErrorFunc:0];
    if (ret != 0) {
        // Error.
        NSString* error = @"";
        if ([mLuaState isString:-1]) {
            error = [mLuaState toString:-1];
        }

        if (ret == RuntimeError) {
            error = [NSString stringWithFormat:@"<pcall> Lua Runtime Error: %@", error];
        } else if (ret == StackOverflowError) {
            error = [NSString stringWithFormat:@"<pcall> Lua StackOverflow Error: %@", error];
        } else if (ret == MemoryError) {
            error = [NSString stringWithFormat:@"<pcall> Lua Memory Error: %@", error];
        } else if (ret == ErrorHandlerError) {
            error = [NSString stringWithFormat:@"<pcall> Lua ErrorHandler Error: %@", error];
        } else {
            error = [NSString stringWithFormat:@"<pcall> Lua error: ret = %d, error = %@", ret, error];
        }

        [NSException raise:@"LuaObject - callWithReturn" format:error, nil];
    }

    int currTop = [mLuaState getTop];
    if (retCount == LUA_MULTRET) {
        retCount = currTop - top;
    }

    if (currTop - top < retCount) {
        [NSException raise:@"LuaObject - callWithReturn"
                    format:@"Result count is not valid: %d", retCount];
    }

    LuaHoldedBridgeArgs* results = [[LuaHoldedBridgeArgs alloc] init:mScriptEngine Count:retCount];
    [mLuaState pop:retCount];

    return results;
}

- (NSString*)description {
    const int type = [self getType];
    switch (type) {
        case LuaTypeNil:
            return @"Nil";
        case LuaTypeNone:
            return @"None";
        case LuaTypeBool:
            return [self toBool] ? @"YES" : @"NO";
        case LuaTypeNumber:
            return [NSString stringWithFormat:@"%f", [self toNumber]];
        case LuaTypeString:
            return [self toString];
        case LuaTypeObject:
            return [[self toObject] description];
        case LuaTypeFunction:
            return @"<lua> Function";
        case LuaTypeTable:
            return @"<lua> Table";
        case LuaTypeUserData:
            return @"<lua> UserData";
        case LuaTypeLightData:
            return @"<lua> LightData";
        case LuaTypeThread:
            return @"<lua> Thread";
        default:
            break;
    }

    return [super description];
}

- (BOOL)isNil {
    return [LuaType isNil:[self getType]];
}

- (BOOL)isBool {
    return [LuaType isBool:[self getType]];
}

- (BOOL)isNumber {
    return [LuaType isNumber:[self getType]];
}

- (BOOL)isString {
    return [LuaType isString:[self getType]];
}

- (BOOL)isTable {
    return [LuaType isTable:[self getType]];
}

- (BOOL)isFunction {
    return [LuaType isFunction:[self getType]];
}

- (BOOL)isObject {
    return [LuaType isObject:[self getType]];
}

- (LuaBridgeArgs*)call:(NSObject*)args, ... {
    LuaBridgeArgs* results = nil;

    va_list ap;
    va_start(ap, args);
    results = [self callWithReturn:0 FirstArg:args VaList:ap];
    va_end(ap);

    return results;
}

- (LuaBridgeArgs*)callWithMultiReturn:(NSObject*)args, ... {
    LuaBridgeArgs* results = nil;

    va_list ap;
    va_start(ap, args);
    results = [self callWithReturn:LUA_MULTRET FirstArg:args VaList:ap];
    va_end(ap);

    return results;
}

- (LuaBridgeArgs*)callWithReturn:(int)retCount Args:(NSObject*)args, ... {
    LuaBridgeArgs* results = nil;

    va_list ap;
    va_start(ap, args);
    results = [self callWithReturn:retCount FirstArg:args VaList:ap];
    va_end(ap);

    return results;
}

@end