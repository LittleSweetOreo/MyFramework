package com.nstv.myframework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

	public static final String BUNDLE = "bundle";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(bindLayout());
		initView();
		initData();
		setListener();
	}

	protected abstract int bindLayout();

	protected void initView() {
	}

	protected void initData() {

	}

	protected void setListener() {

	}

	protected <T extends View> T getView(int resId) {
		return (T) findViewById(resId);
	}

	protected void openActivity(Class activityClz) {
		Intent intent = new Intent();
		intent.setClass(this, activityClz);
		startActivity(intent);
	}

	protected void openActivity(Class activityClz, Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this, activityClz);
		startActivity(intent);
	}
}
