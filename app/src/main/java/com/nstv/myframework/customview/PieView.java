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
import android.view.animation.Animation;
import android.view.animation.Transformation;

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
    private float mArcWidth = 180;  //圆弧的宽度

    private Paint mArcPaint = new Paint();       //圆弧画笔
    private Paint mTextPaint = new Paint();       //文字画笔

    private RectF mRectArc;    //饼状图外切矩形
    private RectF mGrowRectArc; //圆弧点击后对应的外切矩形

    private float mStartAngle;   //绘制圆弧的开始角度,在一个单调区间内方便计算

    private PieChartAnimation mAnimation; //饼状图加载动画
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

        init();
        typedArray.recycle();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //规定开始角度必须在[-90°,90°]内,否则抛出异常
        if (mStartAngle < -90 || mStartAngle > 90) {
            throw new PieStartAngleIllegalException();
        }

        initArcPaint();
        initTextPaint();
        initAnimation();
    }

    /**
     * 初始化圆弧画笔
     */
    private void initArcPaint() {
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(mArcWidth);
        //防止抖动
        mArcPaint.setDither(true);
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

    /**
     * 初始化补间动画
     */
    private void initAnimation() {
        mAnimation = new PieChartAnimation();
        mAnimation.setDuration(1000);
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

        //圆弧对应的圆的半径
        float k = 0.6f;
        mR = Math.min(mActualWidth, mActualHeight) / 2 * k;

        //圆弧所在椭圆的外切矩形(饼状图对应的外切矩形是正方形)
        mRectArc = new RectF((float) -mR, (float) -mR, (float) mR, (float) mR);

        //整个扇形不能超出View的范围
        //经计算当 0<k<= 0.5时,0<mArcWidth<=k*mActualWidth
        //当0.5<k<1时,0<mArcWidth<=(1-k)*mActualWidth
        if (k > 0 && k <= 0.5) {
            mArcWidth = k * 0.4f * mActualWidth;
        } else {
            mArcWidth = (1 - k) * 0.6f * mActualWidth;
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPieData == null || mPieData.isEmpty()) {
            return;
        }

        //将画布的坐标系原点平移到view的中心点
        canvas.translate(mActualWidth / 2, mActualHeight / 2);

        mArcPaint.setStrokeWidth(mArcWidth);

        //文本外切矩形
        Rect rectText = new Rect();
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mPieData.size(); i++) {
            PieData pieData = mPieData.get(i);
            mArcPaint.setColor(pieData.getColor());
            if (mIsNeedGrow && i == mGrowPosition) {
                canvas.drawArc(mGrowRectArc, currentStartAngle, (float) pieData.getAngle(), false, mArcPaint);
            } else {
                //画圆弧
                canvas.drawArc(mRectArc, currentStartAngle, (float) pieData.getAngle(), false, mArcPaint);
            }

            String text = pieData.getName();
            mTextPaint.getTextBounds(text, 0, text.length(), rectText);

            int widthText = rectText.width();
            int heightText = rectText.height();

            //圆弧中心坐标
            double[] centerPosition = calculatePosition(mR, pieData.getAngle(), pieData.getCurrentStartAngle());

            //画圆弧上的文本内容
            if (mIsNeedGrow && i == mGrowPosition) {
                canvas.drawText(text, 0, text.length(),
                        (float) (centerPosition[0] - widthText / 2 + mTranslateX),
                        (float) (centerPosition[1] + heightText / 2 + mTranslateY), mTextPaint);
            } else {
                canvas.drawText(text, 0, text.length(),
                        (float) (centerPosition[0] - widthText / 2),
                        (float) (centerPosition[1] + heightText / 2), mTextPaint);
            }

            currentStartAngle = (float) (currentStartAngle + pieData.getAngle());
        }
    }


    private float mActionDownX;
    private float mActionDownY;
    private boolean mIsNeedGrow;
    private int mGrowPosition = INVALID_POSITION;
//    private float mEventOldX;
//    private float mEventOldY;
//    private float mEventCurrentX;
//    private float mEventCurrentY;

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
                        int positon = getPositionOnArc(event.getX() - mActualWidth / 2, event.getY() - mActualWidth / 2);
                        if (positon == -1) {
                            break;
                        } else {
                            if (positon == mGrowPosition) {//突出的扇形复原
                                mIsNeedGrow = false;
                                mGrowPosition = INVALID_POSITION;
                            } else {//突出的扇形复原，并且使当前点击的扇形突出
                                mIsNeedGrow = true;
                                mGrowPosition = positon;
                                arcGrow(positon);
                            }
                            mOnPieClickListener.onArcClick(positon);
                            invalidate();
                        }
                    }

                }
                break;
