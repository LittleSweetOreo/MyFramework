package com.nstv.myframework.test.event;

import android.view.MotionEvent;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;
import com.nstv.myframework.util.Logger;

public class TouchEventActivity extends BaseActivity {


	@Override
	protected int bindLayout() {
		return R.layout.activity_touch_event;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Logger.w("TouchEventActivity", "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Logger.w("TouchEventActivity", "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
		return super.onTouchEvent(event);
	}
}
