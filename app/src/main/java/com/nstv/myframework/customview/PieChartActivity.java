package com.nstv.myframework.customview;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends BaseActivity {


	@Override
	protected int bindLayout() {
		return R.layout.activity_pie_chart;
	}

	private PieView mPieView;

	@Override
	protected void initView() {
		mPieView = (PieView) getView(R.id.pieview);
	}

	@Override
	protected void initData() {
		List<PieData> pieDatas = new ArrayList<>();
		pieDatas.add(new PieData("学习", 1200));
		pieDatas.add(new PieData("餐饮", 26628));
		pieDatas.add(new PieData("服装", 2000));
		pieDatas.add(new PieData("数码", 13600));

		mPieView.setData(pieDatas);

	}
}
