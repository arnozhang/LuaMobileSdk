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
#import "LuaAbstractBridge.h"
#import "LuaWithMemberBridge.h"


/**
 * 类 Bridge.
 */
@interface LuaClassBridge<Type:NSObject*> : LuaWithMemberBridge


/**
 * 创建对象.
 */
- (NSObject*)createInstance:(LuaScriptEngine*)engine Args:(LuaBridgeArgs*)args;

/**
 * 实例 Class.
 */
- (Class)getDataClass;

/**
 * 注册类实例方法.
 */
- (void)registerInstanceMethods:(id <MethodsRegHelper>)helper;

@end