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
#import "lua.h"
#import "RegisterHelpers.h"


@class LuaBridgedFunction;


/**
 * object info in lua userData.
 */
typedef struct {
    int type;
    void* object;
} _ObjectInfo;


/**
 * bridged method invoke info wrapper.
 */
typedef struct {
    void* luaState;
    void* env;
    StaticBridgeMethod method;
} MethodInvokeWrapper;


/**
 * get closure-method-invoker wrapper info.
 */
MethodInvokeWrapper* sdk_getInvokerWrapper(lua_State* ls);


@interface LuaBridgeUtils : NSObject


/**
 * 获取 Bridge 对象类型. (OC 对象、BridgeFunction etc.)
 *
 * @return 0 - 如果是非 Bridge 对象
 */
+ (int)getBridgedType:(lua_State*)ls Index:(int)index;

/**
 * Push 一个 OC 对象到 Lua.
 */
+ (BOOL)pushObject:(lua_State*)ls Object:(NSObject*)object;

/**
 * Push 一个 Bridge 方法到 Lua.
 */
+ (BOOL)pushBridgedFunction:(lua_State*)ls Function:(LuaBridgedFunction*)function;

/**
 * 将 index 索引处的对象转为 OC 对象.
 */
+ (NSObject*)toObject:(lua_State*)ls Index:(int)index;

/**
 * 将 index 索引处的对象转为 Bridge 方法对象.
 */
+ (LuaBridgedFunction*)toBridgedFunction:(lua_State*)ls Index:(int)index;

/**
 * 错误通知.
 */
+ (void)luaError:(lua_State*)ls Message:(const char*)message;

@end