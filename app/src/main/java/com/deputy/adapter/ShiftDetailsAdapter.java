package com.deputy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deputy.activity.R;
import com.deputy.api.model.Shift;
import com.deputy.util.DateUtil;
import com.deputy.util.DbBitmapUtility;
import com.deputy.util.UiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ShiftDetailsAdapter extends BaseAdapter {

    private List<Shift> shiftArrayList;
    private Context _ctxt;

    public ShiftDetailsAdapter(List<Shift> shiftArrayList, Context ctxt) {
        this.shiftArrayList = shiftArrayList;
        this._ctxt = ctxt;
    }

    @Override
    public int getCount() {
        return shiftArrayList.size();
    }

    @Override
    public Shift getItem(int position) {
        return shiftArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(this._ctxt, R.layout.shift_row, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        Picasso.with(_ctxt)
                .load(shiftArrayList.get(position).getImage())
                .resize(500, 500)
                .centerCrop()
                .placeholder(_ctxt.getDrawable(R.drawable.placeholder))
                .into(holder.iv_shift_image);

        return convertView;
    }

    class ViewHolder {

        ImageView iv_shift_image;


        public ViewHolder(View view) {

            iv_shift_image = (ImageView) view.findViewById(R.id.iv_shift_image);

            view.setTag(this);
        }
    }


}