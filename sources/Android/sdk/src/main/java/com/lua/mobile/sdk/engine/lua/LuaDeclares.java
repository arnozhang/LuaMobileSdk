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

public interface LuaDeclares {

    public static final String __len = "__len";
    public static final String __call = "__call";
    public static final String __index = "__index";
    public static final String __newindex = "__newindex";

    public static final String __java_meta_funcs = "__java_meta_funcs";
    public static final String __java_object_type = "__java_object_type";
    public static final String __java_member_funcs = "__java_member_funcs";
}
