package com.ht.baselib.helper.download.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ht.baselib.helper.dbhelper.DatabaseHelper;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.constants.FileColumns;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>下载数据库操作帮助类
 *
 * @author ljc
 * @version 1.0 (2015/11/09)
 */
public class DownloadDbHelper {

    /**
     * 表名
     */
    public static final String TABLE_NAME = FileColumns.TABLE_NAME;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "download.db";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    /**
     * DemoDbHelper实例
     */
    private static DownloadDbHelper instance;
    /**
     * 数据库操作
     */
    private Dao<DownloadFile, Integer> dbOpe;
    /**
     * DatabaseHelper实体
     */
    private DatabaseHelper helper;

    /**
     * 获取DemoDbHelper实体
     *
     * @param context 上下文
     * @return DemoDbHelper实体
     */
    public static DownloadDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadDbHelper(context);
        }
        return instance;
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public DownloadDbHelper(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context, DB_NAME, DB_VERSION, DownloadFile.class);
            dbOpe = helper.getDao(DownloadFile.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     *
     * @param value 对象
     */
    public int addEntity(DownloadFile value) {
        if (!isModelExit(value)) {
            try {
                return dbOpe.create(value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 删除一条数据
     *
     * @param value 对象
     */
    public int delEntity(DownloadFile value) {
        if (isModelExit(value)) {
            try {
                return dbOpe.delete(value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 更新一条数据
     *
     * @param value 对象
     */
    public int updateEntity(DownloadFile value) {
        try {
            return dbOpe.update(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param value 下载文件实体对象
     * @param key   下载文件唯一标识符
     * @return 成功返回大于0
     */
    public int update(DownloadFile value, String key) {
        try {
            UpdateBuilder<DownloadFile, Integer> updateBuilder = dbOpe.updateBuilder();
            updateBuilder.where().eq(FileColumns.KEY, key);
            updateBuilder.updateColumnValue(FileColumns.RES_URL, value.getResUrl());
            updateBuilder.updateColumnValue(FileColumns.FILE_PATH, value.getFilePath());
            updateBuilder.updateColumnValue(FileColumns.FILE_NAME, value.getFileName());
            updateBuilder.updateColumnValue(FileColumns.FILE_SIZE, value.getFileSize());
            updateBuilder.updateColumnValue(FileColumns.MIME_TYPE, value.getMimeType());
            updateBuilder.updateColumnValue(FileColumns.HAVE_READ, value.getHaveRead());
            updateBuilder.updateColumnValue(FileColumns.STATE, value.getState());
            updateBuilder.updateColumnValue(FileColumns.CLASSID, value.getClassId());
            updateBuilder.updateColumnValue(FileColumns.IS_DELETE, value.getIsDelete());
            updateBuilder.updateColumnValue(FileColumns.EXT1, value.getExt1());
            updateBuilder.updateColumnValue(FileColumns.EXT2, value.getExt2());
            updateBuilder.updateColumnValue(FileColumns.EXT3, value.getExt3());
            updateBuilder.updateColumnValue(FileColumns.EXT4, value.getExt4());
            updateBuilder.updateColumnValue(FileColumns.EXT5, value.getExt5());
            updateBuilder.updateColumnValue(FileColumns.EXT6, value.getExt6());
            updateBuilder.updateColumnValue(FileColumns.EXT7, value.getExt7());
            updateBuilder.updateColumnValue(FileColumns.EXT8, value.getExt8());
            updateBuilder.updateColumnValue(FileColumns.EXT9, value.getExt9());
            updateBuilder.updateColumnValue(FileColumns.EXT10, value.getExt10());
            updateBuilder.updateColumnValue(FileColumns.EXT11, value.getExt11());
            updateBuilder.updateColumnValue(FileColumns.EXT12, value.getExt12());
            updateBuilder.updateColumnValue(FileColumns.EXT13, value.getExt13());
            updateBuilder.updateColumnValue(FileColumns.EXT14, value.getExt14());
            updateBuilder.updateColumnValue(FileColumns.EXT15, value.getExt15());
            updateBuilder.updateColumnValue(FileColumns.EXT16, value.getExt16());
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long update(ContentValues cv, String selection, String[] selectionArgs) {
        return helper.getReadableDatabase().update(TABLE_NAME, cv, selection,
                selectionArgs);
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<DownloadFile> getAll() {
        try {
            return dbOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否存在改对象
     *
     * @param value 对象
     * @return 是否存在
     */
    public boolean isModelExit(DownloadFile value) {
        DownloadFile downloadFileModel = query(value.getKey());
        return false;
    }

    public long delete(String key) {
        String selection = FileColumns.KEY + " = ? ";
        String[] selectionArgs = {
                key
        };
        return helper.getReadableDatabase().delete(TABLE_NAME, selection, selectionArgs);
    }

    public long delete(long id) {
        String selection = FileColumns.ID + " = ? ";
        String[] selectionArgs = {
                String.valueOf(id)
        };
        return helper.getReadableDatabase().delete(TABLE_NAME,selection, selectionArgs);
    }

    public DownloadFile query(String key) {
        try {
            QueryBuilder<DownloadFile, Integer> qb = dbOpe.queryBuilder();
            List<DownloadFile> downloadFileModels =qb.where().eq(FileColumns.KEY, key).query();
            if(downloadFileModels!=null && downloadFileModels.size()>0){
                return downloadFileModels.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DownloadFile query(long id) {
        {
            try {
                QueryBuilder<DownloadFile, Integer> qb = dbOpe.queryBuilder();
                List<DownloadFile> downloadFileModels =qb.where().eq(FileColumns.ID, id).query();
                if(downloadFileModels!=null && downloadFileModels.size()>0){
                    return downloadFileModels.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public  int getCount(String selection, String[] selectionArgs) {
        String[] projection = {
                " count(*) "
        };
        Cursor c = helper.getReadableDatabase().query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        int count = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
        }
        return count;
    }

    public synchronized List<DownloadFile> query(String selection, String[] selectionArgs,
                                                 String orderby) {
        Cursor c = helper.getReadableDatabase().query(TABLE_NAME, null, selection,
                selectionArgs, null, null, orderby);
        return getDownloadFileFromCursor(c);
    }

    public synchronized List<DownloadFile> query(String selection, String[] selectionArgs,
                                                 String orderby, int limit) {
        Cursor c =  helper.getReadableDatabase().query(TABLE_NAME, null, selection,
                selectionArgs, null, null, orderby, String.valueOf(limit));
        return getDownloadFileFromCursor(c);
    }

    public DownloadFile query(String selection, String[] selectionArgs) {
        List<DownloadFile> files = query(selection, selectionArgs, null);
        if (files != null && files.size() > 0) {
            return files.get(0);
        }
        return null;
    }
    /**
     * 获取下载文件信息
     *
     * @param c
     * @return
     */
    public static List<DownloadFile> getDownloadFileFromCursor(Cursor c) {
        if (c != null) {
            List<DownloadFile> files = new ArrayList<DownloadFile>(c.getCount());
            DownloadFile file = null;
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                file = new DownloadFile();
                file.setId((int)c.getLong(c.getColumnIndexOrThrow(FileColumns.ID)));
                file.setResUrl(c.getString(c.getColumnIndexOrThrow(FileColumns.RES_URL)));
                file.setFilePath(c.getString(c.getColumnIndexOrThrow(FileColumns.FILE_PATH)));
                file.setFileName(c.getString(c.getColumnIndexOrThrow(FileColumns.FILE_NAME)));
                file.setFileSize(c.getLong(c.getColumnIndexOrThrow(FileColumns.FILE_SIZE)));
                file.setHaveRead(c.getLong(c.getColumnIndexOrThrow(FileColumns.HAVE_READ)));
                file.setMimeType(c.getString(c.getColumnIndexOrThrow(FileColumns.MIME_TYPE)));
                file.setState(c.getInt(c.getColumnIndexOrThrow(FileColumns.STATE)));
                file.setKey(c.getString(c.getColumnIndexOrThrow(FileColumns.KEY)));
                file.setClassId(c.getInt(c.getColumnIndexOrThrow(FileColumns.CLASSID)));
                file.setIsDelete(c.getInt(c.getColumnIndexOrThrow(FileColumns.IS_DELETE)));
                file.setExt1(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT1)));
                file.setExt2(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT2)));
                file.setExt3(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT3)));
                file.setExt4(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT4)));
                file.setExt5(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT5)));
                file.setExt6(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT6)));
                file.setExt7(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT7)));
                file.setExt8(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT8)));
                file.setExt9(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT9)));
                file.setExt10(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT10)));
                file.setExt11(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT11)));
                file.setExt12(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT12)));
                file.setExt13(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT13)));
                file.setExt14(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT14)));
                file.setExt15(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT15)));
                file.setExt16(c.getString(c.getColumnIndexOrThrow(FileColumns.EXT16)));
                files.add(file);
            }
            c.close();
            return files;
        }
        return null;
    }
}
