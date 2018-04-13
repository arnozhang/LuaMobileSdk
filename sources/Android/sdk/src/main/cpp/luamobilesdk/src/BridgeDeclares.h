/**
 * Android LuaMobileSdk for Android framework project.
 *
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
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

#ifndef __BRIDGE_DECLARES_H__
#define __BRIDGE_DECLARES_H__

#include <jni.h>
#include <lua.hpp>


#ifdef __cplusplus
#define EXTERN_C extern "C"
#else
#define EXTERN_C
#endif // EXTERN_C


#define EXTERN_C_EXPORT EXTERN_C JNIEXPORT


namespace BridgedType {

    static const int JavaObject = 1001;
    static const int JavaFunction = 1002;
}


namespace BridgedJavaClassInfo {

    namespace clazz {
        extern jclass Class;
        extern jclass Throwable;
        extern jclass BridgedFunction;
    }


    namespace method {
        extern jmethodID Throwable_toString;
        extern jmethodID Throwable_getMessage;
        extern jmethodID Throwable_printStackTrace;
        extern jmethodID BridgedFunction_invoke;
    }


    void initBridgedJavaClassInfo(JNIEnv* env);

    void checkAndInitClass(JNIEnv* env, jclass& clazz, const char* const clazzName);
}


#endif // __BRIDGE_DECLARES_H__