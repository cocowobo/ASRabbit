
package com.ht.baselib.helper.download.android;

import android.content.ContentValues;
import android.content.Context;

import com.ht.baselib.helper.download.DefaultOperator;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.constants.FileColumns;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Msg: 下载信息保存类
 * Update:  2015-11-6
 * Version: 1.0
 * Created by laijiacai on 2015-11-6 16:10.
 */
public class DownloadInfoSaver extends DefaultOperator {

    private Context mContext;

    private DownloadDbHelper mDownloadDbHelper;

    public DownloadInfoSaver(Context context) {
        mContext = context;
        mDownloadDbHelper = DownloadDbHelper.getInstance(context);
    }

    /**
     * 新增文件信息
     * 
     * @param file 文件
     * @return 返回新增文件ID
     */
    public long insertFile(DownloadFile file) {
        return mDownloadDbHelper.addEntity((DownloadFile) file);
    }

    /**
     * 更新文件信息
     * 
     * @param file 文件
     * @return 是否更新成功
     */
    public boolean updateFile(DownloadFile file) {
        return mDownloadDbHelper.update((DownloadFile) file, file.getKey()) > 0;
    }

    @Override
    public boolean updateFile(HashMap<String, Object> updateFileds, String selection,
            String[] selectionArgs) {
        if (updateFileds != null && updateFileds.size() > 0) {
            ContentValues cv = new ContentValues();
            Set<String> keys = updateFileds.keySet();
            for (String key : keys) {
                if (FileColumns.KEY.equals(key)) {
                    cv.put(FileColumns.KEY, (String) updateFileds.get(key));
                } else if (FileColumns.CLASSID.equals(key)) {
                    cv.put(FileColumns.CLASSID, (Integer) updateFileds.get(key));
                } else if (FileColumns.FILE_NAME.equals(key)) {
                    cv.put(FileColumns.FILE_NAME, (String) updateFileds.get(key));
                } else if (FileColumns.FILE_PATH.equals(key)) {
                    cv.put(FileColumns.FILE_PATH, (String) updateFileds.get(key));
                } else if (FileColumns.FILE_SIZE.equals(key)) {
                    cv.put(FileColumns.FILE_SIZE, (Long) updateFileds.get(key));
                } else if (FileColumns.RES_URL.equals(key)) {
                    cv.put(FileColumns.RES_URL, (String) updateFileds.get(key));
                } else if (FileColumns.HAVE_READ.equals(key)) {
                    cv.put(FileColumns.HAVE_READ, (Long) updateFileds.get(key));
                } else if (FileColumns.MIME_TYPE.equals(key)) {
                    cv.put(FileColumns.MIME_TYPE, (String) updateFileds.get(key));
                } else if (FileColumns.STATE.equals(key)) {
                    cv.put(FileColumns.STATE, (Integer) updateFileds.get(key));
                } else if (FileColumns.IS_DELETE.equals(key)) {
                    cv.put(FileColumns.IS_DELETE, (Integer) updateFileds.get(key));
                } else if (FileColumns.EXT1.equals(key)) {
                    cv.put(FileColumns.EXT1, (String) updateFileds.get(key));
                } else if (FileColumns.EXT2.equals(key)) {
                    cv.put(FileColumns.EXT2, (String) updateFileds.get(key));
                } else if (FileColumns.EXT3.equals(key)) {
                    cv.put(FileColumns.EXT3, (String) updateFileds.get(key));
                } else if (FileColumns.EXT4.equals(key)) {
                    cv.put(FileColumns.EXT4, (String) updateFileds.get(key));
                } else if (FileColumns.EXT5.equals(key)) {
                    cv.put(FileColumns.EXT5, (String) updateFileds.get(key));
                } else if (FileColumns.EXT6.equals(key)) {
                    cv.put(FileColumns.EXT6, (String) updateFileds.get(key));
                } else if (FileColumns.EXT7.equals(key)) {
                    cv.put(FileColumns.EXT7, (String) updateFileds.get(key));
                } else if (FileColumns.EXT8.equals(key)) {
                    cv.put(FileColumns.EXT8, (String) updateFileds.get(key));
                } else if (FileColumns.EXT9.equals(key)) {
                    cv.put(FileColumns.EXT9, (String) updateFileds.get(key));
                } else if (FileColumns.EXT10.equals(key)) {
                    cv.put(FileColumns.EXT10, (String) updateFileds.get(key));
                } else if (FileColumns.EXT11.equals(key)) {
                    cv.put(FileColumns.EXT11, (String) updateFileds.get(key));
                } else if (FileColumns.EXT12.equals(key)) {
                    cv.put(FileColumns.EXT12, (String) updateFileds.get(key));
                } else if (FileColumns.EXT13.equals(key)) {
                    cv.put(FileColumns.EXT13, (String) updateFileds.get(key));
                } else if (FileColumns.EXT14.equals(key)) {
                    cv.put(FileColumns.EXT14, (String) updateFileds.get(key));
                } else if (FileColumns.EXT15.equals(key)) {
                    cv.put(FileColumns.EXT15, (String) updateFileds.get(key));
                } else if (FileColumns.EXT16.equals(key)) {
                    cv.put(FileColumns.EXT16, (String) updateFileds.get(key));
                }
            }
            return mDownloadDbHelper.update(cv, selection, selectionArgs) > 0;
        } else {
            return false;
        }
    }

    /**
     * 删除文件信息
     * 
     * @param id 文件ID
     * @return 是否删除成功
     */
    public boolean deleteFile(long id) {
        return mDownloadDbHelper.delete(id) > 0;
    }

    /**
     * 删除文件信息
     */
    public boolean deleteFile(String key) {
        return mDownloadDbHelper.delete(key) > 0;
    }

    /**
     * 查询文件信息
     * 
     * @param id 文件ID
     * @return 返回文件信息
     */
    public DownloadFile queryFile(long id) {
        return mDownloadDbHelper.query(id);
    }

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs) {
        return mDownloadDbHelper.query(selection, selectionArgs, null);
    }

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @param orderby 排序条件
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs, String orderby) {
        return mDownloadDbHelper.query(selection, selectionArgs, orderby);
    }

    /**
     * 查询数量
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回数量
     */
    public int getCount(String selection, String[] selectionArgs) {
        return mDownloadDbHelper.getCount(selection, selectionArgs);
    }

}
