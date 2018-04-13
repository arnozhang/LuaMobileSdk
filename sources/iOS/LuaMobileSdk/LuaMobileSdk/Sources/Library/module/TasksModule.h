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
#import "LuaModuleBridge.h"


@class BoolObject;


/**
 * 任务分发模块.
 *
 * Tasks
 */
@interface TasksModule : LuaModuleBridge


/**
 * 异步执行.
 */
- (void)async:(NSObject*)callback;

/**
 * Post 到当前线程执行.
 */
- (void)post:(NSObject*)callback;

/**
 * Post 到主线程执行.
 */
- (void)postToMain:(NSObject*)callback;

/**
 * 判断当前是否为主线程.
 */
- (BoolObject*)isMainThread;

/**
 * 设置一个 Timer.
 *
 * @param intervalMillSeconds   时间间隔
 * @param single                是否单次就结束
 * @param delayMillSeconds       延时
 *
 * @return 返回 TimerId
 */
- (int)setTimer:(NSObject*)callback
       Interval:(int)intervalMillSeconds
         Single:(BOOL)single
          Delay:(int)delayMillSeconds;

/**
 * 取消 Timer.
 */
- (void)cancelTimer:(int)timerId;

@end