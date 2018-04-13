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
#import "UIKit/UIKit.h"


/**
 * 基础 Utils 方法定义.
 */
@interface CommonUtils : NSObject


/**
 * 分发到主线程执行.
 */
+ (void)dispatchToMain:(dispatch_block_t)block;

/**
 * 分发到当前队列执行.
 */
+ (void)dispatchToCurrentQueue:(dispatch_block_t)block;

/**
 * 判断当前队列是否为主线程.
 */
+ (BOOL)isMainThread;

/**
 * Json 解析相关.
 */
+ (NSDictionary*)parseJsonObject:(NSString*)str;
+ (NSArray*)parseJsonArray:(NSString*)str;
+ (id)parseJsonContent:(NSString*)str;

@end
