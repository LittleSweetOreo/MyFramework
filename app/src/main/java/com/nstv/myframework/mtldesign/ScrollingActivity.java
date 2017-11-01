package com.nstv.myframework.mtldesign;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends BaseActivity {

	public static final String TITLE_MODE = "title_mode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window window = getWindow();
		//设置透明状态栏,这样才能让 ContentView 向上
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		super.onCreate(savedInstanceState);
	}

	private int mTitleMode = TitleCollapseMode.TITLE_NONE;

	@Override
	protected int bindLayout() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mTitleMode = bundle.getInt(TITLE_MODE, TitleCollapseMode.TITLE_NONE);
		}

		if (mTitleMode == TitleCollapseMode.TITLE_PENDING) {

			return R.layout.activity_scrolling_pending;
		}
		return R.layout.activity_scrolling_collapse;
	}

	CollapsingToolbarLayout mCollapsingToolbarLayout;
	ViewPager mViewPager;
	TabLayout mTableLayout;

	@Override
	protected void initView() {
		Toolbar toolbar = (Toolbar) getView(R.id.toolbar);
		setSupportActionBar(toolbar);

		mCollapsingToolbarLayout = (CollapsingToolbarLayout) getView(R.id.toolbar_layout);
		mCollapsingToolbarLayout.setTitle(" ");
		mCollapsingToolbarLayout.setTitleEnabled(true);

		mTableLayout = (TabLayout) getView(R.id.tablayout);
		mViewPager = (ViewPager) getView(R.id.viewpager);

	}

	FragmentStatePagerAdapter mAdapter;

	@Override
	protected void initData() {
		List<String> titles = new ArrayList<>();
		titles.add("page1");
		titles.add("page2");
		titles.add("page3");
		titles.add("page4");
		List<Fragment> fragments = new ArrayList<>();
		fragments.add(PageFragment.newInstance("第一页"));
		fragments.add(PageFragment.newInstance("第二页"));
		fragments.add(PageFragment.newInstance("第三页"));
		fragments.add(PageFragment.newInstance("第四页"));
		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), titles, fragments);
		mViewPager.setAdapter(mAdapter);
		mTableLayout.setupWithViewPager(mViewPager);
	}

	@Override
	protected void setListener() {
		AppBarLayout appBarLayout = (AppBarLayout) getView(R.id.app_bar);
		appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (state == State.COLLAPSED) {

				} else if (state == State.EXPANDED) {

				} else {

				}
			}
		});
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {
		List<Fragment> fragmentList;
		List<String> titleList;

		public MyPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> list) {
			super(fm);
			titleList = titles;
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

		@Override
		public CharSequence getPageTitle(int position) {
			return titleList == null || titleList.isEmpty() ? "" : titleList.get(position);
		}
	}

	public static class TitleCollapseMode {
		public static final int TITLE_PENDING = 0;
		public static final int TITLE_NONE = 1;
	}
}

