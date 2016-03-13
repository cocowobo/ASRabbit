package com.ht.baselib.helper.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.ht.baselib.utils.MD5;

/**
 * <p>
 * 一级缓存-内存缓存帮助类
 * <p/>
 * @author zmingchun
 * @version 1.0 (2015-05-30)
 */
public final class LruCacheHelper {
    private LruCacheHelper() {}

    private static LruCache<String, Bitmap> mCache;

    /**
     * 初始化LruCache
     */
    public static void openCache() {
        openCache(CacheConfig.MAX_MEMORY_CACHE_SIZE);
    }

    /**
     * 初始化LruCache
     * @param maxSize 缓存上限,默认为应用程序的可用内存四分之一
     */
    public static void openCache(int maxSize) {
        if(maxSize<=0) {
            maxSize = CacheConfig.MAX_MEMORY_CACHE_SIZE;
        }
        mCache = new LruCache<String, Bitmap>((int) maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回Bitmap对象所占大小，单位：kb
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 把图片写入内存缓存
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
    public static Boolean dumpBitmap(String keyCache, Bitmap bitmap) {
        return null != mCache.put((new MD5()).getMD5ofStrToLowerCase(keyCache), bitmap);
    }

    /**
     * 把图片写入内存缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
//    public static Observable<Boolean> rxDumpBitmap(String keyCache, Bitmap bitmap) {
//        return Observable.defer(() -> Observable.just(dumpBitmap(keyCache,bitmap)));
//    }

    /**
     * 从内存缓存中读取图片数据
     * @param keyCache 缓存key
     * */
    public static Bitmap loadBitmap(String keyCache) {
        return mCache.get((new MD5()).getMD5ofStrToLowerCase(keyCache));
    }

    /**
     * 从内存缓存中读取图片数据-结合rxjava异步化
     * @param keyCache 缓存key
     * @return
     */
//    public static Observable<Bitmap> rxLoadBitmap(String keyCache) {
//        return Observable.defer(() -> Observable.just(loadBitmap(keyCache)));
//    }

    /**
     * 检查key对应的缓存是否存在
     * @param keyCache 缓存key
     * @return
     */
    public static boolean hasCache(String keyCache) {
        return null!=mCache.get((new MD5()).getMD5ofStrToLowerCase(keyCache));
    }

    /**
     * 关闭缓存，不需要手动处理
     */
    public static void closeCache() {
        // 不需要手动处理，LruCache会自动清理的
    }
}
