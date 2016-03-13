package com.ht.baselib.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ht.baselib.R;
import com.ht.baselib.helper.cache.CacheConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Msg: 图片加载管理类
 * <br/> 目前只提供常用方法，后续根据需求可再扩展
 * Update:  2015-11-5
 * Version: 1.0
 * Created by chenchao on 2015-11-5 13:59.
 */
public final class ImageLoaderManager {
    /**
     * 单例对象
     */
    private static ImageLoaderManager instance;
    /**
     * 普通图片属性
     */
    private DisplayImageOptions imageOptions;
    /**
     * 头像属性
     */
    private DisplayImageOptions avatarOptions;
    /**
     * 通用属性，设置默认图
     */
    private DisplayImageOptions commonOptions;

    /**
     * 图片属性类型
     * <br/>
     * IMAGE：普通图片属性
     * <br/>
     * AVATAR：头像属性
     */
    public enum OptionsType {
        /**
         * 普通图片属性
         */
        IMAGE,
        /**
         * 头像属性
         */
        AVATAR,
        /**
         * 通用属性
         */
        COMMON,
        /**
         * 圆形头像属性
         */
        AVATAR_CIRCLE,
    }

    /**
     * 构造器
     */
    private ImageLoaderManager() {
    }

    /**
     * 获取ImageLoaderManger单例对象
     *
     * @return ImageLoaderManager
     */
    public static ImageLoaderManager getInstance() {
        if (instance == null) {
            instance = new ImageLoaderManager();
        }
        return instance;
    }

