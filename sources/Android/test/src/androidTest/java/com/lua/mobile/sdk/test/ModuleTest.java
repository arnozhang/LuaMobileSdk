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
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;
import com.lua.mobile.sdk.test.base.BaseWithLibraryTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


class TestModule extends LuaModuleBridge {

    public static final int staticValue = 180;

    @Override
    public String getBridgeName() {
        return "Test";
    }

    @LuaMethod
    public int intValue() {
        return 80;
    }

    @LuaMethod
    public String stringValue() {
        return "hello";
    }
}


@RunWith(AndroidJUnit4.class)
public class ModuleTest extends BaseWithLibraryTest {

    private TestModule mTestModule = new TestModule();


    @Override
    protected void initialize() {
        super.initialize();
        mEngine.registerModule(mTestModule);
    }

    @Test
    public void toast() {
        mEngine.doScriptString("Toast.show('hello form lua!')");
    }

    @Test
    public void log() {
        mEngine.doScriptString("Log.v(TAG, 'v - log test')");
        mEngine.doScriptString("Log.d(TAG, 'd - log test')");
        mEngine.doScriptString("Log.i(TAG, 'i - log test')");
        mEngine.doScriptString("Log.w(TAG, 'w - log test')");
        mEngine.doScriptString("Log.e(TAG, 'e - log test')");
    }

    @Test
    public void screen() {
        mEngine.doScriptString("Log.e(TAG, 'screenWidth = ' .. Common.getScreenWidth()"
                + " .. ' screenHeight = ' .. Common.getScreenHeight())");
    }

    @Test
    public void test() {
        mEngine.doScriptString("intValue = Test.intValue()");
        mEngine.doScriptString("stringValue = Test.stringValue()");
        mEngine.doScriptString("Test_staticValue = Test.staticValue");

        assertEquals(mLuaState.getLuaObject("Test_staticValue").toInt(), TestModule.staticValue);
        assertEquals(mLuaState.getLuaObject("intValue").toInt(), mTestModule.intValue());
        assertEquals(mLuaState.getLuaObject("stringValue").toString(), mTestModule.stringValue());
    }
}
