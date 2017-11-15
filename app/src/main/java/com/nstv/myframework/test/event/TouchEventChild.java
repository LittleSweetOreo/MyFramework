package com.nstv.myframework.test.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.nstv.myframework.util.Logger;

/**
 * @author liyong
 * @date 2017/11/14 10:00
 * @desc 事件分发测试ViewGroup
 */

public class TouchEventChild extends LinearLayout {

	public TouchEventChild(Context context) {
		super(context);
	}

	public TouchEventChild(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Logger.e("TouchEventChild", "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Logger.i("TouchEventChild", "onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Logger.d("TouchEventChild", "onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return false;
	}

}
