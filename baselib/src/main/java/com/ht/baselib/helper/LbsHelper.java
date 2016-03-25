package com.ht.baselib.helper;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * <p>
 * 定位信息帮助类
 * <br/>提供经纬
 * </p>
 *
 * @author wenwei.chen
 * @version 1.0 (2015/10/28)
 */
public class LbsHelper implements AMapLocationListener{
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 监听回调
     */
    public OnLbsListener onLbsListener;
    /**
     * 时间间隔
     */
    private static double timeGap = 15 * 1000;
    private long statTime;

    /**
     * 实体类初始化
     *
     * @param context       上下文
     * @param onLbsListener 回调
     */
    public LbsHelper(Context context, OnLbsListener onLbsListener) {
        this.onLbsListener = onLbsListener;
        statTime = System.currentTimeMillis();

        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);
        locationClient.setLocationListener(this);

        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }



    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation != null && aMapLocation.getErrorCode() == 0){
            setLongitude(aMapLocation.getLongitude());
            setLatitude(aMapLocation.getLatitude());
            onLbsListener.onLocationSuccess();
            if (null != locationClient) {
                locationClient.onDestroy();
                locationClient = null;
                locationOption = null;
            }
        }
        if (System.currentTimeMillis() - statTime > timeGap) {
            setLongitude(0);
            setLatitude(0);
            onLbsListener.onLocationSuccess();
            if (null != locationClient) {
                locationClient.onDestroy();
                locationClient = null;
                locationOption = null;
            }
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 回调
     */
    public interface OnLbsListener {
        /**
         * 回调成功
         */
        void onLocationSuccess();
    }
}
