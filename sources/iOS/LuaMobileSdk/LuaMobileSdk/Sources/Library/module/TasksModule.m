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

#import "TasksModule.h"
#import "LuaObject.h"
#import "BridgeMethodDefine.h"
#import "CommonUtils.h"
#import "BoolObject.h"


static const int invalid_id = -1;


#define SDK_BRIDGE_IMPL TasksModule

SDK_STATIC_METHOD_IMPL_BEGIN()

SDK_STATIC_RET_0_PARAM_1(async, toObjectFixed)
SDK_STATIC_RET_0_PARAM_1(post, toObjectFixed)
SDK_STATIC_RET_0_PARAM_1(postToMain, toObjectFixed)
SDK_STATIC_RET_1_PARAM_0(isMainThread)
SDK_STATIC_RET_0_PARAM_1(cancelTimer, toIntFixed)

SDK_STATIC_METHOD_IMPL_END()


NSObject* _TasksModule_setTimer(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) {
    TasksModule* module = (TasksModule*) env.bridge;
    int timerId = [module setTimer:[args toObjectFixed:1] Interval:[args toIntFixed:2]
                            Single:[args toBool:3] Delay:[args toIntFixed:4]];
    return @(timerId);
}


@implementation TasksModule {
    int mCurrentIndex;
    NSMutableDictionary<NSNumber*, NSTimer*>* mTimers;
    dispatch_queue_t mAsyncQueue;
}


- (instancetype)init {
    if (self = [super init]) {
        mTimers = [[NSMutableDictionary alloc] init];
        mAsyncQueue = dispatch_queue_create("LuaMobileSdk.Tasks", NULL);
    }

    return self;
}

- (NSString*)getBridgeName {
    return @"Tasks";
}

- (void)registerStaticVariables:(id <StaticVariablesRegHelper>)helper {
    [super registerStaticVariables:helper];

    SDK_STATIC_VARIABLE_INT(invalid_id);
}

- (void)registerStaticMethods:(id <MethodsRegHelper>)helper {
    [super registerStaticMethods:helper];

    SDK_STATIC_METHOD(post);
    SDK_STATIC_METHOD(async);
    SDK_STATIC_METHOD(setTimer);
    SDK_STATIC_METHOD(cancelTimer);
    SDK_STATIC_METHOD(postToMain);
    SDK_STATIC_METHOD(isMainThread);
}

- (void)async:(NSObject*)callback {
    if (!mAsyncQueue || ![callback isKindOfClass:[LuaObject class]]) {
        return;
    }

    dispatch_async(mAsyncQueue, ^() {
        LuaObject* cbk = (LuaObject*) callback;
        [cbk call:nil];
    });
}

- (void)post:(NSObject*)callback {
    if (![callback isKindOfClass:[LuaObject class]]) {
        return;
    }

    [CommonUtils dispatchToCurrentQueue:^() {
        LuaObject* cbk = (LuaObject*) callback;
        [cbk call:nil];
    }];
}

- (void)postToMain:(NSObject*)callback {
    if (![callback isKindOfClass:[LuaObject class]]) {
        return;
    }

    [CommonUtils dispatchToMain:^{
        LuaObject* cbk = (LuaObject*) callback;
        [cbk call:nil];
    }];
}

- (BoolObject*)isMainThread {
    return [BoolObject valueOf:[CommonUtils isMainThread]];
}

- (int)setTimer:(NSObject*)callback
       Interval:(int)intervalMillSeconds
         Single:(BOOL)single
          Delay:(int)delayMillSeconds {

    @synchronized (self) {
        return [self setTimerInternal:callback Interval:intervalMillSeconds
                               Single:single Delay:delayMillSeconds];
    }
}

- (int)setTimerInternal:(NSObject*)callback
               Interval:(int)intervalMillSeconds
                 Single:(BOOL)single
                  Delay:(int)delayMillSeconds {

    if (![callback isKindOfClass:[LuaObject class]]) {
        return invalid_id;
    }

    int timerId = [self nextTimerId];
    void (^block)(NSTimer* timer) = ^(NSTimer* timer_) {
        if (single) {
            [timer_ invalidate];
            [mTimers removeObjectForKey:@(timerId)];
        }

        LuaObject* cbk = (LuaObject*) callback;
        [cbk call:@(timerId), nil];
    };

    NSTimer* timer = nil;
    double interval = intervalMillSeconds / 1000.0;
    if (delayMillSeconds > 0) {
        NSDate* date = [[NSDate alloc] initWithTimeIntervalSinceNow:delayMillSeconds / 1000.0];
        timer = [[NSTimer alloc] initWithFireDate:date interval:interval repeats:!single block:block];
    } else {
        timer = [NSTimer scheduledTimerWithTimeInterval:interval repeats:!single block:block];
    }

    mTimers[@(timerId)] = timer;
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    return timerId;
}

- (void)cancelTimer:(int)timerId {
    @synchronized (self) {
        NSTimer* timer = mTimers[@(timerId)];
        if (!timer) {
            return;
        }

        [timer invalidate];
        [mTimers removeObjectForKey:@(timerId)];
    }
}

- (int)nextTimerId {
    return ++mCurrentIndex;
}

- (void)destroy {
    [super destroy];

    mAsyncQueue = nil;

    NSEnumerator<NSTimer*>* timers = [mTimers objectEnumerator];
    for (NSTimer* timer in timers) {
        [timer invalidate];
    }

    [mTimers removeAllObjects];
}

@end