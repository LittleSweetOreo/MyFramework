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
import android.view.MotionEvent;
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
    private double mR;         //圆弧对应的圆的半径
    private double mArcWidth = 160;  //圆弧的宽度
    private Paint mArcPaint = new Paint();       //圆弧画笔
    private Paint mTextPaint = new Paint();       //文字画笔
    private float mStartAngle; //绘制圆弧的开始角度
    private OnPieClickListener mOnPieClickListener;
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
        Logger.w(TAG, "init StartAngle = " + mStartAngle);
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
        mArcPaint.setStrokeWidth((float) mArcWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 初始化文本画笔
     */
    private void initTextPaint() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(30f);

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

        float mCurrentStartAngle = mStartAngle;

        float xCoorOrigin = mActualWidth / 2;
        float yCoorOrigin = mActualHeight / 2;

        //将画布的坐标系原点平移到view的中心点
        canvas.translate(xCoorOrigin, yCoorOrigin);

        //圆弧对应的圆的半径
        double r = Math.min(mActualWidth, mActualHeight) / 2 * 0.6;
        mR = r;
        //圆弧所在椭圆的外切矩形(饼状图对应的外切矩形是正方形)
        RectF rectArc = new RectF((float) -r, (float) -r, (float) r, (float) r);
        //文本外切矩形
        Rect rectText = new Rect();
        for (int i = 0; i < mPieData.size(); i++) {
            mArcPaint.setColor(mPieData.get(i).getColor());

            //设置该段圆弧起始角度
            mPieData.get(i).setStartAngle(mCurrentStartAngle - 0.5);

            //画圆弧
            canvas.drawArc(rectArc, mCurrentStartAngle - 0.5f, (float) mPieData.get(i).getAngle() + 0.5f, false, mArcPaint);


            String text = mPieData.get(i).getName();
            mTextPaint.getTextBounds(text, 0, text.length(), rectText);

            int widthText = rectText.width();
            int heightText = rectText.height();

            //圆弧中心坐标
            double[] centerPosition = calculatePosition(r, mPieData.get(i).getAngle(), mCurrentStartAngle);

            //画圆弧上的文本内容
            canvas.drawText(text, 0, text.length(),
                    (float) (centerPosition[0] - widthText / 2),
                    (float) (centerPosition[1] + heightText / 2), mTextPaint);

            //下一个圆弧的起始角度
            mCurrentStartAngle += mPieData.get(i).getAngle();
        }
    }


    private float mActionDownX;
    private float mActionDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mActionDownX = event.getX();
                mActionDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //把按下和抬起的时候的触摸点距离<5认为是单击事件
                if (Math.abs(event.getX() - mActionDownX) < 5 && Math.abs(event.getY() - mActionDownY) < 5) {
                    if (isTouchArc(event.getX(), event.getY()) && mOnPieClickListener != null) {
                        mOnPieClickListener.onArcClick(getPositionOnArc(event.getX() - mActualWidth / 2, event.getY() - mActualWidth / 2));
                    }

                }

                break;
        }
        return true;
    }


    /**
     * 判断触摸点是否在圆弧上
     *
     * @param eventX 触摸点X坐标
     * @param eventY 触摸点Y坐标
     * @return
     */
    private boolean isTouchArc(float eventX, float eventY) {
        //触摸点距离圆心的距离平方
        double pointToOrigin2 = Math.pow((double) (eventX - mActualWidth / 2), 2.0) + Math.pow((double) (eventY - mActualHeight / 2), 2.0);
        // 圆弧外侧距圆心的距离平方，+ mArcWidth / 2 是因为画笔描边的宽度跨圆的半径内外各一半
        double outside2 = Math.pow(mR + mArcWidth / 2, 2.0);
        //圆弧内侧距圆心的距离平方
        double inside2 = Math.pow(mR - mArcWidth / 2, 2.0);
        if (pointToOrigin2 < outside2 && pointToOrigin2 > inside2) {
            return true;
        }
        return false;
    }

    public static final int INVALID_POSITION = -1; //无效触摸位置

    /**
     * 得到触摸圆弧的位置
     *
     * @param eventX
     * @param eventY
     * @return
     */
    private int getPositionOnArc(float eventX, float eventY) {
        if (mPieData == null || mPieData.isEmpty()) {
            return INVALID_POSITION;
        }

        double angleWithStart = calculateAngleWithStart(eventX, eventY);
        Logger.w(TAG, "angle with start = " + angleWithStart);

        double currentArcStartAngle = 0;
        for (int i = 0; i < mPieData.size(); i++) {

            double currentArcEndAngle = currentArcStartAngle + mPieData.get(i).getAngle();

            Logger.w(TAG, "currentArcStartAngle = " + currentArcStartAngle);
            Logger.w(TAG, "currentArcEndAngle  = " + currentArcEndAngle);

            if (angleWithStart > currentArcStartAngle && angleWithStart < currentArcEndAngle) {
                return i;
            }
            currentArcStartAngle = currentArcEndAngle;
        }
        return INVALID_POSITION;
    }

    /**
     * 计算触摸点与圆心形成的连线 和 X轴的夹角
     *
     * @param eventX 触摸点X坐标
     * @param eventY 触摸点Y坐标
     * @return
     */
    private double calculateAngleWithX(float eventX, float eventY) {
        if (eventX == 0) {
            if (eventY > 0) {
                return 90;
            } else {
                return 270;
            }
        }

        double δ = Math.atan(eventY / eventX);

        if (δ > 0) {
            if (eventY > 0) {
                return δ * 180 / Math.PI;
            } else if (eventY < 0) {
                return 180 + δ * 180 / Math.PI;
            } else if (eventX > 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (eventY > 0) {
                return 180 - Math.abs(δ) * 180 / Math.PI;
            } else {
                return 360 - Math.abs(δ) * 180 / Math.PI;
            }
        }
    }

    /**
     * 计算触摸点与开始位置的夹角
     *
     * @param eventX
     * @param eventY
     * @return
     */
    private double calculateAngleWithStart(float eventX, float eventY) {
        double tanα = eventY / eventX;
        Logger.e(TAG, "mStartAngle = " + mStartAngle);
        Logger.e(TAG, "Math.abs(Math.atan(tanα)) * 180 / Math.PI= " + Math.abs(Math.atan(tanα)) * 180 / Math.PI);
        if (eventX > 0 && eventY < 0 && tanα < 0 && Math.abs(Math.atan(tanα) * 180 / Math.PI) < Math.abs(mStartAngle)) {
            return Math.abs(mStartAngle) - Math.abs(Math.atan(tanα) * 180 / Math.PI);
        } else {
            return Math.abs(mStartAngle) + calculateAngleWithX(eventX, eventY);
        }
    }

    /**
     * 根据弧度计算圆弧中心点坐标
     *
     * @param r          半径
     * @param sweepAngle 圆弧扫过的角度
     * @param startAngle 开始角度
     * @return
     */
    private double[] calculatePosition(double r, double sweepAngle, double startAngle) {
        double[] center = new double[2];
        center[0] = r * Math.cos(angleToRadian(sweepAngle / 2 + startAngle));
        center[1] = r * Math.sin(angleToRadian(sweepAngle / 2 + startAngle));
        return center;
    }

    /**
     * 角度转成弧度
     *
     * @param angle 角度值 (360°)
     * @return
     */
    private double angleToRadian(double angle) {
        return angle * Math.PI / 180;

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
        double sumValue = 0.0;

        for (int i = 0; i < mPieData.size(); i++) {
            sumValue += mPieData.get(i).getValue();
            //数据项对应颜色赋值
            mPieData.get(i).setColor(mPieColors[i % mPieColors.length]);
        }

        for (int i = 0; i < mPieData.size(); i++) {
            double percentage = mPieData.get(i).getValue() / sumValue;
            double angle = percentage * 360;
            //数据项百分比赋值
            mPieData.get(i).setPercentage(percentage);
            //数据项角度赋值
            mPieData.get(i).setAngle(angle);
        }

    }

    public void setStartAngle(float StartAngle) {
        this.mStartAngle = StartAngle;
        //刷新
        invalidate();
    }

    /**
     * 圆弧点击事件
     */
    public interface OnPieClickListener {
        public void onArcClick(int position);
    }

    public void setOnPieClickListener(OnPieClickListener mOnPieClickListener) {
        this.mOnPieClickListener = mOnPieClickListener;
    }
}
