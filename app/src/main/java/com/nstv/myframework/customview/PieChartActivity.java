package com.nstv.myframework.customview;

import android.widget.Toast;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;
import com.nstv.myframework.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends BaseActivity {
    private static final String TAG = "PieChartActivity";

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
        final List<PieData> pieDatas = new ArrayList<>();
        pieDatas.add(new PieData("学习", 6600));
        pieDatas.add(new PieData("餐饮", 26628));
        pieDatas.add(new PieData("服装", 5800));
        pieDatas.add(new PieData("数码", 13600));

        mPieView.setData(pieDatas);
        mPieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            @Override
            public void onArcClick(int position) {
                Logger.w(TAG, "click position = " + position);

                if (position == -1) {
                    return;
                }
                Toast.makeText(PieChartActivity.this, pieDatas.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
