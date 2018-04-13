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
#import "LuaAbstractBridge.h"
#import "LuaAbstractRegister.h"


@class LuaBridgeArgs;
@class LuaBridgedFunction;


/**
 * 方法调用环境包装.
 */
@interface LuaMethodInvokeEnv : NSObject


- (instancetype)init:(LuaAbstractBridge*)bridge_ Reg:(LuaAbstractRegister*)reg_;

@property(weak, nonatomic) LuaAbstractBridge* bridge;
@property(weak, nonatomic) LuaAbstractRegister* reg;

@end


typedef NSObject* (* StaticBridgeMethod)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args);


/**
 * 静态对象注册 Helper.
 */
@protocol StaticVariablesRegHelper


@required
- (void)registerBool:(NSString*)name Value:(BOOL)value;
- (void)registerInt:(NSString*)name Value:(int)value;
- (void)registerLong:(NSString*)name Value:(long)value;
- (void)registerFloat:(NSString*)name Value:(float)value;
- (void)registerDouble:(NSString*)name Value:(double)value;
- (void)registerString:(NSString*)name Value:(NSString*)value;
- (void)registerObject:(NSString*)name Value:(NSObject*)value;
- (void)registerBridgedFunction:(NSString*)name Value:(LuaBridgedFunction*)value;

@end


/**
 * 各种方法注册 Helper.
 */
@protocol MethodsRegHelper


@required

/**
 * 注册实例方法.
 */
- (void)registerInstanceMethod:(LuaAbstractBridge*)bridge
                          Name:(NSString*)name
                        Method:(StaticBridgeMethod)method;

/**
 * 注册静态方法.
 */
- (void)registerStaticMethod:(LuaAbstractBridge*)bridge
                        Name:(NSString*)name
                      Method:(StaticBridgeMethod)method;

@end
