package com.nstv.myframework.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by safari on 2017/5/3.
 */
public class Logger {
	public static final int LOG_LEVEL_VERBOSE = Log.VERBOSE;    //Log.VERBOSE = 2;
	public static final int LOG_LEVEL_DEBUG = Log.DEBUG;        //Log.DEBUG = 3;
	public static final int LOG_LEVEL_INFO = Log.INFO;          //Log.INFO = 4;
	public static final int LOG_LEVEL_WARN = Log.WARN;          //Log.WARN = 5;
	public static final int LOG_LEVEL_ERROR = Log.ERROR;        //Log.ERROR = 6;
	private static final String appName = "MyFramework";
	public static final String SPACE_CHAR = " ";

	/**
	 * Get The Current Function(函数) Name
	 *
	 * @return
	 */
	private static String getStackTraceInfo() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		if (stElements == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement st : stElements) {

			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(Logger.class.getName())) {
				continue;
			}
			sb.append("\n" + "at" + SPACE_CHAR + st.getClassName() + "." + st.getMethodName() + "(" + st.getFileName() + SPACE_CHAR + "line num:" + st.getLineNumber() + ")");
		}
		return sb.toString();
	}

	/**
	 * get current process name
	 *
	 * @param context
	 * @return
	 */
	public static final String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * get current process PID
	 *
	 * @return
	 */
	public static final int getCurProcessPID() {
		int pid = android.os.Process.myPid();
		return pid;
	}

	public static void v(String tag, String msg) {
		if (LOG_LEVEL_VERBOSE <= Log.VERBOSE) {
			Log.v("[" + appName + "]", "[" + tag + "]" + msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL_DEBUG <= Log.DEBUG) {
			Log.d("[" + appName + "]", "[" + tag + "]" + msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL_INFO <= Log.INFO) {
			Log.i("[" + appName + "]", "[" + tag + "]" + msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL_WARN <= Log.WARN) {
			Log.w("[" + appName + "]", "[" + tag + "]" + msg);
		}
	}

	/**
	 * print common error info
	 *
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (LOG_LEVEL_ERROR <= Log.ERROR) {
			Log.e("[" + appName + "]", "[" + tag + "]" + msg);
		}
	}

	/**
	 * print fatal Exception info
	 *
	 * @param context
	 * @param exceptionMsg
	 */
	public static void e(Context context, String exceptionMsg) {
		if (LOG_LEVEL_ERROR <= Log.ERROR) {
			String name = getStackTraceInfo();
			Log.e("[" + appName + "]", "ERROR INFO:" + SPACE_CHAR + Thread.currentThread().getName() + "\n"
					+ "Process:" + SPACE_CHAR + getCurProcessName(context) + "," + SPACE_CHAR + "PID:" + SPACE_CHAR + getCurProcessPID() + "\n"
					+ exceptionMsg
					+ name);
		}
	}

}

