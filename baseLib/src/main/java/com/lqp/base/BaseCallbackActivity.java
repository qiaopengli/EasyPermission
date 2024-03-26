package com.lqp.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lqp.base.iface.IResult;
import com.lqp.base.util.WatchDog;

import java.util.HashMap;
import java.util.UUID;

public class BaseCallbackActivity extends Activity {

    private static final HashMap<String, Object> sTmpObjs = new HashMap<>();
    private boolean isFirstResume = true;

    public static <T> String putObjToTmp(T obj, IResult<T> onRemoved) {
        String key = UUID.randomUUID().toString();
        sTmpObjs.put(key, obj);
        //时间太长直接清除，防止内存泄漏
        WatchDog.registerCallback(() -> {
            T cbTmp = (T) sTmpObjs.get(key);
            if (cbTmp != null) {
                sTmpObjs.remove(key);
                if (onRemoved != null) {
                    onRemoved.onResult(cbTmp);
                }
            }
        }, 10000);
        return key;
    }

    public static <T> T getObjFromTmpAndRemove(String key) {
        T obj = (T) sTmpObjs.get(key);
        sTmpObjs.remove(key);
        return obj;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstResume = false;
    }

    public boolean isFirstResume() {
        return isFirstResume;
    }

    @Override
    public void onBackPressed() {

    }
}
