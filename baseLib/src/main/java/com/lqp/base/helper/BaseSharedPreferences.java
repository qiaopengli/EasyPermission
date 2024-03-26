package com.lqp.base.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Set;

public class BaseSharedPreferences {
    @NonNull
    private final SharedPreferences sp;
    @NonNull
    private final SharedPreferences.Editor editor;

    protected BaseSharedPreferences(@NonNull Context context) {
        sp = context.getSharedPreferences(getClass().getCanonicalName(), Context.MODE_PRIVATE);
        editor = sp.edit();
    }


    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sp.getStringSet(key, defValue);
    }

    /**
     * Save a Json String value in the preferences editor.
     *
     * @param key   The name of the preference to modify.
     * @param value A parameter of the new Json String for the preference.
     */
    public void putObjectJsonStr(String key, Object value) {
        try {
            Gson gson = new Gson();
            editor.putString(key, gson.toJson(value));
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定类型Object，需确保key保存的为当前Object的json字符串
     *
     * @param key         the name of the preference to retrieve.
     * @param returnClass 返回数据类型class
     * @param <T>         数据类型
     * @return nullable
     */
    public <T> T getObjectForJsonStr(String key, Class<T> returnClass) {
        String json = sp.getString(key, null);
        if (json != null) {
            try {
                Gson gson = new Gson();
                return gson.fromJson(json, returnClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 删除所有信息
     */
    public void deleteAll() {
        editor.clear();
        editor.apply();
    }

    /**
     * 删除一条信息
     */
    public void delete(String key) {
        editor.remove(key);
        editor.apply();
    }

    /**
     * 删除多条信息
     */
    public void deleteBatch(String[] keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }

}
