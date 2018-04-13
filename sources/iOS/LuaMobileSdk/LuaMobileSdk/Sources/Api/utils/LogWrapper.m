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

#import "LogWrapper.h"


void logInternal(NSString* tag, NSString* level, NSString* fmt, va_list ap) {
    NSLog(@"%@/%@:  %@", tag, level, [[NSString alloc] initWithFormat:fmt arguments:ap]);
}

void logV(NSString* tag, NSString* fmt, ...) {
    va_list ap;
    va_start(ap, fmt);
    logInternal(tag, @"Verbose", fmt, ap);
    va_end(ap);
}

void logD(NSString* tag, NSString* fmt, ...) {
    va_list ap;
    va_start(ap, fmt);
    logInternal(tag, @"Debug", fmt, ap);
    va_end(ap);
}

void logI(NSString* tag, NSString* fmt, ...) {
    va_list ap;
    va_start(ap, fmt);
    logInternal(tag, @"Info", fmt, ap);
    va_end(ap);
}

void logW(NSString* tag, NSString* fmt, ...) {
    va_list ap;
    va_start(ap, fmt);
    logInternal(tag, @"Warn", fmt, ap);
    va_end(ap);
}

void logE(NSString* tag, NSString* fmt, ...) {
    va_list ap;
    va_start(ap, fmt);
    logInternal(tag, @"Error", fmt, ap);
    va_end(ap);
}
