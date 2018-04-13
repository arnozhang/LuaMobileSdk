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

#import "LogModule.h"
#import "LuaBridgeArgs.h"
#import "BridgeMethodDefine.h"
#import "LogWrapper.h"


static NSString* const TAG = @"LogModule";


#define SDK_BRIDGE_IMPL LogModule

SDK_STATIC_METHOD_IMPL_BEGIN()

SDK_STATIC_RET_0_PARAM_2(v, toObjectFixed, Message, toObjectFixed)
SDK_STATIC_RET_0_PARAM_2(d, toObjectFixed, Message, toObjectFixed)
SDK_STATIC_RET_0_PARAM_2(i, toObjectFixed, Message, toObjectFixed)
SDK_STATIC_RET_0_PARAM_2(w, toObjectFixed, Message, toObjectFixed)
SDK_STATIC_RET_0_PARAM_2(e, toObjectFixed, Message, toObjectFixed)

SDK_STATIC_METHOD_IMPL_END()


@implementation LogModule {
    NSMutableArray<NSString*>* log;
}


- (instancetype)init {
    self = [super init];
    log = [[NSMutableArray alloc] initWithCapacity:2];

    return self;
}

- (NSString*)getBridgeName {
    return @"Log";
}

- (void)registerStaticMethods:(id <MethodsRegHelper>)helper {
    [super registerStaticMethods:helper];

    SDK_STATIC_METHOD(v);
    SDK_STATIC_METHOD(i);
    SDK_STATIC_METHOD(d);
    SDK_STATIC_METHOD(w);
    SDK_STATIC_METHOD(e);
}

- (void)v:(NSObject*)tag Message:(NSObject*)message {
    [self normalizeLog:tag Message:message];
    logV(log[0], log[1]);
}

- (void)d:(NSObject*)tag Message:(NSObject*)message {
    [self normalizeLog:tag Message:message];
    logD(log[0], log[1]);
}

- (void)i:(NSObject*)tag Message:(NSObject*)message {
    [self normalizeLog:tag Message:message];
    logI(log[0], log[1]);
}

- (void)w:(NSObject*)tag Message:(NSObject*)message {
    [self normalizeLog:tag Message:message];
    logW(log[0], log[1]);
}

- (void)e:(NSObject*)tag Message:(NSObject*)message {
    [self normalizeLog:tag Message:message];
    logE(log[0], log[1]);
}

- (void)normalizeLog:(NSObject*)tag Message:(NSObject*)message {
    if (message != nil) {
        if (tag != nil) {
            log[0] = [NSString stringWithFormat:@"%@", tag];
        } else {
            log[0] = TAG;
        }

        log[1] = [NSString stringWithFormat:@"%@", message];
    } else {
        log[0] = TAG;
        log[1] = [NSString stringWithFormat:@"%@", tag];
    }
}

@end