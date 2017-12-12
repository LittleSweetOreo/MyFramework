package com.nstv.myframework.customview.path;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author liyong
 * @date 2017/12/12 15:28
 * @desc 一个搜索集成动画控件
 */

public class SearchView extends View {
    private static final String TAG = "SearchView";
    private Paint mPaint;
    private Path mSearchPath;
    private Path mCirclePath;
    PathMeasure mPathMeasure;

    private int mActualWidth;
    private int mActualHeight;


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        initListener();
        initAnimator();
        mCurrentStatus = AnimatorStatus.IDLE;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mStartAnimator.start();
                mCurrentStatus = AnimatorStatus.START;
            }
        }, 2000);
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        //设置线帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initPath() {
        mCirclePath = new Path();
        mSearchPath = new Path();

        mPathMeasure = new PathMeasure();

        RectF rectFSearch = new RectF(-50, -50, 50, 50);
        mSearchPath.addArc(rectFSearch, 45, 359.9f);

        RectF rectFCircle = new RectF(-100, -100, 100, 100);
        mCirclePath.addArc(rectFCircle, 45, 359.9f);

        mPathMeasure.setPath(mCirclePath, false);
        float[] pos = new float[2];
        mPathMeasure.getPosTan(0, pos, null);

        //放大镜握把
        mSearchPath.lineTo(pos[0], pos[1]);
        Log.i(TAG, "pos=" + pos[0] + ":" + pos[1]);
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mListener;

    private float mAnimatorValue;
    private ValueAnimator mStartAnimator;
    private ValueAnimator mSearchAnimator;
    private ValueAnimator mEndAnimator;

    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //当前动画结束，通知执行下一个动画
                mAnimatorEndHandler.sendEmptyMessage(0);
                Log.i(TAG, "path search length = " + mPathMeasure.getLength());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initAnimator() {

        mStartAnimator = ValueAnimator.ofFloat(0, 1).setDuration(2000);
        mSearchAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1200);
        mEndAnimator = ValueAnimator.ofFloat(1, 0).setDuration(2000);

        mStartAnimator.addUpdateListener(mUpdateListener);
        mSearchAnimator.addUpdateListener(mUpdateListener);
        mEndAnimator.addUpdateListener(mUpdateListener);

        mStartAnimator.addListener(mListener);
        mSearchAnimator.addListener(mListener);
        mEndAnimator.addListener(mListener);
    }

    //当前动画的状态
    private enum AnimatorStatus {
        IDLE, START, SEARCH, END
    }

    private AnimatorStatus mCurrentStatus = AnimatorStatus.IDLE;
    private int count = 0;
    private Handler mAnimatorEndHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (mCurrentStatus) {
                case START:
                    mStartAnimator.removeAllListeners();
                    mSearchAnimator.start();
                    mCurrentStatus = AnimatorStatus.SEARCH;
                    break;
                case SEARCH:
                    count++;
                    if (count > 2) {
                        mSearchAnimator.removeAllListeners();
                        mEndAnimator.start();
                        mCurrentStatus = AnimatorStatus.END;
                    } else {
                        mSearchAnimator.start();
                    }
                    break;
                case END:
                    mCurrentStatus = AnimatorStatus.IDLE;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mActualWidth = w;
        mActualHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mActualWidth / 2, mActualHeight / 2);
        //绘制View
        drawSearchView(canvas);
    }

    private void drawSearchView(Canvas canvas) {
        switch (mCurrentStatus) {
            case IDLE:
                canvas.drawPath(mSearchPath, mPaint);
                break;
            case START:
                mPathMeasure.setPath(mSearchPath, false);
                Path dst = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SEARCH:
                mPathMeasure.setPath(mCirclePath, false);
                Path dst1 = new Path();
                float start = mPathMeasure.getLength() * mAnimatorValue;
                float stop = start + 6;
                mPathMeasure.getSegment(start, stop, dst1, true);
                canvas.drawPath(dst1, mPaint);
                break;
            case END:
                mPathMeasure.setPath(mSearchPath, false);
                Path dst2 = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
        }
    }
}
