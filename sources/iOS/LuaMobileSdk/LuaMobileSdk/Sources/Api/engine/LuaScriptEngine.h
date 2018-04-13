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
#import "LuaBridgedFunction.h"


@class LuaScriptEngine;
@class LuaObject;
@class LuaModuleBridge;
@class LuaPlainDataBridge;
@class LuaState;
@class LuaFunctionRegister;
@class LuaConstVariableRegister;
@class LuaGlobalModuleRegister;
@class LuaGlobalClassRegister;
@class LuaClassBridge;
@class LuaPlainDataClassRegister;


/**
 * OC 对象添加拦截器.
 */
@protocol ObjectPushInterceptor <NSObject>


@required

- (BOOL)interceptPush:(LuaScriptEngine*)engine Object:(NSObject*)object;

@end


/**
 * 类注册管理器.
 */
@protocol RegisterClassManager


@required
- (void)addBridgedClass:(Class)clazz ClassName:(NSString*)className;
- (NSString*)getBridgedClassName:(Class)clazz;

@end


/**
 * Lua 脚本引擎.
 */
@interface LuaScriptEngine : NSObject <ObjectPushInterceptor, RegisterClassManager> {

    LuaState* mLuaState;
    LuaFunctionRegister* mFunctionRegister;
    LuaConstVariableRegister* mVariableRegister;
    LuaGlobalClassRegister* mGlobalClassRegister;
    LuaGlobalModuleRegister* mGlobalModuleRegister;
    LuaPlainDataClassRegister* mPlainDataClassRegister;
}


- (void)initialize;
- (void)close;
- (LuaState*)getLuaState;

- (void)createRegisters;

- (LuaObject*)getModuleFunction:(NSString*)moduleName
                   FunctionName:(NSString*)functionName;

/**
 * register helper
 */
- (void)registerVariable:(NSString*)name Object:(NSObject*)object;
- (BOOL)registerModuleVariable:(NSString*)moduleName
                       VarName:(NSString*)name Variable:(NSObject*)object;

- (void)registerFunction:(NSString*)name Function:(LuaBridgedFunction*)function;
- (void)registerFunction:(NSString*)name Invoker:(BridgedFunctionInvoker)invoker;
- (BOOL)registerModuleFunction:(NSString*)moduleName
                  FunctionName:(NSString*)name
                      Function:(LuaBridgedFunction*)function;

- (void)registerModule:(LuaModuleBridge*)module;

- (void)registerClass:(LuaClassBridge*)bridge;
- (void)registerPlainDataClass:(LuaPlainDataBridge*)bridge;

/**
 * 加载脚本
 */
- (int)doScriptFile:(NSString*)file;
- (int)doScriptString:(NSString*)str;
- (int)doScriptBuffer:(NSData*)buffer;
- (int)doScriptBuffer:(NSData*)buffer Length:(int)length;

@end