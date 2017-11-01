package com.nstv.myframework.mtldesign;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;
import com.nstv.myframework.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class PageColorGradualActivity extends BaseActivity {

	private ViewPager mViewPager;
	private FragmentStatePagerAdapter mAdapter;
	private static final int PAGE1_IMG = R.drawable.page0;
	private static final int PAGE2_IMG = R.drawable.page1;
	private static final int PAGE3_IMG = R.drawable.page2;
	private static final int PAGE4_IMG = R.drawable.page3;
	public static final String TAG = "PageColorGradualActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int bindLayout() {
		return R.layout.activity_page_color_gradual;
	}

	@Override
	protected void initView() {
		mViewPager = (ViewPager) getView(R.id.viewpager);
	}

	@Override
	protected void initData() {
		List<Fragment> list = new ArrayList<>();
		list.add(PageColorFragment.newInstance(PAGE1_IMG));
		list.add(PageColorFragment.newInstance(PAGE2_IMG));
		list.add(PageColorFragment.newInstance(PAGE3_IMG));
		list.add(PageColorFragment.newInstance(PAGE4_IMG));

		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), list);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	protected void setListener() {
		if (mViewPager != null) {
			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				/**
				 * ViewPager滑动回调方法
				 * @param position 页签[0 ~ 3]
				 * @param positionOffset 页百分比偏移[0F ~ 1F]
				 * @param positionOffsetPixels 页像素偏移[0 ~ ViewPager的宽度]
				 */
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

					Logger.i(TAG, "position=" + position + ",positionOffset=" + positionOffset +
							",positionOffsetPixels=" + positionOffsetPixels);

					ArgbEvaluator evaluator = new ArgbEvaluator();//ARGB求值器
					int gradualColor = 0X00FFFFFF;//默认初始颜色（透明白）
					if (position == 0) {
						gradualColor = (int) evaluator.evaluate(positionOffset, 0XFFFF8080, 0XFFFFBC00);
					} else if (position == 1) {
						gradualColor = (int) evaluator.evaluate(positionOffset, 0XFFFFBC00, 0XFF199AFE);
					} else if (position == 2) {
						gradualColor = (int) evaluator.evaluate(positionOffset, 0XFF199AFE, 0XFF00AB96);
					} else {
						gradualColor = 0XFF00AB96;
					}
					mViewPager.setBackgroundColor(gradualColor);
				}

				@Override
				public void onPageSelected(int position) {

				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});
		}
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {
		List<Fragment> fragmentList;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			fragmentList = list;
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList == null || fragmentList.isEmpty() ? null : fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList == null || fragmentList.isEmpty() ? 0 : fragmentList.size();
		}
	}
}
