package com.ht.baselib.helper.sphelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <p>SharedPreferences工具类 1、继承该helper类实现preference的使用；2、提供preference的存取方法</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class PreferenceOpenHelper {
    private Context context;
    private String mName;
    private int mMode;

    /**
     * 构造方法
     *
     * @param ctx         上下文
     * @param paramString Preference 文件名
     */
    public PreferenceOpenHelper(Context ctx, String paramString) {
        this.context = ctx;
        this.mName = paramString;
        this.mMode = Context.MODE_PRIVATE;
    }

    /**
     * 获取SharedPreferences实例
     *
     * @return SharedPreferences实例
     */
    private SharedPreferences getSharedPreferences() {
        SharedPreferences localSharedPreferences = this.context
                .getSharedPreferences(this.mName, this.mMode);
        return localSharedPreferences;
    }

    /**
     * 根据key值获取字符串
     *
     * @param paramString1 key值
     * @param paramString2 默认值
     * @return key对应的值
     */
    public String getString(String paramString1, String paramString2) {
        return getSharedPreferences().getString(paramString1, paramString2);
    }

    /**
     * 根据key值获取整数
     *
     * @param paramString key值
     * @param paramInt    默认值
     * @return key对应的值
     */
    public int getInt(String paramString, int paramInt) {
        return getSharedPreferences().getInt(paramString, paramInt);
    }

    /**
     * 根据key值获取长整型
     *
     * @param paramString key值
     * @param paramLong   默认值
     * @return key对应的值
     */
    public long getLong(String paramString, long paramLong) {
        return getSharedPreferences().getLong(paramString, paramLong);
    }

    /**
     * 根据key值获取布尔型
     *
     * @param paramString key值
     * @param paramBoolean 默认值
     * @return key对应的值
     */
    public boolean getBoolean(String paramString, boolean paramBoolean) {
        return getSharedPreferences().getBoolean(paramString, paramBoolean);
    }

    /**
     * 根据key值获取浮点值
     *
     * @param paramString key值
     * @param paramFloat  默认值
     * @return key对应的值
     */
    public float getFloat(String paramString, float paramFloat) {
        return getSharedPreferences().getFloat(paramString, paramFloat);
    }

    /**
     * 根据key值存放字符串
     *
     * @param paramString1 key值
     * @param paramString2 字符串值
     * @return 是否存放成功
     */
    public boolean putString(String paramString1, String paramString2) {
        return getSharedPreferences().edit()
                .putString(paramString1, paramString2).commit();
    }

    /**
     * 根据key值存放整形数据
     *
     * @param paramString key值
     * @param paramInt    需保存的值
     * @return 是否存放成功
     */
    public boolean putInt(String paramString, int paramInt) {
        return getSharedPreferences().edit().putInt(paramString, paramInt)
                .commit();
    }

    /**
     * 根据key值存放长整形数据
     *
     * @param paramString key值
     * @param paramLong   需保存的值
     * @return 是否存放成功
     */
    public boolean putLong(String paramString, long paramLong) {
        return getSharedPreferences().edit().putLong(paramString, paramLong)
                .commit();
    }

    /**
     * 根据key值存放布尔形数据
     *
     * @param paramString  key值
     * @param paramBoolean 需保存的值
     * @return 是否存放成功
     */
    public boolean putBoolean(String paramString, boolean paramBoolean) {
        return getSharedPreferences().edit()
                .putBoolean(paramString, paramBoolean).commit();
    }

    /**
     * 根据key值存放浮点形数据
     *
     * @param paramString key值
     * @param paramFloat  需保存的值
     * @return 是否存放成功
     */
    public boolean putFloat(String paramString, float paramFloat) {
        return getSharedPreferences().edit().putFloat(paramString, paramFloat)
                .commit();
    }

    /**
     * @return SharedPreferences.Editor对象
     */
    public SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 判断SharedPreferences是否包含特定key的数据
     *
     * @param paramString key值
     * @return true 存在,false 不存在
     */
    public boolean contains(String paramString) {
        return getSharedPreferences().contains(paramString);
    }


    /**
     * 移除Preference中的key值
     *
     * @param paramString key值
     * @return 是否移除成功
     */
    public boolean removeKey(String paramString) {
        return getEditor().remove(paramString).commit();
    }
}
