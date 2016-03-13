
package com.ht.baselib.helper.download.util;

import com.ht.baselib.helper.download.entity.BlockedDownloadFile;
import com.ht.baselib.helper.download.entity.DataBlock;

/**
 * Msg:空闲块分配类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public final class FreeBlockManager {

    /**
     * 获取空闲块
     * 
     * @param file 分块下载实体
     * @return
     */
    public static synchronized int getFreeBlockIndex(BlockedDownloadFile file) {
        int freeBlockIndex = -1;
        int blockNum = file.getBlockNum();
        int bufferedIndex = file.getBufferedIndex();
        DataBlock[] blocks = file.getDataBlocks();
        // 是否连续块
        boolean isLinked = true;
        for (int i = bufferedIndex + 1; i < blockNum; i++) {
            int state = blocks[i].getState();
            if (state == DataBlock.FREE) {
                isLinked = false;
                freeBlockIndex = i;
                blocks[i].setState(DataBlock.DOWNLOADING);
                break;
            } else if (state == DataBlock.FINISH && isLinked) {
                file.setBufferedIndex(i);
            } else if (state == DataBlock.DOWNLOADING) {
                isLinked = false;
            }
        }
        return freeBlockIndex;
    }

}
