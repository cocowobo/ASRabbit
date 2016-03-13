package com.ht.baselib.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.ht.baselib.utils.FileUtils;
import com.ht.baselib.utils.SDCardUtils;
import com.ht.baselib.views.imageselector.bean.Image;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * 压缩图片的帮助类
 * <br/>1.提供Bitmap质量压缩，2.提供Native压缩
 * </p>
 *
 * @author wenwei.chen
 * @version 1.0 (2015/12/6)
 */
public class CompressImageHelper {
    /**
     * Image List
     */
    public ArrayList<Image> mDataList = new ArrayList<Image>();
    /**
     * 缩略图宽
     */
    private static final int VIDEO_WIDTH = 200;
    /**
     * 缩略图高
     */
    private static final int VIDEO_HEIGHT = 200;
    private boolean isThumbnail = false;

    /**
     * 使用原生的bitmap质量压缩
     *
     * @param originPath 图片绝对地址，如：/sdcard/1/1.jpg
     * @return 保存压缩地址
     */
    public static String compressBase(String originPath) {
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        String savePath = SDCardUtils.IMAGE_FOLDER + "0.jpg";
        return compressBase(originPath, savePath);
    }

    /**
     * 使用原生的bitmap质量压缩
     *
     * @param originList 图片绝对地址列表
     * @return 保存压缩地址列表
     */
    public static ArrayList<String> compressBase(ArrayList<Image> originList) {
        ArrayList<String> saveList = new ArrayList<String>();
        int i = 0;
        String saveDir = SDCardUtils.IMAGE_FOLDER;
        FileUtils.createFolder(saveDir);
        for (Image image : originList) {
            i++;
            File originFile = new File(image.getUri().substring(7));
            if (originFile.length() > 0) {
                saveList.add(compressBase(image.getUri().substring(7), saveDir + i + ".jpg"));
            }
        }
        return saveList;
    }

    /**
     * 使用原生的bitmap质量压缩
     *
     * @param originPath 图片绝对地址，如：/sdcard/1/1.jpg
     * @param savePath   保存压缩地址
     * @return 保存压缩地址
     */
    public static String compressBase(String originPath, String savePath) {
        int quality = 100;
        try {
            Bitmap bit = BitmapFactory.decodeFile(originPath);
            if (bit.getByteCount() / 1024 > (4 * 1024)) {
                quality = 40;
            } else if (bit.getByteCount() / 1024 > (3 * 1024)) {
                quality = 60;
            } else if (bit.getByteCount() / 1024 > (2 * 1024)) {
                quality = 70;
            } else if (bit.getByteCount() / 1024 > (1024)) {
                quality = 80;
            } else if (bit.getByteCount() / 1024 > (1024 / 2)) {
                quality = 95;
            }
            FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
            File originalFile = new File(savePath);
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(originalFile);
            bit.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savePath;
    }

    /**
     * @param bit        bitmap
     * @param originPath 图片绝对地址
     * @param savePath   保存压缩地址
     * @param quality    压缩比
     * @return 返回
     */
    private static String compressBase(Bitmap bit, String originPath, String savePath, int quality) {
        File saveFile = new File(savePath);
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(saveFile);
            bit.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            return savePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return originPath;
    }

    /**
     * 使用Native压缩
     *
     * @param originPath 图片绝对地址
     * @param savePath   保存压缩地址
     * @param quality    压缩质量
     * @return 保存压缩地址
     */
    public static String compressNative(String originPath, String savePath, int quality) {
        Bitmap bit = decodeFile(originPath);
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        File originFile = new File(originPath);
        long originLength = originFile.length() / 1024;
        if (originFile.getName().startsWith("screenshot") || originFile.getName().startsWith("Screenshot")) {
            return compressBase(bit, originPath, savePath, 70);
        }
        try {
            if (bit.getByteCount() < 200000) {
                return compressBase(bit, originPath, savePath, 90);
            }
        } catch (NullPointerException e) {
            return savePath;
        }
        if (originLength < (1024 / 2 / 2 / 2)) {
            return compressBase(bit, originPath, savePath, 90);
        } else if (originLength < (1024 / 2 / 2)) {
            return compressBase(bit, originPath, savePath, 80);
        } else if (originLength < (1024 / 2)) {
            return compressBase(bit, originPath, savePath, 70);
        } else if (originLength < 1024) {
            return compressBase(bit, originPath, savePath, 60);
        } else if (originLength < (1024 * 1.4)) {
            return compressBase(bit, originPath, savePath, 45);
        }
        File saveFile = new File(savePath);
        NativeUtil.compressBitmap(bit, quality,
                saveFile.getAbsolutePath(), true);
        bit.recycle();
        return savePath;
    }

    private static Bitmap compressBase(Bitmap bit, String savePath, int quality) {
        File saveFile = new File(savePath);
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(saveFile);
            bit.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bit;
    }

    /**
     * 返回压缩的bitmap
     *
     * @param originPath 绝对地址列表
     * @return bitmap
     */
    public static Bitmap compressBitmap(String originPath) {
        return compressBitmap(originPath, 60);
    }

    /**
     * 返回压缩的bitmap
     *
     * @param originPath 绝对地址列表
     * @param quality    质量
     * @return bitmap
     */
    public static Bitmap compressBitmap(String originPath, int quality) {
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);

        String savePath = SDCardUtils.IMAGE_FOLDER + System.currentTimeMillis() + "share.jpg";
        return compressBitmap(originPath, savePath, quality);
    }

