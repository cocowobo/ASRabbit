package com.ht.baselib.helper.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.ht.baselib.BuildConfig;
import com.ht.baselib.utils.FileUtils;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.MD5;
import com.ht.baselib.utils.StringUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * 硬盘缓存帮助类
 * <br/> 缓存的key，均已MD5加密
 * <p/>
 * @author zmingchun
 * @version 1.0 (2015-05-30)
 */
public final class DiskLruCacheHelper {
    private static final String LOG_TAG = DiskLruCacheHelper.class.getSimpleName();

    private static DiskLruCache mDiskCache;

    /**(文件夹名前加点，防止相册读取)app文件缓存文件夹-子级分类文件夹名称-视频*/
    public static final String UNIQUE_NAME_VIDEO = ".video";
    /**app文件缓存文件夹-子级分类文件夹名称-图片*/
    public static final String UNIQUE_NAME_BITMAP = ".bitmap";
    /**app文件缓存文件夹-子级分类文件夹名称-其他*/
    public static final String UNIQUE_NAME_OTHER = ".other";
    /**app文件缓存文件夹-子级分类文件夹名称-视频文件夹的下级文件夹-视频录制*/
    public static final String UNIQUE_NAME_VIDEO_CAMERA = "camera";

    /**视频后缀，用于下载保存视频至相册*/
    public static final String UNIQUE_NAME_VIDEO_MP4 = ".mp4";
    /**app文件缓存文件夹-缓存视频后缀(读取缓存产生的临时视频文件后缀加此标识，防止其他应用读取)*/
    public static final String UNIQUE_NAME_VIDEO_MP4_TEMP = ".mp4_";


    private DiskLruCacheHelper() {}

    /**
     * 打开DiskLruCache（本应用默认）
     * @param context 上下文
     * @param uniqueName 缓存文件夹-子级分类文件夹名称
     */
    public static DiskLruCache openCache(Context context, String uniqueName) {
        return openCache(getDiskCacheDir(context,uniqueName));
    }

    /**
     * 打开DiskLruCache
     * @param directory 自定义缓存路径
     */
    public static DiskLruCache openCache(File directory) {
        return openCache(directory, BuildConfig.VERSION_CODE, CacheConfig.DISK_CAHCE_DEFAULT_SIZE);
    }

