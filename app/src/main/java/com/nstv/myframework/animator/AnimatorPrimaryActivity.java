package com.nstv.myframework.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import com.nstv.myframework.BaseActivity;
import com.nstv.myframework.R;

public class AnimatorPrimaryActivity extends BaseActivity {
	@Override
	protected int bindLayout() {
		return R.layout.activity_primary_animator;
	}

	public void rotateAnimRun(View view) {
		ObjectAnimator
				.ofFloat(view, "rotation", 0.0f, 180.0f)
				.setDuration(2000)
				.start();
	}

	public void animator(View view) {
		//x轴缩放
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 3f, 1f);
		scaleXAnimator.setRepeatCount(-1);

		//y轴缩放
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 3f, 1f);
		scaleYAnimator.setRepeatCount(-1);

		//x轴平移
		ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 300f, 0);
		translateXAnimator.setRepeatCount(-1);

		//透明度
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f, 1f);
		alphaAnimator.setRepeatCount(-1);

		//动画集合
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(scaleXAnimator).with(scaleYAnimator).with(translateXAnimator).with(alphaAnimator);
		animatorSet.setDuration(4000);
		animatorSet.start();

	}

	public void alphaXml(View view) {
		Animator alphaAnimator = AnimatorInflater.loadAnimator(this, R.animator.alpha);
		alphaAnimator.setTarget(view);
		alphaAnimator.start();
	}

	public void rotationXml(View view) {
		Animator rotateAnimator = AnimatorInflater.loadAnimator(this, R.animator.rotation);
		rotateAnimator.setTarget(view);
		rotateAnimator.start();
	}

	public void translationXml(View view) {
		Animator rotateAnimator = AnimatorInflater.loadAnimator(this, R.animator.translation);
		rotateAnimator.setTarget(view);
		rotateAnimator.start();
	}

	public void scaleXml(View view) {
		Animator scaleAnimator = AnimatorInflater.loadAnimator(this, R.animator.scale);
		scaleAnimator.setTarget(view);
		scaleAnimator.start();
	}

	public void animatorSet(View view) {
		Animator animator = AnimatorInflater.loadAnimator(this, R.animator.animator_set);
		animator.setTarget(view);
		animator.start();
	}
}