//            case MotionEvent.ACTION_MOVE:
//                mEventCurrentX = event.getX();
//                mEventCurrentY = event.getY();
////                float difAngle = (float) (2 * Math.pow((mEventCurrentX - mEventOldX) * (mEventCurrentX - mEventOldX) + (mEventCurrentY - mEventOldY) * (mEventCurrentY - mEventOldY), 1 / 2));
//                float difAngle;
//                if (mEventCurrentX > 0 && mEventOldX > 0) {
//                    difAngle = 2 * Math.abs(mEventCurrentY - mEventOldY);
//                    if (mEventCurrentY - mEventOldY > 0) {//x轴正半轴顺时针
//                        mStartAngle += difAngle;
//                    } else {
//                        mStartAngle -= difAngle;
//                    }
//                } else if (mEventCurrentX < 0 && mEventOldX < 0) {
//                    difAngle = 2 * Math.abs(mEventCurrentY - mEventOldY);
//                    if (mEventCurrentY - mEventOldY < 0) {//x轴负半轴顺时针
//                        mStartAngle += difAngle;
//                    } else {
//                        mStartAngle -= difAngle;
//                    }
//                } else if (mEventCurrentX > 0 && mEventOldX < 0) {
//                    difAngle = 2 * Math.abs(mEventCurrentX - mEventOldX);
//                    if (mEventCurrentY > 0 && mEventOldY > 0) {//y轴正半轴逆时针
//                        mStartAngle -= difAngle;
//                    } else if (mEventCurrentY < 0 && mEventOldY < 0) {//y轴负半轴顺时针
//                        mStartAngle += difAngle;
//                    }
//                } else {
//                    difAngle = 2 * Math.abs(mEventCurrentX - mEventOldX);
//                    if (mEventCurrentY > 0 && mEventOldY > 0) {//y轴正半轴顺时针
//                        mStartAngle += difAngle;
//                    } else if (mEventCurrentY < 0 && mEventOldY < 0) {//y轴负半轴逆时针
//                        mStartAngle -= difAngle;
//                    }
//                }
//
//                if (mStartAngle < -90 || mStartAngle > 90) {
//                    break;
//                } else {
//                    notifyDataChanged();
//                }
//
//                mEventOldX = mEventCurrentX;
//                mEventOldY = mEventCurrentY;
//                break;
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

        double angleWithX = calculateTouchAngle(eventX, eventY);
        Logger.w(TAG, "angle with start = " + angleWithX);

        double currentArcStartAngle = mStartAngle;
        for (int i = 0; i < mPieData.size(); i++) {

            double currentArcEndAngle = currentArcStartAngle + mPieData.get(i).getAngle();

            Logger.w(TAG, "currentArcStartAngle = " + currentArcStartAngle);
            Logger.w(TAG, "currentArcEndAngle  = " + currentArcEndAngle);

            if (angleWithX > currentArcStartAngle && angleWithX < currentArcEndAngle) {
                return i;
            }
            currentArcStartAngle = currentArcEndAngle;
        }

        return INVALID_POSITION;
    }

    private double mArcSpace = 14; //凸起的圆弧与其他圆弧的间距
    private double mTranslateX = 0;
    private double mTranslateY = 0;

    /**
     * 使点击的那个圆弧凸起
     *
     * @param position
     */
    private void arcGrow(int position) {
        try {
            PieData pieData = mPieData.get(position);
            double angleStart = pieData.getCurrentStartAngle();
            double angleEnd = angleStart + pieData.getAngle();
            double centerAngle = (angleStart + angleEnd) / 2;
            double translateX = mArcSpace / (Math.sin(toRadian(pieData.getAngle() / 2)) * Math.pow((1 + Math.pow(Math.tan(toRadian(centerAngle)), 2)), 1 / 2));
            if ((centerAngle > -90 && centerAngle < 90) || (centerAngle > 270 && centerAngle < 450)) {
                translateX *= 1;
            } else {
                translateX *= -1;
            }
            double translateY = Math.tan(toRadian(centerAngle)) * translateX;
            mTranslateX = translateX;
            mTranslateY = translateY;
            mGrowRectArc = new RectF((float) (-mR + translateX), (float) (-mR + translateY), (float) (mR + translateX), (float) (mR + translateY));
        } catch (Exception e) {
            Logger.e(TAG, "exception " + e.toString());
        }
    }

    /**
     * 计算坐标与圆心形成的连线和X轴正半轴的夹角,
     * 坐标系标准:X轴和Y轴的正方向分别向右、向下
     * 角度按顺时针方向从0°开始逐渐增大
     *
     * @param eventX 触摸点X坐标
     * @param eventY 触摸点Y坐标
     * @return 返回值范围[0, 360]
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
                return toAngle(δ);
            } else if (eventY < 0) {
                return 180 + toAngle(δ);
            } else if (eventX > 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (eventY > 0) {
                return 180 - toAngle(Math.abs(δ));
            } else {
                return 360 - toAngle(Math.abs(δ));
            }
        }
    }

    /**
     * 以饼状图圆弧起始角度为参考标准
     * 计算触摸点与X轴正半轴的夹角
     *
     * @param eventX
     * @param eventY
     * @return
     */
    private double calculateTouchAngle(float eventX, float eventY) {
        if (mStartAngle >= -90 && mStartAngle < 0) {
            if (eventX > 0 && eventY < 0) {
                double tanα = eventY / eventX;
                if (tanα > Math.tan(toRadian(mStartAngle))) {
                    return toAngle(Math.atan(tanα));
                } else {
                    return calculateAngleWithX(eventX, eventY);
                }
            } else {
                return calculateAngleWithX(eventX, eventY);
            }
        } else {
            if (eventX > 0 && eventY > 0) {
                double tanα = eventY / eventX;
                if (tanα < Math.tan(toRadian(mStartAngle))) {
                    return 360 + calculateAngleWithX(eventX, eventY);
                } else {
                    return calculateAngleWithX(eventX, eventY);
                }
            } else {
                return calculateAngleWithX(eventX, eventY);
            }
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
        center[0] = r * Math.cos(toRadian(sweepAngle / 2 + startAngle));
        center[1] = r * Math.sin(toRadian(sweepAngle / 2 + startAngle));
        return center;
    }

    /**
     * 角度转成弧度
     *
     * @param angle 角度值
     * @return
     */
    private double toRadian(double angle) {
        return angle * Math.PI / 180;

    }

    /**
     * 弧度转角度
     *
     * @param radian
     * @return
     */
    private double toAngle(double radian) {
        return radian * 180 / Math.PI;
    }

    /**
     * 填充数据
     *
     * @param pieData
     */
    public void setData(List<? extends PieData> pieData) {
        if (pieData == null || pieData.isEmpty())
            return;
        mPieData = pieData;
        setPieData();
        startAnimation(mAnimation);
        //刷新
        invalidate();
    }

    public void setPieData() {
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

        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mPieData.size(); i++) {
            double percentage = mPieData.get(i).getValue() / sumValue;
            double angle = percentage * 360;
            //当前圆弧开始角度
            mPieData.get(i).setCurrentStartAngle(currentStartAngle);
            //数据项角度赋值
            mPieData.get(i).setAngle(angle);
            //数据项百分比赋值
            mPieData.get(i).setPercentage(percentage);
            currentStartAngle += angle;
        }
    }

    public void notifyDataChanged() {
        double currentStartAngle = mStartAngle;
        for (int i = 0; i < mPieData.size(); i++) {
            PieData pieData = mPieData.get(i);
            pieData.setCurrentStartAngle(currentStartAngle);
            currentStartAngle += pieData.getAngle();
        }
        invalidate();
    }

    /**
     * 设置饼状图绘制的开始角度
     *
     * @param StartAngle ∈[-90,90]
     */
    public void setStartAngle(float StartAngle) {
        this.mStartAngle = StartAngle;

        //规定开始角度必须在[-90°,90°]内,否则抛出异常
        if (mStartAngle < -90 || mStartAngle > 90) {
            throw new PieStartAngleIllegalException();
        }
        //刷新
        invalidate();
    }

    /**
     * 圆弧点击监听接口
     */
    public interface OnPieClickListener {
        void onArcClick(int position);
    }

    public void setOnPieClickListener(OnPieClickListener mOnPieClickListener) {
        this.mOnPieClickListener = mOnPieClickListener;
    }

    /**
     * @author liyong
     * @date 2017/11/23 11:53
     * @desc 饼状图补间动画
     */
    class PieChartAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime <= 1.0f) {

                for (int i = 0; i < mPieData.size(); i++) {
                    PieData pieData = mPieData.get(i);
                    double angle = mPieData.get(i).getPercentage() * 360;
                    pieData.setAngle(angle * interpolatedTime);
                }

            }
            invalidate();
        }
    }

}
