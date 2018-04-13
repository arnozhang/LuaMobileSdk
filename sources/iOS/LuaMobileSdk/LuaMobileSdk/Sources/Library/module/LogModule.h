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


/**
 * Log 模块.
 *
 * Log
 *
 * Log.v([tag,] message)    - verbose
 * Log.d([tag,] message)    - debug
 * Log.i([tag,] message)    - information
 * Log.w([tag,] message)    - warning
 * Log.e([tag,] message)    - error
 */
@interface LogModule : LuaModuleBridge


- (void)registerStaticMethods:(id <MethodsRegHelper>)helper;


- (void)v:(NSObject*)tag Message:(NSObject*)message;
- (void)d:(NSObject*)tag Message:(NSObject*)message;
- (void)i:(NSObject*)tag Message:(NSObject*)message;
- (void)w:(NSObject*)tag Message:(NSObject*)message;
- (void)e:(NSObject*)tag Message:(NSObject*)message;

@end