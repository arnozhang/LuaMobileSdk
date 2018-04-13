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

extern const char* const __gc;
extern const char* const __len;
extern const char* const __call;
extern const char* const __index;
extern const char* const __newindex;
extern const char* const __oc_object_type;
extern const char* const __oc_member_funcs;


typedef enum {
    RuntimeError = 1,       // a runtime error.
    StackOverflowError = 2, // stack overflow error.
    MemoryError = 4,        // memory allocation error. For such errors, Lua does not call the error handler function.
    ErrorHandlerError = 5   // error while running the error handler function.
} LuaError;
