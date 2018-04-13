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
#import <XCTest/XCTest.h>
#import <LuaMobileSdk/LuaState.h>
#import <LuaMobileSdk/LuaScriptEngine.h>
#import <LuaMobileSdk/LuaCore.h>
#import <LuaMobileSdk/LuaObject.h>


@interface BaseTest : XCTestCase {
    LuaCore* mCore;
    LuaState* mLuaState;
    LuaScriptEngine* mEngine;

    NSString* TAG;
}


- (void)initialize;
- (void)unInitialize;

- (LuaCore*)createLuaCore;

@end