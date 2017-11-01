package com.nstv.myframework.mtldesign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nstv.myframework.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageColorFragment extends Fragment {

	private static final String IMG_RESOURCE_ID = "imgResourceId";


	private int mImgResId;


	public PageColorFragment() {

	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param imgResId Parameter 1.
	 * @return A new instance of fragment PageColorFragment.
	 */
	public static PageColorFragment newInstance(int imgResId) {
		PageColorFragment fragment = new PageColorFragment();
		Bundle args = new Bundle();
		args.putInt(IMG_RESOURCE_ID, imgResId);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mImgResId = getArguments().getInt(IMG_RESOURCE_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View contentView = inflater.inflate(R.layout.fragment_page_color, container, false);
		ImageView img = (ImageView) contentView.findViewById(R.id.img_center);
		img.setImageResource(mImgResId);
		return contentView;
	}

}
