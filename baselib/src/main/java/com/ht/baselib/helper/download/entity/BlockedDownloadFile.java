
package com.ht.baselib.helper.download.entity;


/**
 * Msg:分块下载的文件
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class BlockedDownloadFile extends DownloadFile {

	private int blockNum;

    private int blockSize;

    private int bufferedIndex = -1;

    private DataBlock[] dataBlocks;

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBufferedIndex() {
        return bufferedIndex;
    }

    public void setBufferedIndex(int bufferedIndex) {
        this.bufferedIndex = bufferedIndex;
    }

    public DataBlock[] getDataBlocks() {
        return dataBlocks;
    }

    /**
     * 分割数据块（调用这个方法之前，必须先调用setBlockSize方法，初始化blockSize）
     */
    void splitBlocks() {
        blockNum = (int) ((fileSize + blockSize - 1) / blockSize);
        dataBlocks = new DataBlock[blockNum];
        for (int i = 0; i < blockNum; i++) {
            dataBlocks[i] = new DataBlock();
            dataBlocks[i].setBlockIndex(i);
            dataBlocks[i].setStart(i * blockSize);
            dataBlocks[i].setEnd((i + 1) * blockSize - 1);
            dataBlocks[i].setState(DataBlock.FREE);
        }
        dataBlocks[blockNum - 1].setEnd(fileSize - 1);
    }

    /**
     * 分割数据块（调用这个方法之前，必须先调用setBlockSize方法，初始化blockSize）
     * 
     * @param bufferedIndex 缓冲好的块的索引号
     */
    public void splitBlocks(int bufferedIndex) {
        blockNum = (int) ((fileSize + blockSize - 1) / blockSize);
        dataBlocks = new DataBlock[blockNum];
        for (int i = 0; i < blockNum; i++) {
            dataBlocks[i] = new DataBlock();
            dataBlocks[i].setBlockIndex(i);
            dataBlocks[i].setStart(i * blockSize);
            dataBlocks[i].setEnd((i + 1) * blockSize - 1);
            dataBlocks[i].setState(i <= bufferedIndex ? DataBlock.FINISH : DataBlock.FREE);
        }
        dataBlocks[blockNum - 1].setEnd(fileSize - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" blockNum=" + blockNum);
        sb.append(" blockSize=" + blockSize);
        sb.append(" bufferedIndex=" + bufferedIndex);
        return sb.toString();
    }
}
