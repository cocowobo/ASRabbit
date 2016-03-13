package com.ht.baselib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import net.bither.util.NativeUtil;

import java.io.File;

/**
 * <p>图片处理</p>
 *
 * @author wenwei.chen
 * @version 1.0 (2016-1-27)
 */
public class BitmapUtil {

    /**
     * 缩略图宽
     */
    private static final int THUMB_WIDTH = 200;
    /**
     * 缩略图高
     */
    private static final int THUMB_HEIGHT = 200;

    /**
     * 解压
     *
     * @param pathName 地址
     * @param width    长
     * @param height   宽
     * @return bitmap
     */
    public static Bitmap decodeFile(String pathName, int width, int height) {
        Bitmap bitmap = null;
        if (FileUtils.isFileExist(pathName)) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, opts);
//            opts.inSampleSize = computeSampleSize(opts, -1, width * height);
            opts.inSampleSize = ImageUtils.calculateInSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;

            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inPreferredConfig = android.graphics.Bitmap.Config.ALPHA_8;
            opts.inDither = true;
            try {
                bitmap = BitmapFactory.decodeFile(pathName, opts);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = BitmapFactory.decodeFile(pathName, opts);
                } catch (OutOfMemoryError e1) {
                    System.gc();
                    try {
                        bitmap = BitmapFactory.decodeFile(pathName, opts);
                    } catch (OutOfMemoryError e2) {
                        System.gc();
                    }
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = getImageThumbnail(pathName);
        }
        return bitmap;
    }

    /**
     * @param options   参数
     * @param reqWidth  目标的宽度
     * @param reqHeight 目标的高度
     * @return
     * @description 计算图片的压缩比率
     */

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
     * 缩略图
     *
     * @param imagePath path
     * @return bitmap
     */
    public static Bitmap getImageThumbnail(String imagePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  //不申请内存 计算图片比例

        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; //设为 false  申请内存
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / THUMB_WIDTH;
        int beHeight = h / THUMB_HEIGHT;
        int be = 4;
        if (beWidth < beHeight && beHeight >= 1) {
            be = beHeight;
        }
        if (beHeight < beWidth && beWidth >= 1) {
            be = beWidth;
        }
        if (be <= 0) {
            be = 1;
        } else if (be > 3) {
            be = 3;
        }
        options.inSampleSize = be;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, THUMB_WIDTH, THUMB_HEIGHT,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (OutOfMemoryError e) {
            System.gc();
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        }
        return bitmap;
    }

    /**
     * 根据大小压缩
     *
     * @param originPath 原始地址
     * @param imageSize  大小
     * @return bitmap
     */
    public static Bitmap compressBySize(String originPath, int imageSize) {
        return compressBySize(originPath, imageSize, true);
    }

    /**
     * 根据大小压缩
     *
     * @param originPath 原始地址
     * @param imageSize  大小
     * @param isDel      是否删除
     * @return bitmap
     */
    public static Bitmap compressBySize(String originPath, int imageSize, boolean isDel) {
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        String savePath = SDCardUtils.IMAGE_FOLDER + System.currentTimeMillis() + "n.jpg";
        Bitmap bit = decodeFile(originPath, 800, 800);
        int quality = 70;
        File nativeFile = new File(savePath);
        NativeUtil.compressBitmap(bit, quality, nativeFile.getAbsolutePath(), true);
        long originLength = nativeFile.length() / 1024;
        while (originLength > imageSize) {
            try {
                quality -= 8;//每次都减少8
                NativeUtil.compressBitmap(bit, quality, nativeFile.getAbsolutePath(), true);
                originLength = nativeFile.length() / 1024;
                LogUtils.e("quality:" + quality);
            } catch (OutOfMemoryError e2) {
                System.gc();
            }
        }
        bit.recycle();
        Bitmap compressBit = decodeFile(savePath, 800, 800);
        if (isDel) {
            FileUtils.deleteFile(savePath);
        }
        return compressBit;
    }

    /**
     * bitmap 压缩 根据大小压缩
     *
     * @param bit       bitmap
     * @param imageSize 大小
     * @return 返回bitmap
     */
    public static Bitmap compressBySize(Bitmap bit, int imageSize) {
        return compressBySize(bit,imageSize,2);
    }
    /**
     * bitmap 压缩 根据大小压缩
     *
     * @param bit       bitmap
     * @param imageSize 大小
     * @param scale 缩放比率
     * @return 返回bitmap
     */
    public static Bitmap compressBySize(Bitmap bit, int imageSize ,int scale) {
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        String savePath = SDCardUtils.IMAGE_FOLDER + System.currentTimeMillis() + "c.jpg";
        int quality = 95;
        File nativeFile = new File(savePath);
        NativeUtil.compressBitmap(bit, quality, nativeFile.getAbsolutePath(), true);
        long originLength = nativeFile.length() / 1024;
        while (originLength > imageSize) {
            try {
                quality -= 8;//每次都减少8
                NativeUtil.compressBitmap(bit, quality, nativeFile.getAbsolutePath(), true,scale);
                originLength = nativeFile.length() / 1024;
                LogUtils.e("quality:" + quality);
            } catch (OutOfMemoryError e2) {
                System.gc();
            }
        }
        bit.recycle();
        Bitmap compressBit = null;
        try {
           compressBit = BitmapFactory.decodeFile(savePath);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                compressBit = decodeFile(savePath, 800, 800);
            } catch (OutOfMemoryError e1) {

            }
        }
//        FileUtils.deleteFile(savePath);
        return compressBit;
    }
}
