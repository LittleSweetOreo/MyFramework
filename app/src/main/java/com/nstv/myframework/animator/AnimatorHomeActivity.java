package com.nstv.myframework.animator;

import android.view.View;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;

public class AnimatorHomeActivity extends BaseActivity {

	@Override
	protected int bindLayout() {
		return R.layout.activity_animator_home;
	}

	@Override
	protected void setListener() {
		super.setListener();
		getView(R.id.btn_to_primary_animator_activity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(AnimatorPrimaryActivity.class);
			}
		});

		getView(R.id.btn_to_type_evaluator_animator_activity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(AnimatorTypeEvaluatorActivity.class);
			}
		});

	}
}
