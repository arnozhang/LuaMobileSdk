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

#include "BridgeDeclares.h"
#include "Utils.h"


jclass BridgedJavaClassInfo::clazz::Class = 0;
jclass BridgedJavaClassInfo::clazz::Throwable = 0;
jclass BridgedJavaClassInfo::clazz::BridgedFunction = 0;

jmethodID BridgedJavaClassInfo::method::Throwable_toString = 0;
jmethodID BridgedJavaClassInfo::method::Throwable_getMessage = 0;
jmethodID BridgedJavaClassInfo::method::Throwable_printStackTrace = 0;
jmethodID BridgedJavaClassInfo::method::BridgedFunction_invoke = 0;


void BridgedJavaClassInfo::checkAndInitClass(
        JNIEnv* env, jclass& clazz, const char* const clazzName) {

    if (!clazz) {
        clazz = env->FindClass(clazzName);
        if (clazz) {
            clazz = (jclass) env->NewGlobalRef(clazz);
        }
    }
}

void BridgedJavaClassInfo::initBridgedJavaClassInfo(JNIEnv* env) {
    checkAndInitClass(env, clazz::Class, "java/lang/Class");
    checkAndInitClass(env, clazz::Throwable, "java/lang/Throwable");
    checkAndInitClass(env, clazz::BridgedFunction,
                      "com/lua/mobile/sdk/engine/bridge/LuaBridgedFunction");

    if (!method::Throwable_getMessage) {
        if (clazz::Throwable) {
            method::Throwable_getMessage = env->GetMethodID(
                    clazz::Throwable, "getMessage", "()Ljava/lang/String;");
        }
    }

    if (!method::Throwable_printStackTrace) {
        if (clazz::Throwable) {
            method::Throwable_printStackTrace = env->GetMethodID(
                    clazz::Throwable, "printStackTrace", "()V");
        }
    }

    if (!method::Throwable_toString) {
        if (clazz::Throwable) {
            method::Throwable_toString = env->GetMethodID(
                    clazz::Throwable, "toString", "()Ljava/lang/String;");
        }
    }

    if (!method::BridgedFunction_invoke) {
        if (clazz::BridgedFunction) {
            method::BridgedFunction_invoke = env->GetMethodID(
                    clazz::BridgedFunction, "invoke", "()I");
        }
    }
}
