package com.nstv.myframework.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by safari on 2017/5/3.
 */
public class PackageUtil {

	/**
	 * 获取应用主包名
	 *
	 * @param appContext
	 * @return
	 */
	public static String getPackageName(Context appContext) {
		return appContext.getPackageName();
	}

	/**
	 * @param appContext
	 * @return versionCode 用于判断应用升级，用户不关心
	 */
	public static int getVersionCode(Context appContext) {
		PackageManager pacManager = appContext.getPackageManager();
		PackageInfo pacInfo = null;
		try {
			pacInfo = pacManager.getPackageInfo(appContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pacInfo.versionCode;
	}

	/**
	 * @param appContext
	 * @return versionName 用于显示给用户看
	 */
	public static String getVersionName(Context appContext) {
		PackageManager pacManager = appContext.getPackageManager();
		PackageInfo pacInfo = null;
		try {
			pacInfo = pacManager.getPackageInfo(appContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pacInfo.versionName;
	}

}
