package com.lqp.base.util;

import android.os.Handler;
import android.os.Looper;

public class WatchDog {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public static void registerCallback(Runnable runnable, long delayMillis) {
        sHandler.postDelayed(runnable, delayMillis);
    }
}
