package com.nstv.myframework.test.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nstv.myframework.util.Logger;

/**
 * @author liyong
 * @date 2017/11/14 11:41
 * @desc 事件分发测试View
 */

public class TouchEventView extends TextView {
	public TouchEventView(Context context) {
		super(context);
	}

	public TouchEventView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchEventView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Logger.e("TouchEventView", "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Logger.d("TouchEventView", "onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onTouchEvent(ev);
	}
}
