package com.nstv.myframework.customview.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author liyong
 * @date 2017/11/29 17:33
 * @desc 贝塞尔二阶曲线例子
 */

public class QuadView extends View {
	private int mActualWidth;
	private int mActualHeight;
	private Paint mPaint;
	private PointF mStart, mEnd, mControl;

	public QuadView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
		initPaint();
	}

	public QuadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(20);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mActualWidth = w;
		mActualHeight = h;

		mStart = new PointF(-200, 0);
		mEnd = new PointF(200, 0);
		mControl = new PointF(100, 160);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate((float) mActualWidth / 2, (float) mActualHeight / 2);


		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(20);
		canvas.drawPoint(mStart.x, mStart.y, mPaint);
		canvas.drawPoint(mEnd.x, mEnd.y, mPaint);
		canvas.drawPoint(mControl.x, mControl.y, mPaint);

		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(10);
		Path path = new Path();
		path.moveTo(mStart.x, mStart.y);
		path.lineTo(mControl.x, mControl.y);
		path.lineTo(mEnd.x, mEnd.y);
		canvas.drawPath(path,mPaint);

		//清除Path内部数据
		path.reset();
		path.moveTo(mStart.x, mStart.y);
		mPaint.setColor(Color.RED);
		path.quadTo(mControl.x, mControl.y, mEnd.x, mEnd.y);
		canvas.drawPath(path,mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mControl.x = event.getX() - mActualWidth / 2;
		mControl.y = event.getY() - mActualHeight / 2;
		invalidate();
		return true;
	}
}
