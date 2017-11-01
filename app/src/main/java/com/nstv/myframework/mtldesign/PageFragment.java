package com.nstv.myframework.mtldesign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nstv.myframework.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

	private static final String CONTENT_TEXT = "content_text";


	private String mText;


	public PageFragment() {

	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param text Parameter 1.
	 * @return A new instance of fragment PageColorFragment.
	 */
	public static PageFragment newInstance(String text) {
		PageFragment fragment = new PageFragment();
		Bundle args = new Bundle();
		args.putString(CONTENT_TEXT, text);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mText = getArguments().getString(CONTENT_TEXT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View contentView = inflater.inflate(R.layout.fragment_page, container, false);
		TextView textView = (TextView) contentView.findViewById(R.id.tv_name);
		textView.setText(mText);
		return contentView;
	}

}
