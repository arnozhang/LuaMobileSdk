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


@class LuaScriptEngine;


/**
 * 基础 Core 定义.
 */
@interface LuaCore : NSObject {
    LuaScriptEngine* mScriptEngine;
}


/**
 * 初始化.
 */
- (void)initialize;

/**
 * 释放 Core 相关资源.释放后 Core 将不可使用.
 */
- (void)close;

/**
 * 创建脚本引擎. 给子业务一次替换脚本引擎的机会.
 */
- (LuaScriptEngine*)createScriptEngine;

/**
 * 获取脚本引擎包装.
 */
- (LuaScriptEngine*)getScriptEngine;

/**
 * 注册内置模块.
 */
- (void)registerBuildInModules;

/**
 * 注册内置类.
 */
- (void)registerBuildInClasses;

@end