    /**
     * 打开DiskLruCache
     * @param directory 缓存路径
     * @param appVersion 应用版本号
     * @param maxSize 缓存数据的总上限，单位是字节
     */
    public static DiskLruCache openCache( File directory, int appVersion, int maxSize) {
        try {
            /**valueCount决定了一个key可以对应多少个缓存文件，默认为1，其他值不行*/
            mDiskCache = DiskLruCache.open(directory, appVersion, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskCache;
    }

    /** 同步日志 */
    public static void syncJournal() {
        try {
            if (null != mDiskCache) {
                mDiskCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 关闭缓存 */
    public static void closeCache() {
        if (null!= mDiskCache && !mDiskCache.isClosed()) {
            try {
                //clearTempFile();
                syncJournal();
                mDiskCache.close();
                mDiskCache = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查key对应的缓存是否存在
     * @param keyCache 缓存key
     * @return
     */
    public static boolean hasCache(String keyCache) {
        return containsKey((new MD5()).getMD5ofStrToLowerCase(keyCache));
    }

    /**
     * 是否存在key对应的缓存
     * @param keyCache md5加密过的key
     * @return
     */
    private static boolean containsKey(String keyCache) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            if (null!=mDiskCache && !mDiskCache.isClosed()) {
                snapshot = mDiskCache.get(keyCache);
                contained = snapshot != null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        return contained;
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(LOG_TAG, "disk cache CLEARED");
        }
        try {
            if (null != mDiskCache ) {
                mDiskCache.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除读取视频缓存时产生的临时文件
     */
    public static void clearTempFile() {
        if (null != mDiskCache ) {
            //清除缓存目录下的临时视频文件(后缀为UNIQUE_NAME_VIDEO_MP4_)
            File[] tempFiles = mDiskCache.getDirectory()
                    .listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return (filename.endsWith(UNIQUE_NAME_VIDEO_MP4_TEMP));
                        }
                    });
            if (null != tempFiles && tempFiles.length>0){
                for (File tempFile: tempFiles){
                    if(tempFile.isFile()){
                        if(tempFile.delete()) {
                            LogUtils.d(LOG_TAG, "成功删除临时视频文件：" + tempFile.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取磁盘缓存目录
     * @return
     */
    public static File getCacheFolder() {
        return mDiskCache.getDirectory();
    }

    /**
     * 获取缓存大小
     * @return
     */
    public static float getCacheSize(){
        return mDiskCache.size();
    }

    // 文件及文件流缓存
    //===========================================
    /**
     * 写入文件流缓存
     * @param keyCache 缓存key
     * @param inputStream 文件流
     * @return
     */
    public static Boolean dumpInputStream(String keyCache, InputStream inputStream){
        try {
            if (mDiskCache == null) {
                throw new IllegalStateException("Must call openCache() first!");
            }
            DiskLruCache.Editor mEditor = mDiskCache.edit((new MD5()).getMD5ofStrToLowerCase(keyCache));
            OutputStream outputStream = mEditor.newOutputStream(0);
            BufferedInputStream bin = new BufferedInputStream(inputStream);
            BufferedOutputStream bout = new BufferedOutputStream(outputStream);

            byte[] buf = new byte[1024];
            int len;
            while ((len = bin.read(buf)) != -1) {
                bout.write(buf, 0, len);
            }

            bout.close();
            outputStream.close();
            mEditor.commit();
            return true;
        }catch (IOException e){
            //nothing to do
        }
        return false;
    }

    /**
     * 写入文件流缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param inputStream 文件流
     * @return
     */
//    public static Observable<Boolean> rxDumpBitmap(String keyCache, InputStream inputStream) {
//        return Observable.defer(() -> Observable.just(dumpInputStream(keyCache, inputStream)));
//    }

    /**
     * 写入图片缓存
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
    public static Boolean dumpBitmap(String keyCache, Bitmap bitmap){
        try{
            if (mDiskCache == null) {
                throw new IllegalStateException("Must call openCache() first!");
            }

            DiskLruCache.Editor editor = mDiskCache.edit((new MD5()).getMD5ofStrToLowerCase(keyCache));

            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                if (success) {
                    editor.commit();
                    return true;
                } else {
                    editor.abort();
                }
            }
        }catch (IOException e){
            LogUtils.d(LOG_TAG, e.getMessage());
        }
        return false;
    }

    /**
     * 写入图片缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
//    public static Observable<Boolean> rxDumpBitmap(String keyCache, Bitmap bitmap) {
//        return Observable.defer(() -> Observable.just(dumpBitmap(keyCache,bitmap)));
//    }

    /**
     * 读取Bitmap缓存
     * @param keyCache 缓存key
     * @return
     */
    public static Bitmap loadBitmap(String keyCache){
        try {
            if (mDiskCache == null) {
                throw new IllegalStateException("Must call openCache() first!");
            }

            DiskLruCache.Snapshot snapshot = mDiskCache.get((new MD5()).getMD5ofStrToLowerCase(keyCache));

            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(inputStream);
            }

            return null;
        }catch (Exception e){
            LogUtils.d(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 读取Bitmap缓存-结合rxjava异步化
     * @param keyCache key 缓存key
     * @return
     */
//    public static Observable<Bitmap> rxLoadBitmap(String keyCache) {
//        return Observable.defer(() -> Observable.just(loadBitmap(keyCache)));
//    }

    /**
     * 读取InputStream缓存
     * @param keyCache 缓存key
     * @return
     */
    public static InputStream loadInputStream(String keyCache){
        try {
            if (mDiskCache == null) {
                throw new IllegalStateException("Must call openCache() first!");
            }

            DiskLruCache.Snapshot snapshot = mDiskCache.get((new MD5()).getMD5ofStrToLowerCase(keyCache));

            if (snapshot == null){
                return null;
            }else{
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取InputStream缓存-结合rxjava异步化
     * @param keyCache key 缓存key
     * @return
     */
//    public static Observable<InputStream> rxLoadInputStream(String keyCache) {
//        return Observable.defer(() -> Observable.just(loadInputStream(keyCache)));
//    }

    /**
     * 读取缓存文件
     * @param keyCache 缓存key(一般是本地绝对路径）
     * @return
     */
    public static String loadFile(String keyCache){
        InputStream in = null;
        try {
            in = loadInputStream(keyCache);
            byte[] bytes = new byte[0];
            bytes = FileUtils.getBytesFromStream(in);
            boolean result = FileUtils.saveBytesToFile(bytes, keyCache);
            if (result) {
                return keyCache;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取缓存文件-结合rxjava异步化
     * @param keyCache 缓存key(一般是本地绝对路径）
     * @return
     */
//    public static Observable<String> rxLoadFile(String keyCache) {
//        return Observable.defer(() -> Observable.just(loadFile(keyCache)));
//    }

    // 字符串缓存
    //===========================================
    /**
     * 缓存字符串
     * @param keyCache 缓存key
     * @param jsonStr json字符串
     */
    public void dumpString(String keyCache, String jsonStr) {
        if (BuildConfig.DEBUG) {
            LogUtils.d(LOG_TAG, "getDiskStringCache().putString\n" + keyCache + jsonStr);
        }
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit((new MD5()).getMD5ofStrToLowerCase(keyCache));
            if (editor == null) {
                return;
            }

            if (writeStringToFile(jsonStr, editor)) {
                mDiskCache.flush();
                editor.commit();
                if (BuildConfig.DEBUG) {
                    LogUtils.d(LOG_TAG, "string put on disk cache " + keyCache);
                }
            } else {
                editor.abort();
                if (BuildConfig.DEBUG) {
                    LogUtils.d(LOG_TAG, "string on: image put on disk cache " + keyCache);
                }
            }
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                LogUtils.d(LOG_TAG, "string on: image put on disk cache " + keyCache);
            }
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }

    }

    /**
     * 获取缓存字符串
     * @param keyCache 缓存key
     * @return
     */
    public String loadString(String keyCache) {
        String value = null;
        DiskLruCache.Snapshot snapshot = null;
        try {

            snapshot = mDiskCache.get((new MD5()).getMD5ofStrToLowerCase(keyCache));
            if (snapshot == null) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream(in, CacheConfig.IO_BUFFER_SIZE);
                byte[] contents = new byte[1024];

                int bytesRead = 0;
                StringBuffer strFileContents = new StringBuffer();
                while ((bytesRead = buffIn.read(contents)) != -1) {
                    strFileContents.append(new String(contents, 0, bytesRead));
                }
                value = strFileContents.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        if (BuildConfig.DEBUG) {
            LogUtils.d(LOG_TAG, value == null ? "" : "image read from disk " + keyCache);
            LogUtils.d(LOG_TAG, "getDiskStringCache().getString\n" + value);
        }
        return value;
    }

    /**
     * 将字符串写到缓存文件
     * @param str 字符串内容
     * @param editor 缓存编辑器
     * @return
     * @throws IOException io读写异常
     */
    private boolean writeStringToFile(String str, DiskLruCache.Editor editor) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), CacheConfig.IO_BUFFER_SIZE);
            out.write(str.getBytes("UTF-8"));
            return true;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    // 工具类
    //=============================================

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     * @param context 上下文
     * @param uniqueName 用于区分不同的缓存，如bitmap\video\other;为空则表示缓存根目录
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && null != context.getExternalCacheDir()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        if (!StringUtils.isBlank(uniqueName)) {
            File subCachePath = new File(cachePath + File.separator + uniqueName);
            if (!subCachePath.exists()){
                subCachePath.mkdir();
            }
            return subCachePath;
        }else{
            return  new File(cachePath);
        }
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     * @param context 上下文
     * @param uniqueName 用于区分不同的缓存，如bitmap或video或other
     * @return
     */
    public static String getDiskCachePath(Context context, String uniqueName) {
        return getDiskCacheDir(context,uniqueName).getAbsolutePath();
    }

    /**
     * 获取硬盘缓存的路径地址-图片
     * @param context 上下文
     * @return
     */
    public static String getDiskCacheBitmapPath(Context context) {
        return getDiskCacheDir(context,UNIQUE_NAME_BITMAP).getAbsolutePath();
    }

    /**
     * 获取硬盘缓存的路径地址-视频
     * @param context 上下文
     * @return
     */
    public static String getDiskCacheVideoPath(Context context) {
        return getDiskCacheDir(context,UNIQUE_NAME_VIDEO).getAbsolutePath();
    }

    /**
     * 获取硬盘缓存的路径地址-其他
     * @param context 上下文
     * @return
     */
    public static String getDiskCacheOtherPath(Context context) {
        return getDiskCacheDir(context,UNIQUE_NAME_OTHER).getAbsolutePath();
    }
}
