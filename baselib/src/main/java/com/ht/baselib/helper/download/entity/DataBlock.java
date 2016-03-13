
package com.ht.baselib.helper.download.entity;

/**
 * Msg:数据块
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DataBlock {

    /**
     * 空闲状态
     */
    public static final int FREE = 0;

    /**
     * 正在下载
     */
    public static final int DOWNLOADING = 1;

    /**
     * 下载完成
     */
    public static final int FINISH = 2;

    private long blockIndex;

    private long start;

    private long end;

    private int state;

    public long getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(long blockIndex) {
        this.blockIndex = blockIndex;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
