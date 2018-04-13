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

#import <LuaMobileSdk/LogWrapper.h>
#import "BaseWithLibraryTest.h"


@interface FunctionTest : BaseWithLibraryTest

@end


@implementation FunctionTest


- (void)testHello {
    [mEngine registerFunction:@"hello"
                      Invoker:^NSObject*(LuaScriptEngine* engine, LuaBridgeArgs* args) {
                          logE(TAG, [args toString:0]);
                          return nil;
                      }];

    [mEngine doScriptString:@"hello('hello arnozhang!')"];
}

- (void)testAdd {
    const int a = 100;
    const int b = 90;

    [mEngine registerVariable:@"a" Object:@(a)];
    [mEngine registerVariable:@"b" Object:@(b)];
    [mEngine registerFunction:@"add"
                      Invoker:^NSObject*(LuaScriptEngine* engine, LuaBridgeArgs* args) {
                          int result = [args toIntFixed:0] + [args toIntFixed:1];
                          return @(result);
                      }];

    [mEngine doScriptString:@"result = add(a, b)"];
    [mEngine doScriptString:@"Log.e(TAG, 'a + b = '.. result)"];
    XCTAssertEqual(a + b, [mLuaState[@"result"] toInt]);
}

- (void)testCallFunction {
    const int a = 100;
    const int b = 90;

    [mEngine doScriptString:@"function sub(a, b) return a - b end"];
    LuaBridgeArgs* ret = [mLuaState[@"sub"] callWithMultiReturn:@(a), @(b), nil];
    if (ret) {
        XCTAssertEqual(a - b, [ret toIntFixed:0]);
    } else {
        XCTAssertTrue(NO);
    }
}

@end
