
package com.ht.baselib.helper.download.entity;

import com.ht.baselib.helper.download.Statistics;
import com.ht.baselib.helper.download.constants.FileColumns;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Msg:文件实体类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
@DatabaseTable(tableName = FileColumns.TABLE_NAME)
public class DownloadFile {


    /**
     * 数据库自增的id
     */
    @DatabaseField(generatedId = true, columnName = FileColumns.ID)
    protected int id;
    /**
     * 文件下载地址
     */
    @DatabaseField(columnName = FileColumns.RES_URL)
    protected String resUrl;
    /**
     * 文件名称
     */
    @DatabaseField(columnName = FileColumns.FILE_NAME)
    protected String fileName;
    /**
     * 文件类型
     */
    @DatabaseField(columnName = FileColumns.FILE_PATH)
    protected String filePath;
    /**
     * 已下载长度
     */
    @DatabaseField(columnName = FileColumns.HAVE_READ)
    protected long haveRead;
    /**
     * 文件总长度
     */
    @DatabaseField(columnName = FileColumns.FILE_SIZE)
    protected long fileSize;
    /**
     * 文件类型，资源的媒体类型
     */
    @DatabaseField(columnName = FileColumns.MIME_TYPE)
    protected String mimeType;
    /**
     * 下载状态
     * ({@link com.ht.baselib.helper.download.FileDownloader#READY
     * FileDownloader#DOWNLOADING...} )
     */
    @DatabaseField(columnName = FileColumns.STATE)
    protected int state;
    /**
     * 文件唯一标识符，如果服务器没有规定，客户端一般使用下载地址的md5
     */
    @DatabaseField(columnName = FileColumns.KEY)
    protected String key;
    /**
     * 分类id，可自定义，假如是apk下载平台，1=游戏文件，2=应用文件等等
     */
    @DatabaseField(columnName = FileColumns.CLASSID)
    protected int classId;
    /**
     * 是否删除
     */
    @DatabaseField(columnName = FileColumns.IS_DELETE)
    protected int isDelete;

    protected boolean isNewDownload;
    /**
     * 额外字段，用于保存文件的其他相关信息，例如，如果是游戏apk，就保存游戏的其他信息
     */
    @DatabaseField(columnName = FileColumns.EXT1)
    protected String ext1;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT2)
    protected String ext2;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT3)
    protected String ext3;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT4)
    protected String ext4;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT5)
    protected String ext5;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT6)
    protected String ext6;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT7)
    protected String ext7;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT8)
    protected String ext8;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT9)
    protected String ext9;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT10)
    protected String ext10;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT11)
    protected String ext11;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT12)
    protected String ext12;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT13)
    protected String ext13;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT14)
    protected String ext14;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT15)
    protected String ext15;
    /**
     * 额外字段
     */
    @DatabaseField(columnName = FileColumns.EXT16)
    protected String ext16;

    protected Statistics statis;

    /**
     * 文件构造器
     */
    public DownloadFile() {
        statis = new Statistics();
    }

    public Statistics getStatis() {
        return statis;
    }

    public void setStatis(Statistics statis) {
        this.statis = statis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getHaveRead() {
        return haveRead;
    }

    public void setHaveRead(long haveRead) {
        this.haveRead = haveRead;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public boolean isNewDownload() {
        return isNewDownload;
    }

    public void setIsNewDownload(boolean isNewDownload) {
        this.isNewDownload = isNewDownload;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt6() {
        return ext6;
    }

    public void setExt6(String ext6) {
        this.ext6 = ext6;
    }

    public String getExt7() {
        return ext7;
    }

    public void setExt7(String ext7) {
        this.ext7 = ext7;
    }

    public String getExt8() {
        return ext8;
    }

    public void setExt8(String ext8) {
        this.ext8 = ext8;
    }

    public String getExt9() {
        return ext9;
    }

    public void setExt9(String ext9) {
        this.ext9 = ext9;
    }

    public String getExt10() {
        return ext10;
    }

    public void setExt10(String ext10) {
        this.ext10 = ext10;
    }

    public String getExt11() {
        return ext11;
    }

    public void setExt11(String ext11) {
        this.ext11 = ext11;
    }

    public String getExt12() {
        return ext12;
    }

    public void setExt12(String ext12) {
        this.ext12 = ext12;
    }

    public String getExt13() {
        return ext13;
    }

    public void setExt13(String ext13) {
        this.ext13 = ext13;
    }

    public String getExt14() {
        return ext14;
    }

    public void setExt14(String ext14) {
        this.ext14 = ext14;
    }

    public String getExt15() {
        return ext15;
    }

    public void setExt15(String ext15) {
        this.ext15 = ext15;
    }

    public String getExt16() {
        return ext16;
    }

    public void setExt16(String ext16) {
        this.ext16 = ext16;
    }

    /**
     * @param read 新增已读长度
     */
    public synchronized void addHaveRead(long read) {
        this.haveRead += read;
    }

    /**
     * 重置已读长度
     */
    public synchronized void resetHaveRead() {
        this.haveRead = 0;
    }

    /**
     * @param read 要减少的已读长度
     */
    public synchronized void reduceHaveRead(long read) {
        this.haveRead -= read;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" DownloadFile-->");
        // sb.append(" key=" + key);
        sb.append(" id=" + id);
        // sb.append(" resUrl=" + resUrl);
        sb.append(" fileName=" + fileName);
        sb.append(" filePath=" + filePath);
        sb.append(" haveRead=" + haveRead);
        sb.append(" fileSize=" + fileSize);
        sb.append(" mimeType=" + mimeType);
        sb.append(" classId=" + classId);
        sb.append(" isDelete=" + isDelete);
        sb.append(" isNewDownload=" + isNewDownload);
        sb.append(" state=" + state);
        sb.append(" ext1=" + ext1);
        sb.append(" ext2=" + ext2);
        sb.append(" ext3=" + ext3);
        sb.append(" ext4=" + ext4);
        sb.append(" ext5=" + ext5);
        sb.append(" ext6=" + ext6);
        sb.append(" ext7=" + ext7);
        sb.append(" ext8=" + ext8);
        sb.append(" ext9=" + ext9);
        sb.append(" ext10=" + ext10);
        sb.append(" ext11=" + ext11);
        sb.append(" ext12=" + ext12);
        sb.append(" ext13=" + ext13);
        sb.append(" ext14=" + ext14);
        sb.append(" ext15=" + ext15);
        sb.append(" ext16=" + ext16);

        sb.append(" statis=\"" + statis.toString() + "\"");
        return sb.toString();
    }

}
