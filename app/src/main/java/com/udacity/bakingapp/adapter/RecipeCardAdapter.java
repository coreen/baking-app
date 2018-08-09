package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.bakingapp.R;

import timber.log.Timber;

public class RecipeCardAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mCardNames;

    public RecipeCardAdapter(Context mContext, String[] mCardNames) {
        this.mContext = mContext;
        this.mCardNames = mCardNames;
    }

    @Override
    public int getCount() {
        if (null == mCardNames) {
            return 0;
        }
        return mCardNames.length;
    }

    @Override
    public Object getItem(int position) {
        return mCardNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            Timber.d("layoutInflater: " + layoutInflater);
            convertView = layoutInflater.inflate(R.layout.recipe_card_item, null);
            Timber.d("convertView: " + layoutInflater);
        }
        final TextView mNameText = convertView.findViewById(R.id.tv_recipe_card_name);
        mNameText.setText(mCardNames[position]);
        return convertView;
    }
}
