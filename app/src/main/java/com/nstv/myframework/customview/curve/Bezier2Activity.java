package com.nstv.myframework.customview.curve;

import android.support.annotation.IdRes;
import android.widget.RadioGroup;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;

public class Bezier2Activity extends BaseActivity {

	private CubicView mCubicView;

	@Override
	protected int bindLayout() {
		return R.layout.activity_bezier2;
	}

	@Override
	protected void initView() {
		mCubicView = getView(R.id.cubicView);
	}

	@Override
	protected void setListener() {
		((RadioGroup) getView(R.id.rg_control)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch (checkedId) {
					case R.id.rb_1:
						mCubicView.setControlMode(CubicView.ControlMode.CONTROL_MODE_1);
						break;
					case R.id.rb_2:
						mCubicView.setControlMode(CubicView.ControlMode.CONTROL_MODE_2);
						break;
				}
			}
		});
	}
}
