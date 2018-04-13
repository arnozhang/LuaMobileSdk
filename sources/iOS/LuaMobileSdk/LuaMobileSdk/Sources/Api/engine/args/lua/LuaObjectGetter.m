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

#import "LuaObjectGetter.h"
#import "LuaObject.h"
#import "LuaType.h"
#import "BoolObject.h"


@implementation LuaObjectGetter


- (BOOL)toBool:(int)index {
    return [self toBoolDirectly:index];
}

- (double)toNumber:(int)index {
    return [self toNumberDirectly:index];
}

- (NSString*)toString:(int)index {
    return [self toStringDirectly:index];
}

- (NSObject*)toObject:(int)index {
    return [self toObjectDirectly:index];
}

- (BOOL)toBool:(int)index Optional:(BOOL)opt {
    if ([self isBool:index]) {
        return [self toBool:index];
    }

    return opt;
}

- (double)toNumber:(int)index Optional:(double)opt {
    if ([self isNumber:index]) {
        return [self toNumber:index];
    }

    return opt;
}

- (NSString*)toString:(int)index Optional:(NSString*)opt {
    if ([self isString:index]) {
        return [self toString:index];
    }

    return opt;
}

- (NSObject*)toObject:(int)index Optional:(NSObject*)opt {
    if ([self isObject:index]) {
        return [self toObject:index];
    }

    return opt;
}

- (BOOL)toBoolFixed:(int)index {
    return [self toBoolFixed:index Optional:NO];
}

- (double)toNumberFixed:(int)index {
    return [self toNumberFixed:index Optional:0];
}

- (NSString*)toStringFixed:(int)index {
    return [self toStringFixed:index Optional:nil];
}

- (NSObject*)toObjectFixed:(int)index {
    return [self toObjectFixed:index Optional:nil];
}

- (BOOL)toBoolFixed:(int)index Optional:(BOOL)opt {
    const int type = [self getType:index];

    if (type == LuaTypeBool) {
        return [self toBool:index];
    } else if (type == LuaTypeNumber) {
        return [self toNumber:index] != 0;
    }

    return opt;
}

- (double)toNumberFixed:(int)index Optional:(double)opt {
    const int type = [self getType:index];

    if (type == LuaTypeNumber) {
        return [self toNumber:index];
    } else if (type == LuaTypeString) {
        NSString* str = [self toString:index];
        @try {
            return str.floatValue;
        } @catch (NSException* e) {
            return opt;
        }
    } else if (type == LuaTypeBool) {
        return [self toBool:index];
    }

    return opt;
}

- (NSString*)toStringFixed:(int)index Optional:(NSString*)opt {
    const int type = [self getType:index];

    if ([LuaType isNil:type]) {
        return opt;
    } else if (type == LuaTypeString) {
        return [self toString:index];
    } else if (type == LuaTypeNumber) {
        return [NSString stringWithFormat:@"%f", [self toNumber:index]];
    } else if (type == LuaTypeBool) {
        return [self toBool:index] ? @"YES" : @"NO";
    }

    return opt;
}

- (NSObject*)toObjectFixed:(int)index Optional:(NSObject*)opt {
    const int type = [self getType:index];

    if ([LuaType isNil:type]) {
        return nil;
    } else if (type == LuaTypeNumber) {
        return @([self toNumber:index]);
    } else if (type == LuaTypeString) {
        return [self toString:index];
    } else if (type == LuaTypeObject) {
        return [self toObject:index];
    } else if (type == LuaTypeBool) {
        return [BoolObject valueOf:[self toBool:index]];
    } else if (type == LuaTypeFunction || type == LuaTypeTable || type == LuaTypeUserData) {
        return [self getLuaObjectByIndex:index];
    }

    return opt;
}

- (int)toIntFixed:(int)index {
    return [self toIntFixed:index Optional:0];
}

- (int)toIntFixed:(int)index Optional:(int)opt {
    return (int) [self toNumberFixed:index Optional:opt];
}

- (long)toLongFixed:(int)index {
    return [self toLongFixed:index Optional:0];
}

- (long)toLongFixed:(int)index Optional:(long)opt {
    return (long) [self toNumberFixed:index Optional:opt];
}

- (float)toFloatFixed:(int)index {
    return [self toFloatFixed:index Optional:0.0f];
}

- (float)toFloatFixed:(int)index Optional:(float)opt {
    return (float) [self toNumberFixed:index Optional:opt];
}

- (LuaObject*)objectForKeyedSubscript:(NSString*)globalVarName {
    return [self getLuaObject:globalVarName];
}

- (LuaObject*)getLuaObjectByIndex:(int)index {
    return [[LuaObject alloc] init:[self getScriptEngine] Index:index];
}

- (LuaObject*)getLuaObject:(NSString*)globalVarName {
    return [[LuaObject alloc] init:[self getScriptEngine] GlobalVarName:globalVarName];
}

- (LuaObject*)getLuaObject:(LuaObject*)object FieldIndex:(int)index {
    if ([object getScriptEngine] != [self getScriptEngine]) {
        [NSException raise:@"LuaState - getLuaObject"
                    format:@"Cannot access field in not-same Lua state!"];
    }

    return [[LuaObject alloc] init:object FieldIndex:index];
}

- (LuaObject*)getLuaObject:(LuaObject*)object FieldName:(NSString*)fieldName {
    if ([object getScriptEngine] != [self getScriptEngine]) {
        [NSException raise:@"LuaState - getLuaObject"
                    format:@"Cannot access field in not-same Lua state!"];
    }

    return [[LuaObject alloc] init:object FieldName:fieldName];
}

- (LuaObject*)getLuaObject:(LuaObject*)object LuaObject:(LuaObject*)field {
    if ([object getScriptEngine] != [self getScriptEngine]) {
        [NSException raise:@"LuaState - getLuaObject"
                    format:@"Cannot access field in not-same Lua state!"];
    }

    return [[LuaObject alloc] init:object Field:field];
}

@end