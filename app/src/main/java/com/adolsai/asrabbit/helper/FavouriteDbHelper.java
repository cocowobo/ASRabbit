package com.adolsai.asrabbit.helper;

import android.content.Context;

import com.adolsai.asrabbit.model.Topic;
import com.ht.baselib.helper.dbhelper.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>HistoryDbHelper类 1、提供收藏数据库查询功能</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-21 16:31)<br/>
 */
public class FavouriteDbHelper extends DatabaseHelper {

    /**
     * 数据库名
     */
    private static final String DB_NAME = "favourite_topic.db";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    /**
     * HistoryDbHelper
     */
    private static FavouriteDbHelper instance;
    /**
     * 数据库操作
     */
    private Dao<Topic, Integer> dbOpe;

    /**
     * 被映射的对象实体
     */
    private static final Class CURR_CLASS = Topic.class;

    /**
     * 获取FunctionConfigDbHelper实体
     *
     * @param context 上下文
     * @return DemoDbHelper实体
     */
    public static FavouriteDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FavouriteDbHelper(context);
        }
        return instance;
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public FavouriteDbHelper(Context context) {
        super(context, DB_NAME, DB_VERSION, CURR_CLASS);
        try {
            dbOpe = getDao(CURR_CLASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据,假如不存在就添加，假如存在就更新
     *
     * @param value 对象
     */
    public void addEntity(Topic value) {
        try {
            dbOpe.create(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条数据
     *
     * @param value 对象
     */
    public void delEntity(Topic value) {
        if (isModelExit(value)) {
            try {
                dbOpe.delete(value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 更新一条数据
     *
     * @param value 对象
     */
    public void updateEntity(Topic value) {
        try {
            dbOpe.update(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新对象 ，先删除，后添加
     *
     * @param value 被添加对象
     */
    public void addFunctionItem(Topic value) {
        Topic curr = getModeByTopicCode(value.getTopicId());
        if (curr != null) {
            delEntity(curr);
        }
        addEntity(value);
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<Topic> getAll() {
        try {
            return dbOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id，获取对象
     *
     * @param value id
     * @return 对象
     */
    public Topic getModeByTopicCode(String value) {
        List<Topic> lists = getAll();
        if (lists != null && lists.size() != 0) {
            for (int i = 0; i < lists.size(); i++) {
                if (value == lists.get(i).getTopicId()) {
                    return lists.get(i);
                }
            }
        }
        return null;

    }

    /**
     * 是否存在改对象
     *
     * @param value 对象
     * @return 是否存在
     */
    public boolean isModelExit(Topic value) {
        Topic curr = getModeByTopicCode(value.getTopicId());

        return curr == null ? false : true;
    }

}
