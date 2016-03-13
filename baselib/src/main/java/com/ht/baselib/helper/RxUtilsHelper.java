package com.ht.baselib.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ht.baselib.helper.cache.DiskLruCacheHelper;
import com.ht.baselib.helper.cache.LruCacheHelper;
import com.ht.baselib.utils.BlurUtil;
import com.ht.baselib.utils.ImageUtils;

import java.io.InputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * <p>
 *     异步化操作帮助类-项目业务相关
 *
 * <br/><h5>Rxjava 相关概念</h5>
 1）Observable用于表示一个可被消费的数据集合（data provider），它后面的数据的产生机制或者是同步的，或者是异步的，这都不重要的，最重要是它提供了下面的能力：
 Observer可以通过Observable的subscribe向其注册。
 当Observable中有数据产生时，调用Observer的onNext方法通知有新数据到来。
 当Observable数据发送完毕时，调用Observer的onComplete方法通知数据发送完毕。
 当Observable内部出现错误时，调用Observer的onError方法通知有错误需要处理。
 2）通过subscribeOn()指定订阅者运行的线程，observerOn()指定被订阅者运行的线程，也即
 subscribeOn()用来决定你的observer代码在哪个线程运行，observeOn()用来决定你的Subscriber会在哪个线程运行，
 简而言之，请求操作在subscribeOn内定义在哪条线程执行，当结果来了之后会根据observeOn内的设置来决定subscribe内的操作会在哪条线程上执行)。
 3）可能会用到Scheduler：
 Schedulers.computation()：用于计算型工作例如事件循环和回调处理，不要在I/O中使用这个函数（应该使用Schedulers.io()函数）；
 Schedulers.from(executor)：使用指定的Executor作为Scheduler；
 Schedulers.immediate()：在当前线程中立即开始执行任务；
 Schedulers.io()：用于I/O密集型工作例如阻塞I/O的异步操作，这个调度器由一个会随需增长的线程池支持；对于一般的计算工作，使用Schedulers.computation()；
 Schedulers.newThread()：为每个工作单元创建一个新的线程；
 Schedulers.test()：用于测试目的，支持单元测试的高级事件；
 Schedulers.trampoline()：在当前线程中的工作放入队列中排队，并依次操作。
 学习链接：http://blog.dreamtobe.cn/2289.html http://blog.csdn.net/asce1885/article/details/43868133

 <br/><h5>Lambda表达式 相关概念</h5>
 函数式接口(functional interface): 只包含一个抽象方法的接口(如Runnable只有run()这么一个方法)；
 学习链接：http://blog.dreamtobe.cn/2281.html http://www.infoq.com/cn/articles/Java-se-8-lambda
 Lambda表达式在两个方面进行了简化：首先是接口的声明（如Runnable），这可以通过对上下文环境进行推断来得出；
 其次是对接口方法（如run()）的实现，因为函数式接口中只包含一个需要实现的方法。
 Lambda表达式的声明方式比较简单，由形式参数和方法体两部分组成，中间通过“->”分隔。形式参数不需要包含类型声明，可以进行自动推断。
 当然在某些情况下，形式参数的类型声明是不可少的。方法体则可以是简单的表达式或代码块。
 *
 * </p>
 *
 * @author from internet
 * @version 1.0 (2015/10/22)
 */
public class RxUtilsHelper {
    private static final String LOG_TAG = RxUtilsHelper.class.getSimpleName();

    // 图片操作相关
    //======================================================

