package com.nstv.myframework.animator;

/**
 * @author liyong
 * @date 2017/9/19 15:20
 * @desc 用来描述一个点
 */

public class Point {
	private float x;
	private float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
