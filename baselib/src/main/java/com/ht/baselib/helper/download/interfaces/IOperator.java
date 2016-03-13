
package com.ht.baselib.helper.download.interfaces;

import com.ht.baselib.helper.download.entity.DownloadFile;

import java.util.HashMap;
import java.util.List;

/**
 * Msg: 文件下载信息保存接口，外部可以使用数据库或者文件保存
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public interface IOperator {

    /**
     * 新增文件信息
     * 
     * @param file 文件
     * @return 返回新增文件ID
     */
    public long insertFile(DownloadFile file);

    /**
     * 更新文件信息
     * 
     * @param file 文件
     * @return 是否更新成功
     */
    public boolean updateFile(DownloadFile file);

    /**
     * 更新文件信息
     * 
     * @param updateFileds 更新的字段与它的值
     * @param selection  查询条件
     * @param selectionArgs 查询条件赋值
     * @return
     */
    public boolean updateFile(HashMap<String, Object> updateFileds, String selection,
            String[] selectionArgs);

    /**
     * 删除文件信息
     * 
     * @param id 文件ID
     * @return 是否删除成功
     */
    public boolean deleteFile(long id);

    /**
     * 删除文件信息
     * 
     * @param key 文件唯一标识符
     * @return
     */
    public boolean deleteFile(String key);

    /**
     * 查询文件信息
     * 
     * @param id 文件ID
     * @return 返回文件信息
     */
    public DownloadFile queryFile(long id);

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs);

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @param orderby 排序条件
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs, String orderby);

    /**
     * 查询数量
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回数量
     */
    public int getCount(String selection, String[] selectionArgs);
}
