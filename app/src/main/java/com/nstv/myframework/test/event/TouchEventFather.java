package com.nstv.myframework.test.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.nstv.myframework.util.Logger;

/**
 * @author liyong
 * @date 2017/11/14 09:54
 * @desc 事件分发测试ViewGroup
 */

public class TouchEventFather extends LinearLayout {

	public TouchEventFather(Context context) {
		super(context);
	}

	public TouchEventFather(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Logger.e("TouchEventFather", "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Logger.i("TouchEventFather", "onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Logger.d("TouchEventFather", "onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onTouchEvent(ev);
	}
}