    /**
     * 返回压缩的bitmap
     *
     * @param originPath 绝对地址列表
     * @param savePath   保存的地址
     * @param quality    质量
     * @return bitmap
     */
    public static Bitmap compressBitmap(String originPath, String savePath, int quality) {
        Bitmap bit = decodeFile(originPath);
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        File originFile = new File(originPath);
        long originLength = originFile.length() / 1024;
        if (originLength == 0) {
            return Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        }
        if (originFile.getName().startsWith("screenshot") || originFile.getName().startsWith("Screenshot")) {
            return compressBase(bit, savePath, 70);
        }
        try {
            if (bit.getByteCount() < 200000) {
                return compressBase(bit, savePath, 90);
            }
        }catch (NullPointerException e) {
            return bit;
        }
        if (originLength < (1024 / 2 / 2 / 2)) {
            return compressBase(bit, savePath, 90);
        } else if (originLength < (1024 / 2 / 2)) {
            return compressBase(bit, savePath, 80);
        } else if (originLength < (1024 / 2)) {
            return compressBase(bit, savePath, 70);
        } else if (originLength < 1024) {
            return compressBase(bit, savePath, 60);
        } else if (originLength < (1024 * 1.4)) {
            return compressBase(bit, savePath, 45);
        }
        File saveFile = new File(savePath);
        NativeUtil.compressBitmap(bit, quality,
                saveFile.getAbsolutePath(), true);
        bit.recycle();
        return bit;
    }


    /**
     * 使用Native压缩
     *
     * @param originList 图片绝对地址列表
     * @param quality    压缩质量
     * @return 保存压缩地址列表
     */
    public static ArrayList<String> compressNative(ArrayList<Image> originList, int quality) {
        ArrayList<String> saveList = new ArrayList<String>();
        int i = 0;
        String saveDir = SDCardUtils.IMAGE_FOLDER;
        FileUtils.createFolder(saveDir);
        for (Image image : originList) {
            i++;
            File originFile = new File(image.getUri().substring(7));
            if (originFile.length() > 0) {
                saveList.add(compressNative(image.getUri().substring(7), saveDir + i + ".jpg", quality));
            }
        }
        return saveList;
    }

    /**
     * 使用Native压缩
     *
     * @param originPath 图片绝对地址
     * @return 保存压缩地址
     */
    public static String compressNative(String originPath) {
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        String savePath = SDCardUtils.IMAGE_FOLDER + "0.jpg";
        return compressNative(originPath, savePath);
    }

    /**
     * 使用Native压缩
     *
     * @param originPath 图片绝对地址，如：/sdcard/1/1.jpg
     * @param savePath   保存压缩地址
     * @return 保存压缩地址
     */
    public static String compressNative(String originPath, String savePath) {
        int quality = 70;
        return compressNative(originPath, savePath, quality);
    }

    /**
     * 使用Native压缩
     *
     * @param originList 图片绝对地址列表
     * @return 保存压缩地址列表
     */
    public static ArrayList<String> compressNative(ArrayList<Image> originList) {
        return compressNative(originList, 70);
    }

    /**
     * decode
     *
     * @param pathName path
     * @return bitmap
     */
    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
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

        if (bitmap == null) {
            bitmap = getImageThumbnail(pathName);
        }
        return bitmap;
    }

    /**
     * 缩略图
     *
     * @param imagePath path
     * @return bitmap
     */
    private static Bitmap getImageThumbnail(String imagePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  //不申请内存 计算图片比例

        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; //设为 false  申请内存
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / VIDEO_WIDTH;
        int beHeight = h / VIDEO_HEIGHT;
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
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, VIDEO_WIDTH, VIDEO_HEIGHT,
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
     * 获取缩略图
     *
     * @param imagePath 地址
     * @return 缩略图
     */
    public static Bitmap getThumbnail(String imagePath) {
        return getThumbnail(imagePath, 150);
    }

    /**
     * 获取缩略图
     *
     * @param imagePath 地址
     * @param sampeSize 大小
     * @return 缩略图
     */
    public static Bitmap getThumbnail(String imagePath, int sampeSize) {
        Bitmap bit = CompressImageHelper.decodeFile(imagePath);
        int w = bit.getWidth();
        int h = bit.getHeight();
        if (w > h) {
            h = h * sampeSize / w;
            w = sampeSize;
        } else {
            w = w * sampeSize / h;
            h = sampeSize;
        }
        Bitmap bit2 = ThumbnailUtils.extractThumbnail(bit, w, h, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        FileUtils.createFolder(SDCardUtils.IMAGE_FOLDER);
        String savePath = SDCardUtils.IMAGE_FOLDER + System.currentTimeMillis() + "share.jpg";
        File saveFile = new File(savePath);
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(saveFile);
            bit2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodeFile(savePath);
    }
}
