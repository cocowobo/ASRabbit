package com.ht.baselib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>图片操作帮助类</p>
 *
 * @author from internet
 * @version 1.0 (2015/10/19)
 */
public class ImageUtils {
    private static final String LOG_TAG = "ImageUtils";

    // 实用方法-已应用
    //============================================
    /**
     * @description 计算图片的压缩比率
     *
     * @param options 参数
     * @param reqWidth 目标的宽度
     * @param reqHeight 目标的高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 获取图片角度
     * @param imagePath 图片路径
     * @return
     */
    public static int readPictureDegree(String imagePath) {
        int i = 0;
        try {
            ExifInterface localExifInterface = new ExifInterface(imagePath);
            int j = localExifInterface.getAttributeInt("Orientation", 1);
            switch (j) {
                case 6:
                    i = 90;
                    break;
                case 3:
                    i = 180;
                    break;
                case 8:
                    i = 270;
                case 4:
                case 5:
                case 7:
                    default:
                        break;
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return i;
    }

    /**
     * 旋转图片
     * @param paramInt 旋转角度
     * @param paramBitmap 旋转的图片实体
     * @return
     */
    public static Bitmap rotaingImageView(int paramInt, Bitmap paramBitmap) {
        Matrix localMatrix = new Matrix();
        localMatrix.postRotate(paramInt);
        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
    }

    /**
     * 加载本地图片，并压缩
     *
     * @param imagePath 图片路径
     * @param outWidth 压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Bitmap decodeScaleImage(String imagePath, int outWidth, int outHeight) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, localOptions);
        int i = calculateInSampleSize(localOptions, outWidth, outHeight);
        localOptions.inSampleSize = i;
        localOptions.inJustDecodeBounds = false;
        Bitmap localBitmap1 = BitmapFactory.decodeFile(imagePath, localOptions);
        int j = readPictureDegree(imagePath);
        Bitmap localBitmap2 = null;
        if ((localBitmap1 != null) && (j != 0)) {
            localBitmap2 = rotaingImageView(j, localBitmap1);
            localBitmap1.recycle();
            localBitmap1 = null;
            return localBitmap2;
        }
        return localBitmap1;
    }

    /**
     * 加载本地图片，并压缩-结合RxJava实现异步化
     *
     * @param imagePath 本地图片绝对路径
     * @param outWidth  压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
//    public static Observable<Bitmap> rxDecodeScaleImage(String imagePath, int outWidth, int outHeight) {
//        return Observable.defer(() -> Observable.just(decodeScaleImage(imagePath, outWidth, outHeight)));
//    }

    /**
     * 根据图片远程链接获取bitmap图像
     * @param imageUrl 图片绝对地址（网络地址）
     * @param outWidth 压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Bitmap getbitmapWithUrl(String imageUrl, int outWidth, int outHeight) {
        if (StringUtils.isBlank(imageUrl)){
            return null;
        }else {
            // 显示网络上的图片
            Bitmap bitmap = null;
            URL myFileUrl;
            try {
                myFileUrl = new URL(imageUrl.trim());
                HttpURLConnection conn;
                conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                //压缩处理
                bitmap = compressFixBitmap(bitmap, outWidth, outHeight);
            } catch (IOException e) {
                //e.printStackTrace();
                LogUtils.e(LOG_TAG,"getbitmapWithUrl:" + e.getMessage());
            }
            return bitmap;
        }
    }

    /**
     * 根据图片远程链接获取bitmap图像并压缩
     * <br/>固定压缩为480*480
     * @param imageUrl 图片绝对地址（网络地址）
     *
     * @return
     */
    public static Bitmap getbitmapWithUrl(String imageUrl) {
        return getbitmapWithUrl(imageUrl, 480, 480);
    }

    /**
     * 加载网络图片并压缩，并压缩-结合RxJava实现异步化
     * <br/>固定压缩为480*480
     * @param imageUrl 图片绝对地址（网络地址）
     * @return
     */
//    public static Observable<Bitmap> rxGetbitmapWithUrl(String imageUrl) {
//        return Observable.defer(() -> Observable.just(getbitmapWithUrl(imageUrl)));
//    }

    /**
     * 获取圆角图片
     * @param bitmap 图片实体
     * @param roundPx 角度
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    /**
     * 加载网络图片，并圆角化处理
     *
     * @param imageUrl 图片网络链接
     * @param roundPx 角度
     * @return
     */
    public static Bitmap getRoundedBitmapWithUrl(String imageUrl, float roundPx) {
        Bitmap bitmap = getbitmapWithUrl(imageUrl);
        return getRoundedCornerBitmap(bitmap, roundPx);
    }

    /**
     * 加载网络图片，并圆角化处理-结合RxJava实现异步化
     *
     * @param imageUrl 图片网络链接
     * @param roundPx 角度
     * @return
     */
//    public static Observable<Bitmap> rxGetRoundedBitmapWithUrl(String imageUrl, float roundPx) {
//        return Observable.defer(() -> Observable.just(getRoundedBitmapWithUrl(imageUrl, roundPx)));
//    }

    /**
     * 解析uri的数据流为bitmap
     *
     * @param mContext 上下文
     * @param uri 图片uri地址
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context mContext, Uri uri) {
        Bitmap bitmap;
        try {
            /**
             * 先通过getContentResolver方法获得一个ContentResolver实例，
             * 调用openInputStream(Uri)方法获得uri关联的数据流stream
             * 把上一步获得的数据流解析成为bitmap
             */
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 图片转储为文件
     * @param bitmap 图像
     * @param imageFile 输出的文件路径
     */
    public static boolean saveBitmapTofile(Bitmap bitmap, File imageFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            boolean isOK = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return isOK;
        } catch (Exception e) {
            LogUtils.e(LOG_TAG,"Error writing bitmap"+e.getLocalizedMessage());
            return false;
        }
    }
    // 待验证方法
    //===========================================
    /**
     * 比例压缩
     * @param bitmapImage 被压缩图片实体
     * @param outWidth 压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmapImage, int outWidth, int outHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;

        newOpts.inSampleSize = calculateInSampleSize(newOpts,outWidth,outHeight);//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     * @param image 被压缩的图片
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while ( baos.toByteArray().length / 1024>100) {
            options -= 10;//每次都减少10
            if (options>0) {
                //重置baos即清空baos
                baos.reset();
                //这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 固定大小压缩
     * @param bitMap 被压缩的图片
     * @param outWidth 压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Bitmap compressFixBitmap(Bitmap bitMap, int outWidth, int outHeight) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) outWidth) / width;
        float scaleHeight = ((float) outHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }
}
