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

#import "BaseWithLibraryTest.h"


@interface PlainDataTest : BaseWithLibraryTest

@end


@implementation PlainDataTest


- (void)testList {
    NSMutableArray<NSObject*>* list = [NSMutableArray new];
    [list insertObject:@(10) atIndex:0];
    [list insertObject:@(90.98) atIndex:1];
    [list insertObject:@"hello list" atIndex:2];

    [mEngine registerVariable:@"list" Object:list];
    [mEngine doScriptString:@"Log.e(TAG, list[0])"];
    [mEngine doScriptString:@"Log.e(TAG, list[1])"];
    [mEngine doScriptString:@"Log.e(TAG, list[2])"];
    [mEngine doScriptString:@"Log.e(TAG, 'list.size = ' .. list.size .. ', #list = ' .. #list)"];

    [mEngine doScriptString:@"first = list[0]"];
    XCTAssertEqual(10, [mLuaState[@"first"] toInt]);
}

- (void)testMap {
    NSString* name = @"arnozhang";
    NSString* email = @"zyfgood12@163.com";

    NSMutableDictionary<NSString*, NSObject*>* map = [NSMutableDictionary new];
    map[@"name"] = name;
    map[@"address"] = @"HangZhou@China";

    [mEngine registerVariable:@"map" Object:map];
    [mEngine doScriptString:[NSString stringWithFormat:@"map.email = '%@'", email]];
    [mEngine doScriptString:@"Log.e(TAG, 'map.name = ' .. map.name)"];
    [mEngine doScriptString:@"Log.e(TAG, 'map.address = ' .. map.address)"];
    [mEngine doScriptString:@"Log.e(TAG, 'map.email = ' .. map.email)"];

    [mEngine doScriptString:@"name = map.name"];
    XCTAssertEqualObjects(name, [mLuaState[@"name"] toString]);

    [mEngine doScriptString:@"email = map.email"];
    XCTAssertEqualObjects(email, [mLuaState[@"email"] toString]);
}

@end
