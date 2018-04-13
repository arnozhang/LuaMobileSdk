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

#import "BaseTest.h"
#import "BaseWithLibraryTest.h"
#import <LuaMobileSdk/LuaModuleBridge.h>
#import <LuaMobileSdk/BridgeMethodDefine.h>
#import <LuaMobileSdk/LogWrapper.h>


static const int g_intValue = 180;
static NSString* const g_stringValue = @"hello";


@interface TestModule : LuaModuleBridge


- (NSNumber*)intValue;

- (const NSString*)stringValue;

@end


#define SDK_BRIDGE_IMPL TestModule

SDK_STATIC_METHOD_IMPL_BEGIN()

SDK_STATIC_RET_1_PARAM_0(intValue)
SDK_STATIC_RET_1_PARAM_0(stringValue)

SDK_STATIC_METHOD_IMPL_END()

@implementation TestModule


- (NSString*)getBridgeName {
    return @"Test";
}

- (void)registerStaticVariables:(id <StaticVariablesRegHelper>)helper {
    [super registerStaticVariables:helper];

    SDK_STATIC_VARIABLE_INT(g_intValue);
}

- (void)registerStaticMethods:(id <MethodsRegHelper>)helper {
    [super registerStaticMethods:helper];

    SDK_STATIC_METHOD(intValue);
    SDK_STATIC_METHOD(stringValue);
}

- (NSNumber*)intValue {
    return @(180);
}

- (NSString*)stringValue {
    return g_stringValue;
}

@end


@interface ModuleTest : BaseWithLibraryTest

@end


@implementation ModuleTest


- (void)initialize {
    [super initialize];

    [mEngine registerModule:[TestModule new]];
}

- (void)testLog {
    [mEngine doScriptString:@"Log.v(TAG, 'v - log test')"];
    [mEngine doScriptString:@"Log.d(TAG, 'd - log test')"];
    [mEngine doScriptString:@"Log.i(TAG, 'i - log test')"];
    [mEngine doScriptString:@"Log.w(TAG, 'w - log test')"];
    [mEngine doScriptString:@"Log.e(TAG, 'e - log test')"];
}

- (void)testScreen {
    [mEngine doScriptString:@"Log.i(TAG, 'screenWidth = ' .. Common.getScreenWidth() .. ' screenHeight = ' .. Common.getScreenHeight())"];
}

- (void)testModule {
    [mEngine doScriptString:@"intValue = Test.intValue()"];
    [mEngine doScriptString:@"stringValue = Test.stringValue()"];
    [mEngine doScriptString:@"Test_g_intValue = Test.g_intValue"];

    NSString* string = [mLuaState[@"stringValue"] toString];
    XCTAssertEqual(g_intValue, [mLuaState[@"Test_g_intValue"] toInt]);
    XCTAssertEqual(g_intValue, [mLuaState[@"intValue"] toInt]);
    XCTAssertEqualObjects(g_stringValue, string);

    logI(TAG, [NSString stringWithFormat:@"stringValue = %@", string]);
}

@end
