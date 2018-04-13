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

package com.lua.mobile.sdk.library.module;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.lua.mobile.sdk.engine.bridge.LuaMethod;
import com.lua.mobile.sdk.engine.bridge.LuaModuleBridge;
import com.lua.mobile.sdk.engine.lua.LuaObject;

public class TasksModule extends LuaModuleBridge {

    public static final int invalid_id = -1;


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SparseArray<Runnable> mTimerRunnable = new SparseArray<>();
    private int mCurrentIndex = 0;


    @Override
    public String getBridgeName() {
        return "Tasks";
    }

    @LuaMethod
    public void async(final Object object) {
        if (object instanceof LuaObject) {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    LuaObject callback = (LuaObject) object;
                    callback.call();
                    return null;
                }
            };

            task.execute();
        }
    }

    @LuaMethod
    public void post(Object object) {
        postTo(new Handler(Looper.myLooper()), object);
    }

    @LuaMethod
    public void postToMain(Object object) {
        postTo(mHandler, object);
    }

    @LuaMethod
    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @LuaMethod
    public int setTimer(
            final Object object, final int millSeconds, final boolean single, int delay) {

        if (object instanceof LuaObject) {
            final int timerId = nextTimerId();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (single) {
                        removeTimer(timerId);
                    } else {
                        mHandler.postDelayed(this, millSeconds);
                    }

                    LuaObject callback = (LuaObject) object;
                    callback.call(timerId);
                }
            };

            mTimerRunnable.put(timerId, runnable);
            mHandler.postDelayed(runnable, millSeconds);
            return timerId;
        }

        return invalid_id;
    }

    @LuaMethod
    public void cancelTimer(int timerId) {
        Runnable runnable = mTimerRunnable.get(timerId);
        removeTimer(timerId);
        if (runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private static void postTo(Handler handler, final Object object) {
        if (object instanceof LuaObject) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    LuaObject callback = (LuaObject) object;
                    callback.call();
                }
            });
        }
    }

    private synchronized int nextTimerId() {
        return ++mCurrentIndex;
    }

    private synchronized void removeTimer(int timerId) {
        mTimerRunnable.remove(timerId);
    }
}
