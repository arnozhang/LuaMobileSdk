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

#import "LuaBridgeLog.h"


static const char* const TAG = "LuaLogger";


@implementation LuaBridgeLog


+ (void)v:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message {
    [LuaBridgeLog callLogFunction:ls FunctionName:"v" Tag:tag Message:message];
}

+ (void)d:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message {
    [LuaBridgeLog callLogFunction:ls FunctionName:"d" Tag:tag Message:message];
}

+ (void)i:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message {
    [LuaBridgeLog callLogFunction:ls FunctionName:"i" Tag:tag Message:message];
}

+ (void)w:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message {
    [LuaBridgeLog callLogFunction:ls FunctionName:"w" Tag:tag Message:message];
}

+ (void)e:(lua_State*)ls Tag:(const char*)tag Message:(const char*)message {
    [LuaBridgeLog callLogFunction:ls FunctionName:"e" Tag:tag Message:message];
}

+ (void)v:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog v:ls Tag:TAG Message:message];
}

+ (void)d:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog d:ls Tag:TAG Message:message];
}

+ (void)i:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog i:ls Tag:TAG Message:message];
}

+ (void)w:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog w:ls Tag:TAG Message:message];
}

+ (void)e:(lua_State*)ls Message:(const char*)message {
    [LuaBridgeLog e:ls Tag:TAG Message:message];
}

+ (void)callLogFunction:(lua_State*)ls
           FunctionName:(const char* const)functionName
                    Tag:(const char*)tag
                Message:(const char*)message {

    lua_getglobal(ls, "Log");
    lua_getfield(ls, -1, functionName);
    lua_remove(ls, -2);

    lua_pushstring(ls, tag);
    lua_pushstring(ls, message);
    lua_call(ls, 2, 0);
}

@end