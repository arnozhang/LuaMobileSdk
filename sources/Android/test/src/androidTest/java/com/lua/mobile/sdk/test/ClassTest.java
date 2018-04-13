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

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import com.lua.mobile.sdk.engine.LuaScriptEngine;
import com.lua.mobile.sdk.engine.args.LuaBridgeArgs;
import com.lua.mobile.sdk.engine.bridge.LuaClassBridge;
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.test.base.BaseWithLibraryTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


class View {

    private String mId;

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }
}


class ViewBridge extends LuaClassBridge<View> {

    public static final int width_fill = 10;


    @Override
    public Class<View> getDataClass() {
        return View.class;
    }

    @Override
    public String getBridgeName() {
        return "View";
    }

    @NonNull
    @Override
    public View createInstance(@NonNull LuaScriptEngine scriptEngine, @NonNull LuaBridgeArgs args) {
        return new View();
    }

    @LuaMethod
    public void setId(View view, String id) {
        view.setId(id);
    }

    @LuaMethod
    public String getId(View view) {
        return view.getId();
    }
}


@RunWith(AndroidJUnit4.class)
public class ClassTest extends BaseWithLibraryTest {

    @Override
    protected void initialize() {
        super.initialize();
        mEngine.registerClass(new ViewBridge());
    }

    @Test
    public void view() {
        final String id = "arnozhang - view";
        mEngine.doScriptString("view = View()");
        mEngine.doScriptString(String.format("view.setId('%s')", id));
        mEngine.doScriptString("id = view.getId()");
        mEngine.doScriptString("Log.i(TAG, 'view.getId = ' .. id)");

        assertEquals(id, mLuaState.getLuaObject("id").toString());
    }

    @Test
    public void viewFromAndroid() {
        View view = new View();
        view.setId("view from Android");
        mEngine.registerVariable("view1", view);
        mEngine.doScriptString("Log.e(TAG, 'view1.getId = ' .. view1.getId()");
        mEngine.doScriptString("id = view1.getId()");

        assertEquals(view.getId(), mLuaState.getLuaObject("id").toString());
    }

    @Test
    public void staticFields() {
        mEngine.doScriptString("width = View.width_fill");
        assertEquals(ViewBridge.width_fill, mLuaState.getLuaObject("width").toInt());
    }
}
