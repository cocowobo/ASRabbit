package com.ht.baselib.helper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>抽象第三方对象数据库基类</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public abstract class ObjectDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Map<String, Dao> mDaoMap = new HashMap<>();
    /**
     * 默认版本
     */
    private static final int DEFAULT_VERSION = 1;
    /**
     * 默认的数据库名
     */
    private static final String DEFAULT_DB_NAME = "object.db";

    /**
     * 构造方法
     *
     * @param ctx 上下文
     */
    protected ObjectDatabaseHelper(Context ctx) {
        super(ctx, DEFAULT_DB_NAME, null, DEFAULT_VERSION);
    }

    /**
     * 构造方法
     *
     * @param ctx       上下文
     * @param dbName    数据库名
     * @param dbVersion 版本
     */
    protected ObjectDatabaseHelper(Context ctx, String dbName, int dbVersion) {
        super(ctx, dbName, null, dbVersion);
    }

    /**
     * 创建表
     */
    public abstract void createTable();

    /**
     * 销毁表
     */
    public abstract void dropTable();

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropTable();
        onCreate(database, connectionSource);
    }

    /**
     * 获取dao对象
     *
     * @param clazz 被映射的数据库模型
     * @return dao
     * @throws SQLException sql异常
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (mDaoMap.containsKey(className)) {
            dao = mDaoMap.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            mDaoMap.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        //释放资源
        super.close();
        for (String key : mDaoMap.keySet()) {
            Dao dao = mDaoMap.get(key);
            dao = null;
        }
    }

}