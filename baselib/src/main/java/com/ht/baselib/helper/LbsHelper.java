package com.ht.baselib.helper;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * <p>
 * 定位信息帮助类
 * <br/>提供经纬和省市区信息
 * </p>
 *
 * @author wenwei.chen
 * @version 1.0 (2015/10/28)
 */
public class LbsHelper {
    /**
     * 定位类
     */
    public LocationClient mLocationClient;
    /**
     * 监听类
     */
    public MyLocationListener mMyLocationListener;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 地区
     */
    private String district;
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
     * 飞行模式的返回
     */
    private static double airMode = 4.9E-324;
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
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);// 注册监听函数
        mLocationClient.setLocOption(initLocation());
        mLocationClient.start();
    }

    /**
     * 定位参数初始化
     */
    private LocationClientOption initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要

        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.disableCache(false);// 禁止启用缓存定位
        return option;
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLongitude() != airMode && location.getLatitude() != airMode
                    && location.getLongitude() != 0 && location.getLatitude() != 0) {
                setProvince(location.getProvince());
                setCity(location.getCity());
                setDistrict(location.getDistrict());
                setLongitude(location.getLongitude());
                setLatitude(location.getLatitude());
                onLbsListener.onLocationSuccess();
                mLocationClient.stop();
            }
            if (System.currentTimeMillis() - statTime > timeGap) {
                setLongitude(0);
                setLatitude(0);
                onLbsListener.onLocationSuccess();
                if (mLocationClient != null) {
                    mLocationClient.stop();
                }
            }
        }
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
