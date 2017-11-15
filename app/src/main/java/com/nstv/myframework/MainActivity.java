package com.nstv.myframework;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nstv.myframework.animator.AnimatorHomeActivity;
import com.nstv.myframework.customview.PieChartActivity;
import com.nstv.myframework.mtldesign.ScrollingActivity;
import com.nstv.myframework.mtldesign.PageColorGradualActivity;
import com.nstv.myframework.test.event.TouchEventActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

	private ListView mListView;
	private MainAdapter mAdapter;
	private List<String> mList = new ArrayList<>();

	@Override
	protected int bindLayout() {
		return R.layout.activity_main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		mListView = (ListView) getView(R.id.listview);
	}

	@Override
	protected void initData() {
		mList.add("属性动画");
		mList.add("折叠标题栏效果1");
		mList.add("折叠标题栏效果2");
		mList.add("ViewPager滑动背景色渐变");
		mList.add("事件分发");
		mList.add("饼状图");
		mAdapter = new MainAdapter(this, mList);
		mListView.setAdapter(mAdapter);

	}

	@Override
	protected void setListener() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						openActivity(AnimatorHomeActivity.class);
						break;
					case 1:
						Bundle bundle1 = new Bundle();
						bundle1.putInt(ScrollingActivity.TITLE_MODE, ScrollingActivity.TitleCollapseMode.TITLE_PENDING);
						openActivity(ScrollingActivity.class, bundle1);
						break;
					case 2:
						Bundle bundle2 = new Bundle();
						bundle2.putInt(ScrollingActivity.TITLE_MODE, ScrollingActivity.TitleCollapseMode.TITLE_NONE);
						openActivity(ScrollingActivity.class, bundle2);
						break;
					case 3:
						openActivity(PageColorGradualActivity.class);
						break;
					case 4:
						openActivity(TouchEventActivity.class);
						break;
					case 5:
						openActivity(PieChartActivity.class);
						break;
					default:
						break;
				}
			}
		});
	}

}
