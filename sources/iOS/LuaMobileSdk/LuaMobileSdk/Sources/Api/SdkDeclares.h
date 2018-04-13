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


#define sdk_weakify(variable) \
    __weak typeof(variable) _SdkWeaked_##variable = variable


#define __SDK_STRONGIFY(strongName, variable) \
    _Pragma("clang diagnostic push") \
    _Pragma("clang diagnostic ignored \"-Wshadow\"") \
    __strong typeof(variable) strongName = _SdkWeaked_##variable; \
    _Pragma("clang diagnostic pop")


#define sdk_strongify(variable) __SDK_STRONGIFY(variable, variable)


#define sdk_weakify_self() sdk_weakify(self)

#define sdk_strongify_self() __SDK_STRONGIFY(self, self)

