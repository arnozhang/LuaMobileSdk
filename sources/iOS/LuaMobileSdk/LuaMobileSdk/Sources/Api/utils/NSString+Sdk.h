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


@interface NSString (Sdk)


- (BOOL)sdk_isEmpty;
- (BOOL)sdk_isNotEmpty;

+ (BOOL)sdk_isEmpty:(NSString*)str;
+ (BOOL)sdk_isNotEmpty:(NSString*)str;

- (instancetype)sdk_trim;

- (BOOL)sdk_startWithChar:(char)ch;
- (BOOL)sdk_startWith:(NSString*)str;

- (BOOL)sdk_endWithChar:(char)ch;
- (BOOL)sdk_endWith:(NSString*)str;

@end
