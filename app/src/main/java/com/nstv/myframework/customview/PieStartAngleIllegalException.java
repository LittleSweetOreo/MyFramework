package com.nstv.myframework.customview;

/**
 * @author liyong
 * @date 2017/11/20 15:39
 * @desc 饼状图开始角度不合法异常检查类
 */

public class PieStartAngleIllegalException extends RuntimeException {
	public static final String E = "Pie chart's start angle must be between -90° and 90°";

	public PieStartAngleIllegalException() {
		this(E);
	}

	public PieStartAngleIllegalException(String detailMessage) {
		super(detailMessage);
	}

}
