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
#import "LuaBridgeArgs.h"


typedef NSObject* (^BridgedFunctionInvoker)(LuaScriptEngine* engine, LuaBridgeArgs* args);


@class LuaBridgedFunction;

/**
 * Bridge OC 方法到 Lua.
 */
@interface LuaBridgedFunction : NSObject


- (instancetype)initWithEngine:(LuaScriptEngine*)engine;
- (instancetype)initWithInvoker:(BridgedFunctionInvoker)invoker;
- (instancetype)initWith:(LuaScriptEngine*)engine Invoker:(BridgedFunctionInvoker)invoker;


- (LuaBridgedFunction*)setScriptEngine:(LuaScriptEngine*)engine;
- (LuaBridgedFunction*)setInvokeHandler:(BridgedFunctionInvoker)invoker;

- (LuaBridgedFunction*)setFunctionName:(NSString*)name;
- (NSString*)getFunctionName;

- (LuaScriptEngine*)getScriptEngine;

/**
 * 调用.
 */
- (NSObject*)invoke;
- (NSObject*)invokeWithArgs:(LuaBridgeArgs*)args;

/**
 * 给一次自主设定参数转换规则的机会.
 */
- (LuaBridgeArgs*)createBridgeArgs;

/**
 * 内部调用.你需要重写它来实现 Bridge 调用.
 *
 * @returns 返回值个数
 */
- (NSObject*)invokeInternal:(LuaBridgeArgs*)args;

@end