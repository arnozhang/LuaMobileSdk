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
#import "lua.h"


/**
 * Bridge Log - 供 Lua-C-Function 调用.
 * log level:
 *
 *      v - verbose
 *      d - debug
 *      i - information
 *      w - warning
 *      e - error
 */
@interface LuaBridgeLog : NSObject


+ (void)v:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message;
+ (void)d:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message;
+ (void)i:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message;
+ (void)w:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message;
+ (void)e:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message;

+ (void)v:(lua_State*)ls Message:(const char*)message;
+ (void)d:(lua_State*)ls Message:(const char*)message;
+ (void)i:(lua_State*)ls Message:(const char*)message;
+ (void)w:(lua_State*)ls Message:(const char*)message;
+ (void)e:(lua_State*)ls Message:(const char*)message;

@end