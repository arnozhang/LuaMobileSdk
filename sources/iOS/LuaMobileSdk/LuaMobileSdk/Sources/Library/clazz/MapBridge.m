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

#import "MapBridge.h"


@implementation MapBridge


- (Class)getDataClass {
    return [NSDictionary class];
}

- (NSString*)getBridgeName {
    return @"Map";
}

- (NSObject*)createInstance:(LuaScriptEngine*)engine Args:(LuaBridgeArgs*)args {
    return [[NSMutableDictionary alloc] init];
}

- (NSObject*)getData:(NSObject*)object Name:(NSString*)name {
    NSDictionary* dictionary = (NSDictionary*) object;
    if ([name isEqual:@"size"]) {
        return @([self length:object]);
    }

    return dictionary[name];
}

- (void)setData:(NSObject*)object Name:(NSString*)name Value:(NSObject*)value {
    if ([object isKindOfClass:[NSMutableDictionary class]]) {
        NSMutableDictionary* dictionary = (NSMutableDictionary*) object;
        dictionary[name] = value;
    }
}

- (int)length:(NSObject*)object {
    NSDictionary* dictionary = (NSDictionary*) object;
    return (int) dictionary.count;
}

@end