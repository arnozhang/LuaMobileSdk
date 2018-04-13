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
#import <LuaMobileSdk/LuaClassBridge.h>
#import <LuaMobileSdk/BridgeMethodDefine.h>


@interface View : NSObject


- (void)setId:(NSString*)id;
- (NSString*)getId;

@end


@implementation View {
    NSString* mId;
}


- (void)setId:(NSString*)id {
    mId = id;
}

- (NSString*)getId {
    return mId;
}

@end


@interface ViewBridge : LuaClassBridge<View*>


- (void)setId:(View*)view Id:(NSString*)id;
- (NSString*)getId:(View*)view;

@end


#define SDK_BRIDGE_IMPL ViewBridge

SDK_METHOD_IMPL_BEGIN(View)

SDK_PROPERTY_IMPL(Id, toStringFixed)

SDK_METHOD_IMPL_END()


const int g_width_fill = 10;


@implementation ViewBridge


- (NSString*)getBridgeName {
    return @"View";
}

- (Class)getDataClass {
    return [View class];
}

- (void)registerStaticVariables:(id <StaticVariablesRegHelper>)helper {
    [super registerStaticVariables:helper];

    [helper registerInt:@"width_fill" Value:g_width_fill];
}

- (void)registerStaticMethods:(id <MethodsRegHelper>)helper {
    [super registerStaticMethods:helper];
}

- (void)registerInstanceMethods:(id <MethodsRegHelper>)helper {
    [super registerInstanceMethods:helper];

    SDK_PROPERTY(Id);
}

- (NSObject*)createInstance:(LuaScriptEngine*)engine Args:(LuaBridgeArgs*)args {
    return [View new];
}

- (void)setId:(View*)view Id:(NSString*)id {
    [view setId:id];
}

- (NSString*)getId:(View*)view {
    return [view getId];
}

@end


@interface ClassTest : BaseWithLibraryTest

@end


@implementation ClassTest


- (void)initialize {
    [super initialize];
    [mEngine registerClass:[ViewBridge new]];
}

- (void)testView {
    NSString* viewId = @"arnozhang - view";
    [mEngine doScriptString:@"view = View()"];
    [mEngine doScriptString:[NSString stringWithFormat:@"view.setId('%@')", viewId]];
    [mEngine doScriptString:@"id = view.getId()"];
    [mEngine doScriptString:@"Log.i(TAG, 'view.getId = ' .. id)"];

    XCTAssertEqualObjects(viewId, [mLuaState[@"id"] toString]);
}

- (void)testViewFromAndroid {
    View* view = [View new];
    [view setId:@"view from Android"];
    [mEngine registerVariable:@"view1" Object:view];
    [mEngine doScriptString:@"Log.e(TAG, 'view1.getId = ' .. view1.getId())"];
    [mEngine doScriptString:@"id = view1.getId()"];

    XCTAssertEqualObjects([view getId], [mLuaState[@"id"] toString]);
}

- (void)testStaticFields {
    [mEngine doScriptString:@"width = View.width_fill"];
    XCTAssertEqual(g_width_fill, [mLuaState[@"width"] toInt]);
}

@end
