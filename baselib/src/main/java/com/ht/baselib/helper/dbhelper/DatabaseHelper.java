package com.ht.baselib.helper.dbhelper;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * <p>对象数据库基类，提供给上层的数据库帮助类继承</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class DatabaseHelper extends ObjectDatabaseHelper {

    /**
     * DatabaseHelper实例
     */
    private static DatabaseHelper instance;
    /**
     * 被映射的数据库模型 如City.Class
     */
    private static Class mClass;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param name    数据库名
     * @param version 版本
     * @param clazz   被映射的数据库模型
     */
    public DatabaseHelper(Context context, String name, int version, Class clazz) {
        super(context, name, version);
        mClass = clazz;
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param clazz   被映射的数据库模型
     */
    public DatabaseHelper(Context context, Class clazz) {
        super(context);
        mClass = clazz;
    }

    /**
     * 单例获取该Helper
     *
     * @param context 上下文
     * @param name    名字
     * @param version 版本
     * @param clazz   对应的对象
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context, String name, int version, Class clazz) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context, name, version, clazz);
                }
            }
        }
        return instance;
    }

    /**
     * 单例获取该Helper
     * @param context 上下文
     * @param clazz 对应的model
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context, Class clazz) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context, clazz);
                }
            }
        }
        return instance;
    }

    /**
     * 创建表
     */
    public void createTable() {
        try {
            TableUtils.createTable(connectionSource, mClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     */
    public void dropTable() {
        try {
            TableUtils.dropTable(connectionSource, mClass, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}