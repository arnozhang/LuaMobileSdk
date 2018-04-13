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
#import "BoolObject.h"
#import "LuaBridgedFunction.h"
#import "LuaHoldedBridgeArgs.h"


@interface ArgsInfo : NSObject


@property(nonatomic) int type;
@property(nonatomic) NSObject* object;
@end


@implementation ArgsInfo
@end


@implementation LuaHoldedBridgeArgs {
    int count;
    NSMutableArray<ArgsInfo*>* mHoldedArgs;
}


- (instancetype)init:(LuaScriptEngine*)engine Count:(int)count_ {
    if (self = [super init:engine]) {
        count = count_;
        mHoldedArgs = [[NSMutableArray alloc] initWithCapacity:(NSUInteger) count];

        for (int i = 0; i < count; ++i) {
            int index = [mInitializer convertIndex:i];

            ArgsInfo* info = [ArgsInfo new];
            info.type = [mLuaState getType:index];
            info.object = [mLuaState toObjectFixed:index];
            [mHoldedArgs addObject:info];
        }
    }

    return self;
}

- (int)convertIndex:(int)index {
    return index - count;
}

- (int)getCount {
    return count;
}

- (int)getType:(int)index {
    return mHoldedArgs[(NSUInteger) index].type;
}

- (BOOL)toBoolDirectly:(int)index {
    NSObject* object = mHoldedArgs[(NSUInteger) index].object;
    if ([object isKindOfClass:[BoolObject class]]) {
        return [(BoolObject*) object value];
    }

    return NO;
}

- (double)toNumberDirectly:(int)index {
    NSObject* object = mHoldedArgs[(NSUInteger) index].object;
    if ([object isKindOfClass:[NSNumber class]]) {
        return [(NSNumber*) object doubleValue];
    }

    return 0;
}

- (NSString*)toStringDirectly:(int)index {
    NSObject* object = mHoldedArgs[(NSUInteger) index].object;
    if ([object isKindOfClass:[NSString class]]) {
        return (NSString*) object;
    }

    return nil;
}

- (NSObject*)toObjectDirectly:(int)index {
    return mHoldedArgs[(NSUInteger) index].object;
}

- (LuaBridgedFunction*)toBridgedFunctionDirectly:(int)index {
    NSObject* object = mHoldedArgs[(NSUInteger) index].object;
    return object && [object isKindOfClass:[LuaBridgedFunction class]]
            ? (LuaBridgedFunction*) object : nil;
}

@end