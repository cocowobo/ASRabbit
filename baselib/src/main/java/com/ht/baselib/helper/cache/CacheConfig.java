package com.ht.baselib.helper.cache;

/**
 * 缓存参数配置相关
 * @author zmingchun
 * @version 1.0 (2015-05-30)
 */
public class CacheConfig {

    /**单位KB*/
    public static final int KB = 1024;
    /**单位MB*/
    public static final int MB = 1024 * KB;
    /**分配的可用内存*/
    public static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    /**硬盘缓存上限默认为100M*/
    public static final int DISK_CAHCE_DEFAULT_SIZE = 50 * MB;
    /**缓存的File数量 大小*/
    public static final int DISK_FILE_COUNT = 100;
    /**内存缓存上线默认为可用内存的八分之一*/
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE/8;
    /**IO buffer 大小*/
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    // Fresco 所用
    //--------------------------------
    /**磁盘缓存上限*/
    public static final long MAX_DISK_CACHE_SIZE = DISK_CAHCE_DEFAULT_SIZE;
    /**小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）*/
    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * MB;
    /**小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）*/
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * MB;
    /**小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）*/
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * MB;
    /**默认图极低磁盘空间缓存的最大值*/
    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * MB;
    /**默认图低磁盘空间缓存的最大值*/
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * MB;
    /**小图所放路径的文件夹名*/
    public static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "mini_cache";
    /**默认图所放路径的文件夹名*/
    public static final String IMAGE_PIPELINE_CACHE_DIR = "default_cache";

}
