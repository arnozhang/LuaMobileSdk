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


@class LuaAbstractBridge;
@class LuaScriptEngine;
@class LuaState;


/**
 * 抽象模块注册器.
 */
@interface LuaAbstractRegister : NSObject {

    LuaState* mLuaState;
    LuaScriptEngine* mScriptEngine;
}


- (instancetype)initWithEngine:(LuaScriptEngine*)engine;

- (void)holdBridge:(LuaAbstractBridge*)bridge;

- (void)destroy;

@end