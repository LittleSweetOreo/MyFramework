package com.nstv.myframework.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MyAnimatorPointView extends View {

	private Point mPoint;
	private Paint mPaint;
	private Context mContext;
	private String color;
	public static final float RADIUS = 50f;

	public void setColor(String color) {
		this.color = color;
		mPaint.setColor(Color.parseColor(color));
		invalidate();
	}

	public String getColor() {
		return color;
	}

	public MyAnimatorPointView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLUE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Point startPoint = new Point(RADIUS, RADIUS);
		Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
		if (mPoint == null) {
			mPoint = new Point(RADIUS, RADIUS);
			canvas.drawCircle(RADIUS, RADIUS, RADIUS, mPaint);
			startAnimator(startPoint, endPoint);
		} else {
			canvas.drawCircle(mPoint.getX(), mPoint.getY(), RADIUS, mPaint);
		}

	}

	private void startAnimator(Point startPoint, Point endPoint) {
		ValueAnimator animatorPoint = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
		animatorPoint.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mPoint = (Point) animation.getAnimatedValue();
				invalidate();
			}
		});
		ObjectAnimator animatorColor = ObjectAnimator
				.ofObject(this, "color", new ColorEvaluator(), "#0000FF", "#FF0000");

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(animatorPoint).with(animatorColor);
		animatorSet.setDuration(5000);
		animatorSet.start();
	}

	/**
	 * 定义Point对象如何从初始位置变化到结束位置
	 */
	class PointEvaluator implements TypeEvaluator<Point> {

		@Override
		public Point evaluate(float fraction, Point startValue, Point endValue) {
			float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
			float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
			Point point = new Point(x, y);
			return point;
		}
	}

	/**
	 * 定义颜色的变化过度过程
	 */
	class ColorEvaluator implements TypeEvaluator<String> {

		private int curRed = -1;
		private int curGreen = -1;
		private int curBlue = -1;

		@Override
		public String evaluate(float fraction, String startValue, String endValue) {
			int startRed = Integer.parseInt(startValue.substring(1, 3), 16);
			int startGreen = Integer.parseInt(startValue.substring(3, 5), 16);
			int startBlue = Integer.parseInt(startValue.substring(5, 7), 16);
			int endRed = Integer.parseInt(endValue.substring(1, 3), 16);
			int endGreen = Integer.parseInt(endValue.substring(3, 5), 16);
			int endBlue = Integer.parseInt(endValue.substring(5, 7), 16);

			if (curRed == -1)
				curRed = startRed;

			if (curGreen == -1)
				curGreen = startGreen;

			if (curBlue == -1)
				curBlue = startBlue;


			//计算出初始结束颜色差值
			int difRed = Math.abs(endRed - startRed);
			int difGreen = Math.abs(endGreen - startGreen);
			int difBlue = Math.abs(endBlue - startBlue);
			int difColor = difRed + difGreen + difBlue;

			if (curRed != endRed) {
				curRed = getCurColor(fraction, startRed, endRed, difColor, 0);
			} else if (curGreen != endGreen) {
				curGreen = getCurColor(fraction, startGreen, endGreen, difColor, difRed);
			} else if (curBlue != endBlue) {
				curBlue = getCurColor(fraction, startBlue, endBlue, difColor, difRed + difGreen);
			}
			String curColor = "#" + getHexString(curRed) + getHexString(curGreen) + getHexString(curBlue);
			return curColor;
		}
	}

	private int getCurColor(float fraction, int startColor, int endColor, int colorDif, int offset) {
		int curColor;
		if (startColor < endColor) {
			curColor = (int) (startColor + (colorDif * fraction - offset));
			if (curColor > endColor) {
				curColor = endColor;
			}
		} else {
			curColor = (int) (startColor - (colorDif * fraction - offset));
			if (curColor < endColor) {
				curColor = endColor;
			}
		}
		return curColor;
	}

	private String getHexString(int colorInt) {
		String colorHex = Integer.toHexString(colorInt);
		if (colorHex.length() == 1) {
			colorHex = "0" + colorHex;
		}
		return colorHex;
	}
}
