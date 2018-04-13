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

package com.lua.mobile.sdk.test;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaBridgedFunction;
import com.lua.mobile.sdk.test.base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class FunctionTest extends BaseTest {

    @Test
    public void hello() {
        mEngine.registerFunction("hello", new LuaBridgedFunction() {
            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Throwable {
                Log.e(TAG, args.toString(0));
                return 0;
            }
        });

        mEngine.doScriptString("hello('hello arnozhang!')");
    }

    @Test
    public void add() {
        final int a = 100;
        final int b = 90;

        mEngine.registerVariable("a", a);
        mEngine.registerVariable("b", b);
        mEngine.registerFunction("add", new LuaBridgedFunction() {
            @Override
            protected int invokeInternal(LuaBridgeArgs args) throws Throwable {
                mLuaState.pushInteger(args.toIntFixed(0) + args.toIntFixed(1));
                return 1;
            }
        });

        mEngine.doScriptString("result = add(a, b)");
        mEngine.doScriptString("Log.e(TAG, 'a + b = '.. result)");
        assertEquals(mLuaState.getLuaObject("result").toInt(), a + b);
    }

    @Test
    public void callLuaFunction() {
        final int a = 100;
        final int b = 90;

        mEngine.doScriptString("function sub(a, b) return a - b end");
        LuaBridgeArgs ret = mLuaState.getLuaObject("sub").callWithMultiReturn(a, b);
        if (ret != null) {
            assertEquals(ret.toIntFixed(0), a - b);
        } else {
            assertTrue(false);
        }
    }
}
