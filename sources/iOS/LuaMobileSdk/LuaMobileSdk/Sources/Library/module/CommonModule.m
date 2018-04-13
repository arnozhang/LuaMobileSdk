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

#import <UIKit/UIKit.h>
#import "CommonModule.h"
#import "LuaBridgeArgs.h"
#import "BridgeMethodDefine.h"


static const int platform_android = 1;
static const int platform_ios = 2;

static const BOOL isAndroid = NO;
static const BOOL isiOS = YES;
static const int platform = platform_ios;


#define SDK_BRIDGE_IMPL CommonModule

SDK_STATIC_METHOD_IMPL_BEGIN()

SDK_STATIC_RET_1_PARAM_0(getScreenWidth)
SDK_STATIC_RET_1_PARAM_0(getScreenHeight)

SDK_STATIC_METHOD_IMPL_END()


@implementation CommonModule {
    int mScreenWidth;
    int mScreenHeight;
    float mDensity;
}


- (instancetype)init {
    if (self = [super init]) {
        UIScreen* screen = [UIScreen mainScreen];
        CGSize size = [screen bounds].size;
        mScreenWidth = (int) size.width;
        mScreenHeight = (int) size.height;
        mDensity = screen.scale;
    }

    return self;
}

- (NSString*)getBridgeName {
    return @"Common";
}

- (void)registerStaticVariables:(id <StaticVariablesRegHelper>)helper {
    [super registerStaticVariables:helper];

    SDK_STATIC_VARIABLE_INT(platform);
    SDK_STATIC_VARIABLE_INT(platform_ios);
    SDK_STATIC_VARIABLE_INT(platform_android);

    SDK_STATIC_VARIABLE_BOOL(isiOS);
    SDK_STATIC_VARIABLE_BOOL(isAndroid);
}

- (void)registerStaticMethods:(id <MethodsRegHelper>)helper {
    [super registerStaticMethods:helper];

    SDK_STATIC_METHOD(getScreenWidth);
    SDK_STATIC_METHOD(getScreenHeight);
}

- (NSNumber*)getScreenWidth {
    return @(mScreenWidth * mDensity);
}

- (NSNumber*)getScreenHeight {
    return @(mScreenHeight * mDensity);
}

@end