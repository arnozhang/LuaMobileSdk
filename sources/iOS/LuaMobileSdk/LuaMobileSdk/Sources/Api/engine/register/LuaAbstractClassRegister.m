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
#import "LuaDeclares.h"
#import "LuaBridgeUtils.h"
#import "LuaState.h"
#import "LuaBridgeArgs.h"
#import "LuaScriptEngine.h"
#import "LuaClassBridge.h"
#import "LuaAbstractClassRegister.h"


int sdk_MethodInvokerInternal(lua_State* ls, BOOL isStatic) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;
    LuaMethodInvokeEnv* env = (__bridge LuaMethodInvokeEnv*) wrapper->env;

    if (isStatic) {
        // 占位.
        [state pushNil];
        [state insert:1];
        [state pushNil];
        [state insert:1];
    }

    LuaScriptEngine* engine = [state getScriptEngine];
    LuaBridgeArgs* args = [[LuaBridgeArgs alloc] init:engine];
    [state push:(*wrapper->method)(env, args)];

    return 1;
}

int sdk_instanceMethodInvoker(lua_State* ls) {
    return sdk_MethodInvokerInternal(ls, NO);
}

int sdk_staticMethodInvoker(lua_State* ls) {
    return sdk_MethodInvokerInternal(ls, YES);
}

int sdk_callInvoker(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;
    LuaMethodInvokeEnv* env = (__bridge LuaMethodInvokeEnv*) wrapper->env;
    LuaClassBridge* bridge = (LuaClassBridge*) env.bridge;
    LuaScriptEngine* engine = [state getScriptEngine];

    LuaBridgeArgs* args = [[LuaBridgeArgs alloc] init:engine];
    NSObject* instance = [bridge createInstance:engine Args:args];
    [state pushObjectWithMeta:instance ClassName:[bridge getBridgeName]];

    return 1;
}


@implementation LuaAbstractClassRegister


- (instancetype)initWithEngine:(LuaScriptEngine*)engine
                  ClassManager:(id <RegisterClassManager>)manager {
    if (self = [super initWithEngine:engine]) {
        mLuaState = [engine getLuaState];
        mRegisterClassManager = manager;
    }

    return self;
}

- (BOOL)registerClassPrepare:(LuaClassBridge*)bridge {
    bridge.env = [[LuaMethodInvokeEnv alloc] init:bridge Reg:self];
    [self holdBridge:bridge];

    NSString* name = [bridge getBridgeName];
    if (!name || name.length <= 0) {
        @throw [[NSException alloc]
                initWithName:@"GlobalClassRegister"
                      reason:@"LuaClassBridge name must not empty!" userInfo:nil];
        return NO;
    }

    [mRegisterClassManager addBridgedClass:[bridge getDataClass] ClassName:name];

    // temp = {}
    [mLuaState newTable];

    // _G.ClassName = temp
    [mLuaState pushIndexValueToTop:-1];
    [mLuaState setGlobal:name];

    // register const static vars.
    [bridge registerStaticVariables:self];

    // register static methods.
    [bridge registerStaticMethods:self];

    // meta = {}
    [mLuaState newTable];

    // _G.ClassName.metatable = meta
    [mLuaState pushIndexValueToTop:-1];
    [mLuaState setMetaTable:-3];

    // meta.__oc_object_type = LuaTypeObject
    [mLuaState pushNumber:LuaTypeObject];
    [mLuaState setField:-2 CStrKey:__oc_object_type];

    // register meta methods.
    [self bindMetaMethods:bridge];

    return YES;
}

- (void)bindMetaMethods:(LuaClassBridge*)bridge {
    // meta.__call = call
    [self newWrappedClosureAtTop:bridge Method:nil Closure:&sdk_callInvoker];
    [mLuaState setField:-2 CStrKey:__call];
}

- (void)registerBool:(NSString*)name Value:(BOOL)value {
    [mLuaState pushBool:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerInt:(NSString*)name Value:(int)value {
    [mLuaState pushInt:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerLong:(NSString*)name Value:(long)value {
    [self registerDouble:name Value:value];
}

- (void)registerFloat:(NSString*)name Value:(float)value {
    [self registerDouble:name Value:value];
}

- (void)registerDouble:(NSString*)name Value:(double)value {
    [mLuaState pushNumber:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerString:(NSString*)name Value:(NSString*)value {
    [mLuaState pushString:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerObject:(NSString*)name Value:(NSObject*)value {
    [mLuaState push:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerBridgedFunction:(NSString*)name Value:(LuaBridgedFunction*)value {
    [mLuaState pushBridgedFunction:value];
    [mLuaState setField:-2 Key:name];
}

- (void)registerInstanceMethod:(LuaAbstractBridge*)bridge
                          Name:(NSString*)name
                        Method:(StaticBridgeMethod)method {

    [self newWrappedClosureAtTop:bridge Method:method Closure:&sdk_instanceMethodInvoker];
    [mLuaState setField:-2 Key:name];
}

- (void)registerStaticMethod:(LuaAbstractBridge*)bridge
                        Name:(NSString*)name
                      Method:(StaticBridgeMethod)method {

    [self newWrappedClosureAtTop:bridge Method:method Closure:&sdk_staticMethodInvoker];
    [mLuaState setField:-2 Key:name];
}

- (void)newWrappedClosureAtTop:(LuaAbstractBridge*)bridge
                        Method:(void*)method
                       Closure:(lua_CFunction)closure {

    LuaWithMemberBridge* module = (LuaWithMemberBridge*) bridge;

    MethodInvokeWrapper* wrapper = [mLuaState newUserData:sizeof(MethodInvokeWrapper)];
    wrapper->method = method;
    wrapper->env = (__bridge void*) module.env;
    wrapper->luaState = (__bridge void*) mLuaState;

    [mLuaState pushCClosure:closure UpValueCount:1];
}

@end