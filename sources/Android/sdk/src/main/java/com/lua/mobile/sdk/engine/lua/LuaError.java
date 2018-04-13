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

package com.lua.mobile.sdk.engine.lua;

public interface LuaError {

    /**
     * a runtime error.
     */
    final public static int RuntimeError = 1;

    /**
     * stack overflow error.
     */
    final public static int StackOverflowError = 2;

    /**
     * memory allocation error. For such errors, Lua does not call
     * the error handler function.
     */
    final public static int MemoryError = 4;

    /**
     * error while running the error handler function.
     */
    final public static int ErrorHandlerError = 5;
}
