package com.nstv.myframework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author liyong
 * @date 2017/10/9 14:48
 * @desc MainActivity滑动列表适配器
 */

public class MainAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<String> mList;

	public MainAdapter(Context context, List<String> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return (mList == null || mList.isEmpty()) ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_main_listview, parent, false);
			holder = new MyViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (MyViewHolder) convertView.getTag();
		}

		holder.tvName.setText(mList.get(position));

		return convertView;
	}

	class MyViewHolder {
		private TextView tvName;

		MyViewHolder(View itemView) {
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
		}
	}
}
