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

#import "LuaPlainDataClassRegister.h"
#import "LuaState.h"
#import "LuaDeclares.h"
#import "NSString+Sdk.h"
#import "LuaBridgeUtils.h"
#import "SdkDeclares.h"


int sdk_PlainClassLenInvoker(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;
    LuaMethodInvokeEnv* env = (__bridge LuaMethodInvokeEnv*) wrapper->env;
    LuaPlainDataBridge* bridge = (LuaPlainDataBridge*) env.bridge;

    // a.__index = function(object) ... end
    //                         1
    //
    NSObject* object = [state toObject:1];
    [state pushNumber:[bridge length:object]];

    return 1;
}

int sdk_PlainClassIndexInvoker(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;
    LuaMethodInvokeEnv* env = (__bridge LuaMethodInvokeEnv*) wrapper->env;
    LuaPlainDataBridge* bridge = (LuaPlainDataBridge*) env.bridge;

    // a.__index = function(object, name) ... end
    //                         1     2
    //
    NSObject* object = [state toObject:1];
    NSString* name = [state toString:2];
    [state push:[bridge getData:object Name:name]];

    return 1;
}

int sdk_PlainClassNewIndexInvoker(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;
    LuaMethodInvokeEnv* env = (__bridge LuaMethodInvokeEnv*) wrapper->env;
    LuaPlainDataBridge* bridge = (LuaPlainDataBridge*) env.bridge;

    // a.__index = function(object, name, value) ... end
    //                         1     2     3
    //
    NSObject* object = [state toObject:1];
    NSString* name = [state toString:2];
    NSObject* value = [state toObjectFixed:3];
    [bridge setData:object Name:name Value:value];

    return 0;
}

@implementation LuaPlainDataClassRegister


- (void)destroy {
    [super destroy];
}

- (void)registerPlainDataClass:(LuaPlainDataBridge*)bridge {
    if (![super registerClassPrepare:bridge]) {
        return;
    }

    [mLuaState pop:2];
}

- (void)bindMetaMethods:(LuaClassBridge*)bridge {
    [super bindMetaMethods:bridge];

    // meta.__len = len
    [self newWrappedClosureAtTop:bridge Method:nil Closure:&sdk_PlainClassLenInvoker];
    [mLuaState setField:-2 CStrKey:__len];

    // meta.__index = index
    [self newWrappedClosureAtTop:bridge Method:nil Closure:&sdk_PlainClassIndexInvoker];
    [mLuaState setField:-2 CStrKey:__index];

    // meta.__newindex = newindex
    [self newWrappedClosureAtTop:bridge Method:nil Closure:&sdk_PlainClassNewIndexInvoker];
    [mLuaState setField:-2 CStrKey:__newindex];
}

- (LuaBridgedFunction*)createLenFunction:(LuaPlainDataBridge*)bridge {
    sdk_weakify(bridge);
    return [[LuaBridgedFunction alloc]
            initWith:mScriptEngine Invoker:^NSObject*(LuaScriptEngine* engine, LuaBridgeArgs* args) {
                sdk_strongify(bridge);

                if (![args atLeastHas:1]) {
                    return @(0);
                }

                NSObject* object = [args toObject:0];
                return @([bridge length:object]);
            }];
}

- (LuaBridgedFunction*)createIndexFunction:(LuaPlainDataBridge*)bridge {
    sdk_weakify(bridge);
    return [[LuaBridgedFunction alloc]
            initWith:mScriptEngine Invoker:^NSObject*(LuaScriptEngine* engine, LuaBridgeArgs* args) {
                sdk_strongify(bridge);

                if (![args atLeastHas:2]) {
                    return nil;
                }

                NSString* name = [args toString:1];
                if (!name || [name sdk_isEmpty]) {
                    return nil;
                }

                NSObject* object = [args toObject:0];
                return [bridge getData:object Name:name];
            }];
}

- (LuaBridgedFunction*)createNewIndexFunction:(LuaPlainDataBridge*)bridge {
    sdk_weakify(bridge);
    return [[LuaBridgedFunction alloc]
            initWith:mScriptEngine Invoker:^NSObject*(LuaScriptEngine* engine, LuaBridgeArgs* args) {
                sdk_strongify(bridge);

                if (![args atLeastHas:3]) {
                    return nil;
                }

                NSString* name = [args toString:1];
                if (!name || [name sdk_isEmpty]) {
                    return nil;
                }

                NSObject* object = [args toObject:0];
                NSObject* value = [args toObjectFixed:2];
                [bridge setData:object Name:name Value:value];
                return nil;
            }];
}

@end