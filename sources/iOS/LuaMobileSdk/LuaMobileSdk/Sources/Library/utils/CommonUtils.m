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

#import "CommonUtils.h"
#import "NSString+Sdk.h"


@implementation CommonUtils


+ (void)dispatchToMain:(dispatch_block_t)block {
    dispatch_async(dispatch_get_main_queue(), block);
}

+ (void)dispatchToCurrentQueue:(dispatch_block_t)block {
    dispatch_async(dispatch_get_current_queue(), block);
}

+ (BOOL)isMainThread {
    return strcmp(
            dispatch_queue_get_label(DISPATCH_CURRENT_QUEUE_LABEL),
            dispatch_queue_get_label(dispatch_get_main_queue())) == 0;
}

+ (NSDictionary*)parseJsonObject:(NSString*)str {
    return [CommonUtils parseJsonContent:str];
}

+ (NSArray*)parseJsonArray:(NSString*)str {
    id json = [CommonUtils parseJsonContent:str];
    if ([json isKindOfClass:[NSArray class]]) {
        return json;
    }

    return nil;
}

+ (id)parseJsonContent:(NSString*)str {
    if (!str || [str sdk_isEmpty]) {
        return nil;
    }

    NSData* data = [str dataUsingEncoding:NSUTF8StringEncoding];
    NSError* error = nil;
    return [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
}

@end
