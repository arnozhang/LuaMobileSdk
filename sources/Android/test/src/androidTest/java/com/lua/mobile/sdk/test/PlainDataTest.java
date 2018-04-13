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
import com.lua.mobile.sdk.test.base.BaseWithLibraryTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class PlainDataTest extends BaseWithLibraryTest {

    @Test
    public void list() {
        List<Object> list = new ArrayList<>();
        list.add(10);
        list.add(90.98);
        list.add("hello list");

        mEngine.registerVariable("list", list);
        mEngine.doScriptString("Log.e(TAG, list[0])");
        mEngine.doScriptString("Log.e(TAG, list[1])");
        mEngine.doScriptString("Log.e(TAG, list[2])");
        mEngine.doScriptString("Log.e(TAG, 'list.size = ' .. list.size .. ', #list = ' .. #list)");

        mEngine.doScriptString("first = list[0]");
        assertEquals(10, mLuaState.getLuaObject("first").toInt());

//        mEngine.doScriptString("list[0] = 'arnozhang'");
//        mEngine.doScriptString("Log.e(TAG, list[0])");
//
//        mEngine.doScriptString("first = list[0]");
//        assertEquals("arnozhang", mLuaState.getLuaObject("first").toString());
    }

    @Test
    public void newList() {
//        mEngine.doScriptString("list = List(100)");
//        mEngine.doScriptString("list[0] = 90");
//        mEngine.doScriptString("list[1] = 'arnozhang'");
//        mEngine.doScriptString("Log.e(TAG, 'list.size = ' .. list.size .. ', #list = ' .. #list)");
//
//        mEngine.doScriptString("first = list[0]");
//        assertEquals(90, mLuaState.getLuaObject("first").toInt());
//
//        mEngine.doScriptString("second = list[1]");
//        assertEquals("arnozhang", mLuaState.getLuaObject("second").toString());
    }

    @Test
    public void map() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "arnozhang");
        map.put("address", "HangZhou@China");

        mEngine.registerVariable("map", map);
        mEngine.doScriptString("map.email = 'zyfgood12@163.com'");
        mEngine.doScriptString("Log.e(TAG, 'map.name = ' .. map.name)");
        mEngine.doScriptString("Log.e(TAG, 'map.address = ' .. map.address)");
        mEngine.doScriptString("Log.e(TAG, 'map.email = ' .. map.email)");
    }
}
