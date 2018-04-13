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

#import "LuaScriptEngine.h"
#import "NSString+Sdk.h"
#import "LuaObject.h"
#import "LuaModuleBridge.h"
#import "LuaClassBridge.h"
#import "LuaPlainDataBridge.h"
#import "LuaState.h"
#import "LuaPlainDataClassRegister.h"
#import "LuaGlobalClassRegister.h"
#import "LuaConstVariableRegister.h"
#import "LuaFunctionRegister.h"
#import "LuaGlobalModuleRegister.h"


@implementation LuaScriptEngine {
    NSMutableDictionary<Class, NSString*>* mBridgedClassNames;
}


- (instancetype)init {
    if (self = [super init]) {
        mBridgedClassNames = [[NSMutableDictionary alloc] init];
        [self initialize];
    }

    return self;
}

- (void)initialize {
    mLuaState = [[LuaState alloc] init:self];
    [mLuaState setPushInterceptor:self];

    [self createRegisters];
}

- (void)close {
    [mVariableRegister destroy];
    [mFunctionRegister destroy];
    [mGlobalClassRegister destroy];
    [mGlobalModuleRegister destroy];
    [mPlainDataClassRegister destroy];

    [mLuaState close];
}

- (LuaState*)getLuaState {
    return mLuaState;
}

- (void)createRegisters {
    mFunctionRegister = [[LuaFunctionRegister alloc] initWithEngine:self];
    mVariableRegister = [[LuaConstVariableRegister alloc] initWithEngine:self];
    mGlobalModuleRegister = [[LuaGlobalModuleRegister alloc] initWithEngine:self];
    mGlobalClassRegister = [[LuaGlobalClassRegister alloc]
            initWithEngine:self ClassManager:self];
    mPlainDataClassRegister = [[LuaPlainDataClassRegister alloc]
            initWithEngine:self ClassManager:self];
}

- (LuaObject*)getModuleFunction:(NSString*)moduleName
                   FunctionName:(NSString*)functionName {

    [mLuaState getGlobal:moduleName];
    if (![mLuaState isTable:-1]) {
        [mLuaState pop:1];
        return nil;
    }

    [mLuaState getField:-1 FieldName:functionName];
    LuaObject* function = [mLuaState getLuaObjectByIndex:-1];
    [mLuaState pop:2];

    return function;
}

- (BOOL)interceptPush:(LuaScriptEngine*)engine Object:(NSObject*)object {
    NSString* className = [self getBridgedClassName:[object class]];
    if (className && [className sdk_isNotEmpty]) {
        [mLuaState pushObjectWithMeta:object ClassName:className];
        return YES;
    }

    return NO;
}

- (void)addBridgedClass:(Class)clazz ClassName:(NSString*)className {
    mBridgedClassNames[clazz] = className;
}

- (NSString*)getBridgedClassName:(Class)clazz {
    NSString* name = mBridgedClassNames[clazz];
    if (name) {
        return name;
    }

    NSEnumerator<Class>* enumerator = [mBridgedClassNames keyEnumerator];
    for (Class classKey in enumerator) {
        if ([clazz isSubclassOfClass:classKey]) {
            name = mBridgedClassNames[classKey];
            mBridgedClassNames[clazz] = name;
            return name;
        }
    }

    return nil;
}

- (void)registerVariable:(NSString*)name Object:(NSObject*)object {
    [mVariableRegister registerVariable:name Variable:object];
}

- (BOOL)registerModuleVariable:(NSString*)moduleName
                       VarName:(NSString*)name Variable:(NSObject*)object {
    return [mVariableRegister registerModuleVariable:moduleName VarName:name Variable:object];
}

- (void)registerFunction:(NSString*)name Function:(LuaBridgedFunction*)function {
    [mFunctionRegister registerFunction:name Function:function];
}

- (void)registerFunction:(NSString*)name Invoker:(BridgedFunctionInvoker)invoker {
    [self registerFunction:name
                  Function:[[LuaBridgedFunction alloc] initWith:self Invoker:invoker]];
}

- (BOOL)registerModuleFunction:(NSString*)moduleName
                  FunctionName:(NSString*)name
                      Function:(LuaBridgedFunction*)function {

    return [mFunctionRegister registerModuleFunction:moduleName FunctionName:name Function:function];
}

- (void)registerModule:(LuaModuleBridge*)module {
    [mGlobalModuleRegister registerModule:module];
}

- (void)registerClass:(LuaClassBridge*)bridge {
    [mGlobalClassRegister registerClass:bridge];
}

- (void)registerPlainDataClass:(LuaPlainDataBridge*)bridge {
    [mPlainDataClassRegister registerPlainDataClass:bridge];
}

- (int)doScriptFile:(NSString*)file {
    return [mLuaState doFile:file];
}

- (int)doScriptString:(NSString*)str {
    return [mLuaState doString:str];
}

- (int)doScriptBuffer:(NSData*)buffer {
    return [mLuaState doBuffer:buffer];
}

- (int)doScriptBuffer:(NSData*)buffer Length:(int)length {
    return [mLuaState doBuffer:buffer.bytes Length:(size_t) length Name:@"internal"];
}

@end