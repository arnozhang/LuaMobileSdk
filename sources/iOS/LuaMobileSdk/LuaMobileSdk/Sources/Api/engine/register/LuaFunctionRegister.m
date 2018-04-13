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

#import "LuaFunctionRegister.h"
#import "LuaState.h"


@implementation LuaFunctionRegister


- (void)registerFunction:(NSString*)name Function:(LuaBridgedFunction*)function {
    [function setFunctionName:name];
    [mLuaState pushBridgedFunction:function];
    [mLuaState setGlobal:name];
}

- (BOOL)registerModuleFunction:(NSString*)moduleName
                  FunctionName:(NSString*)name
                      Function:(LuaBridgedFunction*)function {

    if (moduleName == nil || moduleName.length <= 0) {
        [self registerFunction:name Function:function];
        return YES;
    }

    [mLuaState getGlobal:moduleName];
    if ([mLuaState isNil:-1]) {
        // new module
        [mLuaState pop:1];
        [mLuaState newTable];

        // _T.moduleName = temp
        [mLuaState pushIndexValueToTop:-1];
        [mLuaState setGlobal:moduleName];
    } else if (![mLuaState isTable:-1]) {
        return NO;
    }

    // _G.moduleName.functionName = function
    NSString* detailName = [NSString stringWithFormat:@"%@.%@", moduleName, name];
    [function setFunctionName:detailName];
    [mLuaState pushBridgedFunction:function];
    [mLuaState setField:-2 Key:name];

    [mLuaState pop:1];
    return YES;
}

@end