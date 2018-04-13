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
#import "LuaAbstractRegister.h"
#import "LuaScriptEngine.h"
#import "LuaClassBridge.h"
#import "lua.h"


@protocol RegisterClassManager;


/**
 * 类抽象注册器,用于 Class/PlainDataClass 等注册.
 */
@interface LuaAbstractClassRegister : LuaAbstractRegister <MethodsRegHelper, StaticVariablesRegHelper> {

    id <RegisterClassManager> mRegisterClassManager;
}


- (instancetype)initWithEngine:(LuaScriptEngine*)engine
        ClassManager:(id <RegisterClassManager>)manager;

/**
 * prepare for register class.
 *
 * after return, stack as follows:
 *
 * Class    meta
 *   -2     -1
 */
- (BOOL)registerClassPrepare:(LuaClassBridge*)bridge;

/**
 * bind meta methods.
 *
 * current -1 is metatable.
 */
- (void)bindMetaMethods:(LuaClassBridge*)bridge;

/**
 * create method wrapper closure at stack top.
 */
- (void)newWrappedClosureAtTop:(LuaAbstractBridge*)bridge
                        Method:(void*)method
                       Closure:(lua_CFunction)closure;

@end