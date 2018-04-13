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
#import "LuaPlainDataBridge.h"
#import "LuaAbstractClassRegister.h"


@class LuaScriptEngine;


/**
 * 普通数据模型注册器.
 *
 * 注册后可支持如下这种下标数据访问,适合 Json/Array/Map 等等数据类型.
 *
 *      obj = Obj();  print(obj.name); obj.name = xxx;
 */
@interface LuaPlainDataClassRegister : LuaAbstractClassRegister


/**
 * 注册一个全局 PlainData 类.
 */
- (void)registerPlainDataClass:(LuaPlainDataBridge*)bridge;

@end