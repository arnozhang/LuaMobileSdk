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
#import "RegisterHelpers.h"
#import "LuaBridgeArgs.h"


/**
 * 位检测.
 */
#define SDK_MASK_TEST(value, mask) ((value & mask) == mask)


/**
 * Bridge 方法名称生成.
 *
 * _SDK_METHOD_NAME(Bridge, Method  -> _Bridge_Method
 */
#define _SDK_METHOD_MERGER(Bridge, Method)  _ ## Bridge ## _ ## Method
#define _SDK_METHOD_NAME(Bridge, Method)    _SDK_METHOD_MERGER(Bridge, Method)
#define SDK_METHOD_NAME_GENERATOR(Method)   _SDK_METHOD_NAME(SDK_BRIDGE_IMPL, Method)



/**
 * 展开 Bridge 参数.
 */
#define EXPAND_BRIDGE_ARGS(BridgeClassName, InstanceClassName) \
    BridgeClassName* bridge = (BridgeClassName*) env.bridge; \
    InstanceClassName* instance = (InstanceClassName*) [args toObject:0]


/**
 * 展开 Static Bridge 参数.
 */
#define EXPAND_STATIC_BRIDGE_ARGS(BridgeClassName) \
    BridgeClassName* bridge = (BridgeClassName*) env.bridge;


/**
 * 注册常量.
 */
#define SDK_STATIC_VARIABLE_INT(VariableName) \
    [helper registerInt:@#VariableName Value:VariableName]

#define SDK_STATIC_VARIABLE_BOOL(VariableName) \
    [helper registerBool:@#VariableName Value:VariableName]

#define SDK_STATIC_VARIABLE_STRING(VariableName) \
    [helper registerString:@#VariableName Value:VariableName]


/**
 * 注册方法.
 */
#define SDK_METHOD(MethodName) \
    [helper registerInstanceMethod:self Name:@#MethodName Method:&SDK_METHOD_NAME_GENERATOR(MethodName)]

#define SDK_STATIC_METHOD(MethodName) \
    [helper registerStaticMethod:self Name:@#MethodName Method:&SDK_METHOD_NAME_GENERATOR(MethodName)]

#define SDK_METHOD_BRIDGE(Bridge, MethodName) \
    [helper registerInstanceMethod:Bridge Name:@#MethodName Method:&SDK_METHOD_NAME_GENERATOR(MethodName)]

#define SDK_STATIC_METHOD_BRIDGE(Bridge, MethodName) \
    [helper registerStaticMethod:Bridge Name:@#MethodName Method:&SDK_METHOD_NAME_GENERATOR(MethodName)]


/**
 * 类方法实现列表 Begin.
 */
#define SDK_METHOD_IMPL_BEGIN(InstanceClassName) \
    typedef SDK_BRIDGE_IMPL _BridgeClass; \
    typedef InstanceClassName _InstanceClass; \


/**
 * 类方法实现列表 End.
 */
#define SDK_METHOD_IMPL_END()


/**
 * Static 方法实现列表 Begin.
 */
#define SDK_STATIC_METHOD_IMPL_BEGIN() \
    typedef SDK_BRIDGE_IMPL _BridgeClass;


/**
 * Static 方法实现列表 End.
 */
#define SDK_STATIC_METHOD_IMPL_END()


/**
 * void ()
 */
#define SDK_RET_0_PARAM_0(Method) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        [bridge Method:instance]; \
        return nil; \
    }

/**
 * R ()
 */
#define SDK_RET_1_PARAM_0(Method) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        return [bridge Method:instance]; \
    }


/**
 * void (T)
 */
#define SDK_RET_0_PARAM_1(Method, Arg, toArg) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        [bridge Method:instance Arg:[args toArg:1]]; \
        return nil; \
    }

/**
 * R (T)
 */
#define SDK_RET_1_PARAM_1(Method, Arg, toArg) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        return [bridge Method:instance Arg:[args toArg:1]]; \
    }


/**
 * void (T1, T2)
 */
#define SDK_RET_0_PARAM_2(Method, Arg1, toArg1, Arg2, toArg2) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        [bridge Method:instance Arg1:[args toArg1:1] Arg2:[args toArg2:2]]; \
        return nil; \
    }


/**
 * R (T1, T2)
 */
#define SDK_RET_1_PARAM_2(Method, Arg1, toArg1, Arg2, toArg2) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        return [bridge Method:instance  Arg1:[args toArg1:1] Arg2:[args toArg2:2]]; \
    }


/**
 * void (T1, T2, T3)
 */
#define SDK_RET_0_PARAM_3(Method, Arg1, toArg1, Arg2, toArg2, Arg3, toArg3) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        [bridge Method:instance Arg1:[args toArg1:1] Arg2:[args toArg2:2] Arg3:[args toArg3:3]]; \
        return nil; \
    }


/**
 * R (T1, T2, T3)
 */
