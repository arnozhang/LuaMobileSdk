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

#import "LuaState.h"
#import "LuaDeclares.h"
#import "LuaBridgeUtils.h"
#import "LuaGlobalClassRegister.h"


int sdk_ClassMemberFunctionInvokeClosure(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    LuaState* state = (__bridge LuaState*) wrapper->luaState;

    const int objIndex = lua_upvalueindex(2);
    const int nameIndex = lua_upvalueindex(3);

    if (![state isObject:objIndex]) {
        return 0;
    }

    if (![state getMetaTable:objIndex]) {
        return 0;
    }

    int argsCount = [state getTop];

    // arg1, arg2, ... obj.meta, obj.meta.__oc_member_funcs
    lua_getfield(ls, -1, __oc_member_funcs);

    // arg1, arg2, ... obj.meta, obj.meta.__oc_member_funcs, methodClosure
    [state pushIndexValueToTop:nameIndex];
    [state rawGet:-2];

    // obj, arg1, arg2
    [state pushIndexValueToTop:objIndex];
    [state insert:1];

    // methodClosure, obj, arg1, arg2, ... obj.meta, obj.meta.__oc_member_funcs
    [state insert:1];

    // methodClosure, obj, arg1, arg2, ...
    [state pop:2];

    // placeholder
    // methodClosure, nil obj, arg1, arg2, ...
    //
    [state pushNil];
    [state insert:2];

    [state call:argsCount + 1 ResultCount:LUA_MULTRET];

    return [state getTop];
}

int sdk_ClassIndexInvoker(lua_State* ls) {
    MethodInvokeWrapper* wrapper = sdk_getInvokerWrapper(ls);
    if (!wrapper) {
        return 0;
    }

    int wrapperIndex = lua_upvalueindex(1);
    LuaState* state = (__bridge LuaState*) wrapper->luaState;

    // a.__index = function(a, name) ... end
    //                      1   2
    //
    // wrapper -> upvalue(1)
    [state pushIndexValueToTop:wrapperIndex];

    // obj -> upvalue(2)
    [state pushIndexValueToTop:1];

    // name -> upvalue(3)
    [state pushIndexValueToTop:2];

    [state pushCClosure:&sdk_ClassMemberFunctionInvokeClosure UpValueCount:3];

    return 1;
}


@implementation LuaGlobalClassRegister


- (void)destroy {
    [super destroy];
}

- (void)registerClass:(LuaClassBridge*)bridge {
    if (![super registerClassPrepare:bridge]) {
        return;
    }

    [self registerClassMethods:bridge];

    // Class meta
    [mLuaState pop:2];
}

- (void)bindMetaMethods:(LuaClassBridge*)bridge {
    [super bindMetaMethods:bridge];

    // meta.__index = index
    [self newWrappedClosureAtTop:bridge Method:nil Closure:&sdk_ClassIndexInvoker];
    [mLuaState setField:-2 CStrKey:__index];
}

- (void)registerClassMethods:(LuaClassBridge*)bridge {
    // register public methods.
    // meta.__oc_member_funcs = {}
    [mLuaState newTable];

    // register member methods.
    [bridge registerInstanceMethods:self];

    [mLuaState setField:-2 CStrKey:__oc_member_funcs];
}

@end