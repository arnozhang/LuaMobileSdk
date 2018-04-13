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

#import "NSString+Sdk.h"


@implementation NSString (Sdk)


- (BOOL)sdk_isEmpty {
    return self.length <= 0;
}

- (BOOL)sdk_isNotEmpty {
    return ![self sdk_isEmpty];
}

+ (BOOL)sdk_isEmpty:(NSString*)str {
    return str && [str sdk_isEmpty];
}

+ (BOOL)sdk_isNotEmpty:(NSString*)str {
    return str && [str sdk_isNotEmpty];
}

- (instancetype)sdk_trim {
    return [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (BOOL)sdk_startWithChar:(char)ch {
    return [self sdk_isNotEmpty] && [self characterAtIndex:0] == ch;
}

- (BOOL)sdk_startWith:(NSString*)str {
    if ([NSString sdk_isEmpty:str]) {
        return NO;
    }

    NSRange range = [self rangeOfString:str];
    return range.location == 0;
}

- (BOOL)sdk_endWithChar:(char)ch {
    return [self sdk_isNotEmpty] && [self characterAtIndex:self.length - 1] == ch;
}

- (BOOL)sdk_endWith:(NSString*)str {
    if ([NSString sdk_isEmpty:str]) {
        return NO;
    }

    NSRange range = [self rangeOfString:str];
    return range.location == self.length - str.length;
}

@end