    /**
     * ImageLoader初始化
     * 建议在APPLICATION初始化时调用
     *
     * @param context 上下文对象
     */
    public void init(Context context) {
        /**缓存的目录地址*/
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "hd/cache/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                /**线程池*/
                .threadPoolSize(3) // default  线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) //default 设置当前线程的优先级
                /**内存缓存*/
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(CacheConfig.MAX_MEMORY_CACHE_SIZE))
                .memoryCacheSize(CacheConfig.MAX_MEMORY_CACHE_SIZE)
                /**硬盘缓存*/  //注意：disc的方法已经弃用
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheSize(CacheConfig.DISK_CAHCE_DEFAULT_SIZE) //硬盘缓存50MB
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                .diskCacheFileCount(CacheConfig.DISK_FILE_COUNT) //缓存的File数量100个
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                /**使用默认配置getImageOptions*/
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                /**超时，连接时间10秒，读取时间60秒*/
                .imageDownloader(new BaseImageDownloader(context, 10 * 1000, 60 * 1000))
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 加载图片，占位图默认为 OptionsType.IMAGE
     *
     * @param imageView ImageView
     * @param uri       图片URI 格式：
     *                  <br/> String imageUri = "http://site.com/image.png"; // from Web
     *                  <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                  <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                  <br/> String imageUri = "assets://image.png"; // from assets
     *                  <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     */
    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, OptionsType.IMAGE);
    }

    /**
     * 加载图片
     *
     * @param imageView ImageView
     * @param uri       图片URI 格式：
     *                  <br/> String imageUri = "http://site.com/image.png"; // from Web
     *                  <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                  <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                  <br/> String imageUri = "assets://image.png"; // from assets
     *                  <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param type      图片显示属性类型
     */
    public void displayImage(ImageView imageView, String uri, OptionsType type) {
        ImageLoader.getInstance().displayImage(uri, imageView, getImageOptions(type));
    }


    /**
     * 用户自定义加载图片
     * @param imageView ImageView
     * @param uri 图片URI 格式：
     *            <br/> String imageUri = "http://site.com/image.png"; // from Web
     *            <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *            <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *            <br/> String imageUri = "assets://image.png"; // from assets
     *            <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param type 图片显示属性类型
     */
    /**
     * @param imageView ImageView
     * @param uri       图片URI
     * @param loadingId 下载中的drawable id
     * @param defaultId 下载失败的drawable id
     */
    public void displayImage(ImageView imageView, String uri, int loadingId, int defaultId) {
        ImageLoader.getInstance().displayImage(uri, imageView, getCustomOptions(loadingId, defaultId));
    }

    /**
     * 加载图片，占位图默认为 OptionsType.IMAGE
     *
     * @param imageView        ImageView
     * @param uri              图片URI 格式：
     *                         <br/> String imageUri = "http://site.com/image.png"; // from Web
     *                         <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                         <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                         <br/> String imageUri = "assets://image.png"; // from assets
     *                         <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param progressListener 加载进度监听器
     */
    public void displayImage(ImageView imageView, String uri, ImageLoadingProgressListener progressListener) {
        displayImage(imageView, uri, OptionsType.IMAGE, progressListener);
    }

    /**
     * 加载图片
     *
     * @param imageView        ImageView
     * @param uri              图片URI 格式：
     *                         <br/> String imageUri = "http://site.com/image.png"; // from Web
     *                         <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                         <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                         <br/> String imageUri = "assets://image.png"; // from assets
     *                         <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param type             图片显示属性类型
     * @param progressListener 加载进度监听器
     */
    public void displayImage(ImageView imageView, String uri, OptionsType type, ImageLoadingProgressListener progressListener) {
        displayImage(imageView, uri, type, null, progressListener);
    }

    /**
     * 加载图片
     *
     * @param imageView            ImageView
     * @param uri                  图片URI 格式：
     *                             <br/> String imageUri = "http://site.com/image.png"; // from Web
     *                             <br/> String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                             <br/> String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                             <br/> String imageUri = "assets://image.png"; // from assets
     *                             <br/> String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param type                 图片显示属性类型
     * @param imageLoadingListener 加载事件监听器
     * @param progressListener     加载进度监听器
     */
    public void displayImage(ImageView imageView, String uri, OptionsType type, ImageLoadingListener imageLoadingListener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, getImageOptions(type), imageLoadingListener, progressListener);
    }

    /**
     * 根据类型获取图片显示属性
     *
     * @param type 图片显示属性类型
     * @return 显示属性
     */
    private DisplayImageOptions getImageOptions(OptionsType type) {
        if (OptionsType.IMAGE == type) {
            return getImageOptions();
        }

        if (OptionsType.AVATAR == type) {
            return getAvatarOptions();
        }

        if (OptionsType.COMMON == type) {
            return getCommonOptions();
        }

        if (OptionsType.AVATAR_CIRCLE == type) {
            return getCircleAvatarOptions();
        }
        return getImageOptions();
    }

    /**
     * 获取普通图片显示属性
     *
     * @return 显示属性
     */
    private DisplayImageOptions getImageOptions() {
        if (imageOptions == null) {
            imageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.imageselector_default_img) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.imageselector_default_img) //设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.imageselector_default_img) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                    .delayBeforeLoading(100)// 延迟加载
                    .displayer(new FadeInBitmapDisplayer(100))// 淡入
                    .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
                    .build(); //构建完成
        }
        return imageOptions;
    }

    /**
     * 获取普通图片显示属性
     *
     * @param loadingId 下载中的drawable id
     * @param defaultId 下载失败的drawable id
     * @return 显示属性
     */
    private DisplayImageOptions getCustomOptions(int loadingId, int defaultId) {

        DisplayImageOptions customImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingId) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(defaultId) //设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultId) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
                .build();
        return customImageOptions;
    }

    /**
     * 获取头像显示属性（全圆角）
     *
     * @return 显示属性
     */
    private DisplayImageOptions getAvatarOptions() {
        if (avatarOptions == null) {
            avatarOptions = new DisplayImageOptions.Builder()
                    //TODO 根据需求修改对应属性
                    .showImageOnLoading(R.drawable.imageselector_default_avatar) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.imageselector_default_avatar) //设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.imageselector_default_avatar) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                    .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
                    .displayer(new FadeInBitmapDisplayer(100)) //是否图片加载好后渐入的动画时间，注意：这个参数只能设置一次，否则会覆盖
                    .build(); //构建完成
        }
        return avatarOptions;
    }

    /**
     * 获取头像显示属性（全圆角）
     *
     * @return 显示属性
     */
    private DisplayImageOptions getCircleAvatarOptions() {
        if (avatarOptions == null) {
            avatarOptions = new DisplayImageOptions.Builder()
                    //TODO 根据需求修改对应属性
                    .showImageOnLoading(R.drawable.imageselector_default_avatar) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.imageselector_default_avatar) //设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.imageselector_default_avatar) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//                  .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions) //设置图片的解码配置
//                  .delayBeforeLoading(int delayInMillis) //int delayInMillis为你设置的下载前的延迟时间
//                  .preProcessor(BitmapProcessor preProcessor) //设置图片加入缓存前，对bitmap进行设置
                    .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
                    .displayer(new RoundedBitmapDisplayer(360)) //是否设置为圆角，弧度为多少，注意：这个参数只能设置一次，否则会覆盖
                    .build(); //构建完成
        }
        return avatarOptions;
    }

    /**
     * 获取通用显示属性，不设置默认图片
     *
     * @return 显示属性
     */
    private DisplayImageOptions getCommonOptions() {
        if (commonOptions == null) {
            commonOptions = new DisplayImageOptions.Builder()
                    //TODO 根据需求修改对应属性
                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//                  .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions) //设置图片的解码配置
//                  .delayBeforeLoading(int delayInMillis) //int delayInMillis为你设置的下载前的延迟时间
//                  .preProcessor(BitmapProcessor preProcessor) //设置图片加入缓存前，对bitmap进行设置
                    .resetViewBeforeLoading(true) //设置图片在下载前是否重置，复位
//                  .displayer(new RoundedBitmapDisplayer(20)) //是否设置为圆角，弧度为多少
//                  .displayer(new FadeInBitmapDisplayer(100)) //是否图片加载好后渐入的动画时间
                    .build(); //构建完成
        }
        return commonOptions;
    }
}
