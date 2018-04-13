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
#import "LuaTypeChecker.h"
#import "LuaBridgeArgsInitializer.h"
#import "LuaObjectGetter.h"


@class LuaState;
@class LuaScriptEngine;


/**
 * Bridge 调用各处参数绑定器.
 */
@interface LuaBridgeArgs : LuaObjectGetter <LuaBridgeArgsInitializer> {

    LuaState* mLuaState;
    LuaScriptEngine* mScriptEngine;
    id <LuaBridgeArgsInitializer> mInitializer;
}


- (instancetype)init:(LuaScriptEngine*)engine;
- (instancetype)init:(LuaScriptEngine*)engine
     ArgsInitializer:(id <LuaBridgeArgsInitializer>)initializer;

/**
 * 获取脚本引擎.
 */
- (LuaScriptEngine*)getScriptEngine;

/**
 * 获取参数个数.
 */
- (int)getCount;

/**
 * 参数个数为空判断.
 */
- (BOOL)isEmpty;
- (BOOL)isNotEmpty;

/**
 * 是否至少有 N 个参数.
 */
- (BOOL)atLeastHas:(int)count;

/**
 * 索引转换. OC 索引转为 Lua 索引.
 */
- (int)convertIndex:(int)index;

@end