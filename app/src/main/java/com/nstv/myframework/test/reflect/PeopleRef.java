package com.nstv.myframework.test.reflect;

/**
 * @author liyong
 * @date 2017/11/9 14:32
 * @desc 反射研究测试类
 */

public class PeopleRef {
	private String mName;
	private Sex mSex;
	private int mAge;
	private static final String TAG = "PeopleRef";

	public static void main(String[] args) {
		PeopleRef peopleOne = new PeopleRef("张三丰", Sex.MALE, 67);
		Class peopOneCls = peopleOne.getClass();

		System.out.println(TAG + " cls.getName() is " + peopOneCls.getName());
		System.out.println(TAG + " cls.getSimpleName() is " + peopOneCls.getSimpleName());

		try {
			System.out.println(TAG + " cls.getDeclaredField(mName) is " + peopOneCls.getDeclaredField("mSex"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public PeopleRef(String mName, Sex mSex, int mAge) {
		this.mName = mName;
		this.mSex = mSex;
		this.mAge = mAge;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public void setSex(Sex mSex) {
		this.mSex = mSex;
	}

	public void setAge(int mAge) {
		this.mAge = mAge;
	}

	public String getmName() {
		return mName;
	}

	public Sex getmSex() {
		return mSex;
	}

	public int getmAge() {
		return mAge;
	}

}
