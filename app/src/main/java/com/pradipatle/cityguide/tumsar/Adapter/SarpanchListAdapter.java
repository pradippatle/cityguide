package com.pradipatle.cityguide.tumsar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pradipatle.cityguide.tumsar.R;
import com.pradipatle.cityguide.tumsar.model.SarpanchTahsildarModel;

import java.util.ArrayList;

/**
 * Created by Aeon-02 on 03-04-2017.
 */

public class SarpanchListAdapter extends BaseAdapter {

    ArrayList<SarpanchTahsildarModel> sarpanchList;
    Context context;

    public SarpanchListAdapter(ArrayList<SarpanchTahsildarModel> pastRecordsList, Context context) {
        this.sarpanchList = pastRecordsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sarpanchList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_sarpanch_tahsildar, parent, false);
        }
        TextView village = (TextView) convertView.findViewById(R.id.txt_row_village);
        TextView name = (TextView) convertView.findViewById(R.id.txt_row_name);
        TextView mobile = (TextView) convertView.findViewById(R.id.txt_row_mobile);

        village.setText(sarpanchList.get(position).getVillage());
        name.setText(sarpanchList.get(position).getName());
        mobile.setText(sarpanchList.get(position).getMobile());

        return convertView;
    }


}