#define SDK_RET_1_PARAM_3(Method, Arg1, toArg1, Arg2, toArg2, Arg3, toArg3) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        return [bridge Method:instance  Arg1:[args toArg1:1] \
                      Arg2:[args toArg2:2] Arg3: [args toAeg3:3]]; \
    }

/**
 * void (T1, T2, T3, T4)
 */
#define SDK_RET_0_PARAM_4(Method, Arg1, toArg1, Arg2, toArg2, Arg3, toArg3, Arg4, toArg4) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        [bridge Method:instance Arg1:[args toArg1:1] Arg2:[args toArg2:2] Arg3:[args toArg3:3] Arg4:[args toArg4:4]]; \
        return nil; \
    }


/**
 * R (T1, T2, T3, T4)
 */
#define SDK_RET_1_PARAM_4(Method, Arg1, toArg1, Arg2, toArg2, Arg3, toArg3, Arg4, toArg4) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_BRIDGE_ARGS(_BridgeClass, _InstanceClass); \
        return [bridge Method:instance  Arg1:[args toArg1:1] \
                      Arg2:[args toArg2:2] Arg3: [args toArg3:3] Arg4:[args toArg4]]; \
    }


/**
 * 单方法.
 *
 * void ()   -> show() / hide()
 */
#define SDK_METHOD_SINGLE(Method) SDK_RET_0_PARAM_0(Method)


/**
 * Getter 方法.
 *
 * R ()     -> getId()
 */
#define SDK_METHOD_GETTER(Method) SDK_RET_1_PARAM_0(get##Method)

#define SDK_METHOD_GETTER_IS(Method) SDK_RET_1_PARAM_0(is##Method)


/**
 * Setter 方法.
 *
 * void (T)     -> setId(id)
 */
#define SDK_METHOD_SETTER(Method, toArg) SDK_RET_0_PARAM_1(set##Method, Method, toArg)


/**
 * 属性成对注册.
 *
 * setXXX   -> setId
 * getXXX   -> getId
 */
#define SDK_PROPERTY(Property) \
    SDK_METHOD(get##Property); \
    SDK_METHOD(set##Property)


/**
 * 属性成对注册.
 *
 * setXXX   -> setEnabled
 * isXXX    -> isEnabled
 */
#define SDK_PROPERTY_IS(Property) \
    SDK_METHOD(is##Property); \
    SDK_METHOD(set##Property)


/**
 * 属性成对实现.
 *
 * getXXX
 * setXXX
 */
#define SDK_PROPERTY_IMPL(Property, toArg) \
    SDK_METHOD_GETTER(Property) \
    SDK_METHOD_SETTER(Property, toArg)


/**
 * 属性成对实现.
 *
 * isXXX
 * setXXX
 */
#define SDK_PROPERTY_IMPL_IS(Property, toArg) \
    SDK_METHOD_GETTER_IS(Property) \
    SDK_METHOD_SETTER(Property, toArg)



/**
 * Static 方法相关宏定义.
 */
#pragma mark -- Static Method Macro


/**
 * void ()
 */
#define SDK_STATIC_RET_0_PARAM_0(Method) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        [bridge Method]; \
        return nil; \
    }

/**
 * R ()
 */
#define SDK_STATIC_RET_1_PARAM_0(Method) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        return [bridge Method]; \
    }


/**
 * void (T)
 */
#define SDK_STATIC_RET_0_PARAM_1(Method, toArg) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        [bridge Method:[args toArg:1]]; \
        return nil; \
    }

/**
 * R (T)
 */
#define SDK_STATIC_RET_1_PARAM_1(Method, toArg) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        return [bridge Method:[args toArg:1]]; \
    }


/**
 * void (T1, T2)
 */
#define SDK_STATIC_RET_0_PARAM_2(Method, toArg1, Arg2, toArg2) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        [bridge Method:[args toArg1:1] Arg2:[args toArg2:2]]; \
        return nil; \
    }


/**
 * R (T1, T2)
 */
#define SDK_STATIC_RET_1_PARAM_2(Method, toArg1, Arg2, toArg2) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        return [bridge Method:[args toArg1:1] Arg2:[args toArg2:2]]; \
    }


/**
 * void (T1, T2, T3)
 */
#define SDK_STATIC_RET_0_PARAM_3(Method, toArg1, Arg2, toArg2, Arg3, toArg3) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        [bridge Method:[args toArg1:1] Arg2:[args toArg2:2] Arg3:[args toArg3:3]]; \
        return nil; \
    }


/**
 * R (T1, T2, T3)
 */
#define SDK_STATIC_RET_1_PARAM_3(Method, Arg1, toArg1, Arg2, toArg2, Arg3, toArg3) \
    NSObject* SDK_METHOD_NAME_GENERATOR(Method)(LuaMethodInvokeEnv* env, LuaBridgeArgs* args) { \
        EXPAND_STATIC_BRIDGE_ARGS(_BridgeClass); \
        return [bridge Method:[args toArg1:1] Arg2:[args toArg2:2] Arg3: [args toAeg3:3]]; \
    }
