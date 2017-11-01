package com.nstv.myframework.mtldesign;

import android.support.design.widget.AppBarLayout;

/**
 * @author liyong
 * @date 2017/10/16 16:21
 * @desc 监听AppBarLayout的滑动
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

	public enum State {
		EXPANDED,
		COLLAPSED,
		INTERNAL
	}

	private State mCurrentState = State.INTERNAL;

	@Override
	public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
		if (i == 0) {
			if (mCurrentState != State.EXPANDED) {
				onStateChanged(appBarLayout, State.EXPANDED);
			}
			mCurrentState = State.EXPANDED;
		} else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
			if (mCurrentState != State.COLLAPSED) {
				onStateChanged(appBarLayout, State.COLLAPSED);
			}
			mCurrentState = State.COLLAPSED;
		} else {
			if (mCurrentState != State.INTERNAL) {
				onStateChanged(appBarLayout, State.INTERNAL);
			}
			mCurrentState = State.INTERNAL;
		}
	}

	public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}