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

#import <Foundation/Foundation.h>
#import "LuaClassBridge.h"


/**
 * PlainData Bridge.
 */
@interface LuaPlainDataBridge : LuaClassBridge


/**
 * PlainData 的成员数据 Getter.
 *
 *      object.name
 */
- (NSObject*)getData:(NSObject*)object Name:(NSString*)name;

/**
 * PlainData 的成员数据 Setter.
 *
 *      object.name = value
 */
- (void)setData:(NSObject*)object Name:(NSString*)name Value:(NSObject*)value;

/**
 * 获取 PlainData 个数(长度).
 *
 *      #object
 */
- (int)length:(NSObject*)object;

@end
