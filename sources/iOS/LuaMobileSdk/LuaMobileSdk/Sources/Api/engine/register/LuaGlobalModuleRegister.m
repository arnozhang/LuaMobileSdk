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

#import "LuaGlobalModuleRegister.h"
#import "LuaState.h"
#import "LuaModuleBridge.h"


@implementation LuaGlobalModuleRegister


- (void)registerModule:(LuaModuleBridge*)module {
    module.env = [[LuaMethodInvokeEnv alloc] init:module Reg:self];
    [self holdBridge:module];

    // temp = {}
    [mLuaState newTable];

    // _G.ModuleName = temp
    [mLuaState pushIndexValueToTop:-1];
    [mLuaState setGlobal:[module getBridgeName]];

    // register const static vars.
    [module registerStaticVariables:self];

    // register static methods.
    [module registerStaticMethods:self];

    // pop temp
    [mLuaState pop:1];
}

@end