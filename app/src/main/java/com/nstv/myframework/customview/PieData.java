package com.nstv.myframework.customview;

/**
 * @author liyong
 * @date 2017/11/15 10:53
 * @desc 饼状图数据源
 */

public class PieData {
	//用户关心的数据
	private String name; //数据项名称
	private float value; //数据项数值

	//饼状图绘制关心的数据
	private float angle; //绘制的角度
	private int color;   //绘制的颜色
	private float percentage; //占数据项总和百分比

	public PieData(String name, float value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
}
