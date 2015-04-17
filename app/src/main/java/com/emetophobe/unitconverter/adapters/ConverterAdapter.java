/*
 * Copyright (C) 2013-2015 Mike Cunningham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.emetophobe.unitconverter.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emetophobe.unitconverter.R;

import java.util.List;


public class ConverterAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Pair<String, Double>> mData;

	public ConverterAdapter(Context context, List<Pair<String, Double>> list) {
		mInflater = LayoutInflater.from(context);
		mData = list;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Pair<String, Double> getItem(int position) {
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.converter_list_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.unit_name);
			holder.value = (TextView) convertView.findViewById(R.id.unit_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Pair<String, Double> pair = getItem(position);
		if (pair != null) {
			holder.name.setText(pair.first);
			holder.value.setText(String.valueOf(pair.second));
		}

		return convertView;
	}

	private static class ViewHolder {
		public TextView name;
		public TextView value;
	}
}