    /**
     * 加载本地图片，并压缩-结合RxJava实现异步化
     *
     * @param imagePath 本地图片绝对路径
     * @param outWidth  压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Observable<Bitmap> rxDecodeScaleImage(final String imagePath, final int outWidth, final int outHeight) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(ImageUtils.decodeScaleImage(imagePath, outWidth, outHeight));
            }
        });
    }

    /**
     * 加载网络图片并压缩，并压缩-结合RxJava实现异步化
     * <br/>固定压缩为480*480
     * @param imageUrl 图片绝对地址（网络地址）
     * @return
     */
    public static Observable<Bitmap> rxGetbitmapWithUrl(final String imageUrl) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(ImageUtils.getbitmapWithUrl(imageUrl));
            }
        });
    }

    /**
     * 加载网络图片，并圆角化处理-结合RxJava实现异步化
     *
     * @param imageUrl 图片网络链接
     * @param roundPx 角度
     * @return
     */
    public static Observable<Bitmap> rxGetRoundedBitmapWithUrl(final String imageUrl, final float roundPx) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(ImageUtils.getRoundedBitmapWithUrl(imageUrl, roundPx));
            }
        });
    }

    /**
     * 图片高斯模糊处理-结合RxJava实现异步化
     *
     * @param bmp 模糊对象
     * @return
     */
    public static Observable<Drawable> rxBoxBlurFilter(final Bitmap bmp) {
        return Observable.defer(new Func0<Observable<Drawable>>() {
            @Override
            public Observable<Drawable> call() {
                return Observable.just(BlurUtil.boxBlurFilter(bmp));
            }
        });
    }

    // 缓存操作相关
    //===========================================

    /**
     * 写入文件流缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param inputStream 文件流
     * @return
     */
    public static Observable<Boolean> rxDLruDumpBitmap(final String keyCache, final InputStream inputStream) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(DiskLruCacheHelper.dumpInputStream(keyCache, inputStream));
            }
        });
    }

    /**
     * 写入图片缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
    public static Observable<Boolean> rxDLruDumpBitmap(final String keyCache, final Bitmap bitmap) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(DiskLruCacheHelper.dumpBitmap(keyCache, bitmap));
            }
        });
    }

    /**
     * 读取Bitmap缓存-结合rxjava异步化
     * @param keyCache key 缓存key
     * @return
     */
    public static Observable<Bitmap> rxDLruLoadBitmap(final String keyCache) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(DiskLruCacheHelper.loadBitmap(keyCache));
            }
        });
    }

    /**
     * 读取InputStream缓存-结合rxjava异步化
     * @param keyCache key 缓存key
     * @return
     */
    public static Observable<InputStream> rxDLruLoadInputStream(final String keyCache) {
        return Observable.defer(new Func0<Observable<InputStream>>() {
            @Override
            public Observable<InputStream> call() {
                return Observable.just(DiskLruCacheHelper.loadInputStream(keyCache));
            }
        });
    }

    /**
     * 读取缓存文件-结合rxjava异步化
     * @param keyCache 缓存key(一般是本地绝对路径）
     * @return
     */
    public static Observable<String> rxDLruLoadFile(final String keyCache) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(DiskLruCacheHelper.loadFile(keyCache));
            }
        });
    }


    /**
     * 把图片写入内存缓存-结合rxjava异步化
     * @param keyCache 缓存key
     * @param bitmap 图片实体
     * @return
     */
    public static Observable<Boolean> rxLruDumpBitmap(final String keyCache, final Bitmap bitmap) {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(LruCacheHelper.dumpBitmap(keyCache, bitmap));
            }
        });
    }

    /**
     * 从内存缓存中读取图片数据-结合rxjava异步化
     * @param keyCache 缓存key
     * @return
     */
    public static Observable<Bitmap> rxLruLoadBitmap(final String keyCache) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(LruCacheHelper.loadBitmap(keyCache));
            }
        });
    }

    /**
     * RXANDROID处理，逻辑操作在子线程中处理，结果自动回调UI线程
     * @param func 逻辑操作方法对象
     * @param action1 成功回调方法
     * @param action2 失败回调方法
     * @param <T> 泛型
     */
    public static <T> void doByRxAndroid(Func0<Observable<T>> func, Action1<T> action1, Action1<Throwable> action2) {
        Observable.defer(func)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, action2);
    }

    /**
     * RXANDROID处理，逻辑操作和结果处理都在子线程中处理，上层不关心结果
     * @param func 逻辑操作方法对象
     * @param <T> 泛型
     */
    public static <T> void doByRxAndroid(Func0<Observable<T>> func) {
        Observable.defer(func)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T models) {
                        //do nothing
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {//do nothing
                    }
                });
    }
}
