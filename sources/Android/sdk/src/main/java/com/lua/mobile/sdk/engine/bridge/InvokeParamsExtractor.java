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

package com.lua.mobile.sdk.engine.bridge;

import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class InvokeParamsExtractor {

    public abstract Object[] extractParams(LuaBridgeArgs args);


    private interface SingleParamExtractor {
        Object extract(LuaBridgeArgs args, int index);
    }


    private static SingleParamExtractor mObjectParamExtractor = null;
    private static Map<Class<?>, SingleParamExtractor> mSingleParamExtractors = new HashMap<>();


    static {
        initSingleParamExtractors();
    }

    private static void initSingleParamExtractors() {
        // Object
        SingleParamExtractor obj = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toJavaObjectFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - Object";
            }
        };
        mObjectParamExtractor = obj;
        mSingleParamExtractors.put(Object.class, obj);

        // Boolean
        SingleParamExtractor bool = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toBooleanFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - boolean";
            }
        };
        mSingleParamExtractors.put(boolean.class, bool);
        mSingleParamExtractors.put(Boolean.class, bool);

        // Int
        SingleParamExtractor integer = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toIntFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - int";
            }
        };
        mSingleParamExtractors.put(int.class, integer);
        mSingleParamExtractors.put(Integer.class, integer);

        // Long
        SingleParamExtractor long_ = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toLongFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - long";
            }
        };
        mSingleParamExtractors.put(long.class, long_);
        mSingleParamExtractors.put(Long.class, long_);

        // Float
        SingleParamExtractor float_ = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toFloatFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - float";
            }
        };
        mSingleParamExtractors.put(float.class, float_);
        mSingleParamExtractors.put(Float.class, float_);

        // Double
        SingleParamExtractor double_ = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toNumberFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - double";
            }
        };
        mSingleParamExtractors.put(double.class, double_);
        mSingleParamExtractors.put(Double.class, double_);

        // String
        SingleParamExtractor string = new SingleParamExtractor() {
            @Override
            public Object extract(LuaBridgeArgs args, int index) {
                return args.toStringFixed(index);
            }

            @Override
            public String toString() {
                return "Extractor - String";
            }
        };
        mSingleParamExtractors.put(String.class, string);
    }


    public static InvokeParamsExtractor createParamsExtractor(final Method invoker) {
        final Class<?>[] params = invoker.getParameterTypes();
        if (params == null || params.length <= 0) {
            return null;
        }

        final SingleParamExtractor[] extractors = new SingleParamExtractor[params.length];
        for (int i = 0; i < params.length; ++i) {
            Class<?> clazz = params[i];
            SingleParamExtractor extractor = mSingleParamExtractors.get(clazz);
            if (extractor == null) {
                extractor = mObjectParamExtractor;
            }

            extractors[i] = extractor;
        }

        return new InvokeParamsExtractor() {
            @Override
            public Object[] extractParams(LuaBridgeArgs args) {
                Object[] results = new Object[params.length];
                for (int i = 0; i < params.length; ++i) {
                    results[i] = extractors[i].extract(args, i);
                }

                return results;
            }

            @Override
            public String toString() {
                return "ParamsExtractor - " + invoker.getName() + " - " + invoker.toString();
            }
        };
    }
}
