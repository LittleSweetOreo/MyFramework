package com.nstv.myframework.util;

import android.content.Context;

/**
 * @author liyong
 * @date 2017/9/19 16:45
 * @desc 屏幕密度计算转换工具类
 */

public class MetricUtil {

	/**
	 * 根据手机分辨率从DP转成PX
	 *
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率PX(像素)转成DP
	 *
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dp(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int statusBarHeight = -1;
		int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0)
			statusBarHeight = context.getResources().getDimensionPixelSize(resId);

		return statusBarHeight;
	}
}
