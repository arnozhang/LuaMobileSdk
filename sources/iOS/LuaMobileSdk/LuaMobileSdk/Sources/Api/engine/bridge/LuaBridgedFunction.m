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

#import "LuaBridgedFunction.h"


@implementation LuaBridgedFunction {
    NSString* mFunctionName;
    LuaScriptEngine* mScriptEngine;
    BridgedFunctionInvoker mInvoker;
}


- (instancetype)initWithEngine:(LuaScriptEngine*)engine {
    if (self = [super init]) {
        mScriptEngine = engine;
    }

    return self;
}

- (instancetype)initWithInvoker:(BridgedFunctionInvoker)invoker {
    if (self = [super init]) {
        mInvoker = invoker;
    }

    return self;
}

- (instancetype)initWith:(LuaScriptEngine*)engine Invoker:(BridgedFunctionInvoker)invoker {
    if (self = [super init]) {
        mScriptEngine = engine;
        mInvoker = invoker;
    }

    return self;
}

- (LuaBridgedFunction*)setScriptEngine:(LuaScriptEngine*)engine {
    mScriptEngine = engine;
    return self;
}

- (LuaBridgedFunction*)setInvokeHandler:(BridgedFunctionInvoker)invoker {
    mInvoker = invoker;
    return self;
}

- (LuaBridgedFunction*)setFunctionName:(NSString*)name {
    mFunctionName = name;
    return self;
}

- (NSString*)getFunctionName {
    return mFunctionName;
}

- (LuaScriptEngine*)getScriptEngine {
    return mScriptEngine;
}

- (NSObject*)invoke {
    if (!mScriptEngine) {
        NSLog(@"Cannot invoke LuaBridgedFunction: [%@] with LuaState == NULL!", mFunctionName);
        return nil;
    }

    LuaBridgeArgs* args = [self createBridgeArgs];
    return [self invokeWithArgs:args];
}

- (NSObject*)invokeWithArgs:(LuaBridgeArgs*)args {
    return [self invokeInternal:args];
}

- (NSObject*)invokeInternal:(LuaBridgeArgs*)args {
    if (mInvoker) {
        return mInvoker(mScriptEngine, args);
    }

    return nil;
}

- (LuaBridgeArgs*)createBridgeArgs {
    return [[LuaBridgeArgs alloc] init:mScriptEngine];
}

@end