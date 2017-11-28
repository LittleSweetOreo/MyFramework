package com.nstv.myframework.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by aiwz3 on 2017/3/8.
 */
public class LocationUtil {
    private static LocationUtil mLocationUtil;
    private OnLocateListener mOnlocateListener;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public static LocationUtil newInstance() {
        if (mLocationUtil == null)
            mLocationUtil = new LocationUtil();
        return mLocationUtil;
    }

    public void initialize(Context context) {
        //声明LocationClient类
        mLocationClient = new LocationClient(context);
        //配置定位参数
        initLocOption();
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
    }

    public void start() {
        mLocationClient.start();
    }

    private void initLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

//        int span=1000;
//        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int resultCode = -1;
            //获取定位结果
            StringBuffer sb = new StringBuffer();
            int locType = location.getLocType();
            String strProvince = null;
            String strCity = null;
            if (locType == BDLocation.TypeGpsLocation || locType == BDLocation.TypeNetWorkLocation || locType == BDLocation.TypeOffLineLocation) {
                resultCode = 0;
                strProvince = location.getProvince();
                strCity = location.getCity();

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                resultCode = -1;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                resultCode = -1;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                resultCode = -1;
            }
            //定位完成
            mLocationClient.stop();
            Log.i("BaiduLocation===", sb.toString());
            String placeInfo = sb.toString();
            if (placeInfo.contains("北京") || placeInfo.contains("天津") || placeInfo.contains("上海") || placeInfo.contains("重庆"))
                mOnlocateListener.locatComplete(resultCode, location.getCity());
            else
                mOnlocateListener.locatComplete(resultCode, sb.toString());
        }


    }

    public interface OnLocateListener {
        void locatComplete(int resultCode, String addStr);
    }

    public void setmOnlocateListener(OnLocateListener mOnlocateListener) {
        this.mOnlocateListener = mOnlocateListener;
    }

}
