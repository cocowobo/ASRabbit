
package com.ht.baselib.helper.download;

import java.util.HashMap;
import java.util.List;

import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IOperator;

/**
 * Msg:默认文件信息保存类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DefaultOperator implements IOperator {

    @Override
    public long insertFile(DownloadFile file) {
        return 1;
    }

    @Override
    public boolean updateFile(DownloadFile file) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateFile(HashMap<String, Object> updateFileds, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteFile(long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteFile(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public DownloadFile queryFile(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs, String orderby) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCount(String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
