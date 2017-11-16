package com.nstv.myframework.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.nstv.myframework.R;
import com.nstv.myframework.util.Logger;

import java.util.List;

/**
 * @author liyong
 * @date 2017/11/15 10:12
 * @desc 饼状图（提交测试）
 */

public class PieView extends View {
    private static final String TAG = "PieView";
    private int mActualWidth;  //实际的宽度
    private int mActualHeight; //实际的高度

    private Paint mArcPaint = new Paint();       //圆弧画笔
    private Paint mTextPaint = new Paint();       //文字画笔
    private float mStartAngle;  //绘制圆弧的开始角度

    //预定义饼状图颜色
    private int[] mPieColors = new int[]{0xFFE8A200
            , 0xFFCC483F, 0xFFA449A3, 0xFF577AB9, 0xFFC9AEFF
            , 0xFF0EC9FF, 0xFF1DE6B5, 0xFF4CB122};

    private List<? extends PieData> mPieData;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieView);
        mStartAngle = typedArray.getFloat(R.styleable.PieView_startAngle, 0f);

        initArcPaint();
        initTextPaint();

        typedArray.recycle();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化圆弧画笔
     */
    private void initArcPaint() {
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(160);
        mArcPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 初始化文本画笔
     */
    private void initTextPaint() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(20f);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量的宽高
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);

        //获取宽高对应的测量模式
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //记录view实际的宽高
        mActualWidth = w;
        mActualHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPieData == null || mPieData.isEmpty()) {
            return;
        }

        float xCoorOrigin = mActualWidth / 2;
        float yCoorOrigin = mActualHeight / 2;

        //将画布的坐标系原点平移到view的中心点
        canvas.translate(xCoorOrigin, yCoorOrigin);

        //圆弧对应的圆的半径
        float r = Math.min(mActualWidth, mActualHeight) / 2 * 0.6f;

        //圆弧所在椭圆的外切矩形(饼状图对应的外切矩形是正方形)
        RectF rectArc = new RectF(-r, -r, r, r);
        //文本外切矩形
        Rect rectText = new Rect();
        for (int i = 0; i < mPieData.size(); i++) {
            mArcPaint.setColor(mPieData.get(i).getColor());
            canvas.drawArc(rectArc, mStartAngle, mPieData.get(i).getAngle(), false, mArcPaint);

            char[] text = mPieData.get(i).getName().toCharArray();
            mTextPaint.getTextBounds(text, 0, text.length, rectText);

            int widthText = rectText.width();
            int heightText = rectText.height();

            double[] centerPosition = calculatePosition(mPieData.get(i).getAngle(), mStartAngle);
            canvas.drawText(text, 0, text.length,
                    (float) (centerPosition[0] - widthText / 2),
                    (float) (centerPosition[1] - heightText / 2), mTextPaint);


            //下一个圆弧的起始角度
            mStartAngle += mPieData.get(i).getAngle();
        }
    }

    /**
     * 根据弧度计算圆弧中心点坐标
     *
     * @param sweepDegree
     * @return
     */
    private double[] calculatePosition(float sweepDegree, float startAngle) {
        double[] center = new double[2];
        center[0] = Math.cos((double) (sweepDegree / 2 + startAngle));
        center[0] = Math.sin((double) (sweepDegree / 2 + startAngle));
        return center;
    }

    public void setData(List<? extends PieData> pieData) {
        mPieData = pieData;
        initData();
        //刷新
        invalidate();
    }

    public void initData() {
        if (mPieData == null || mPieData.isEmpty()) {
            return;
        }

        //数据项总和，用来计算角度和百分比
        float sumValue = 0.0f;

        for (int i = 0; i < mPieData.size(); i++) {
            sumValue += mPieData.get(i).getValue();
            //数据项对应颜色赋值
            mPieData.get(i).setColor(mPieColors[i % mPieColors.length]);
        }

        float sumAngle = 0;
        for (int i = 0; i < mPieData.size(); i++) {
            float percentage = mPieData.get(i).getValue() / sumValue;
            float angle = percentage * 360;
            sumAngle += angle;
            //数据项百分比赋值
            mPieData.get(i).setPercentage(percentage);
            //数据项角度赋值
            mPieData.get(i).setAngle(angle);
        }

        Logger.i(TAG, "sumAngle = " + sumAngle);
    }

    public void setStartAngle(float StartAngle) {
        this.mStartAngle = StartAngle;
        //刷新
        invalidate();
    }
}
