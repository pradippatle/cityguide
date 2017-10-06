package com.pradipatle.cityguide.tumsar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pradipatle.cityguide.tumsar.R;
import com.pradipatle.cityguide.tumsar.model.ModelContacts;

import java.util.ArrayList;

/**
 * Created by Aeon-02 on 03-04-2017.
 */

public class contactListAdapter extends BaseAdapter {

    ArrayList<ModelContacts> contactsList;
    Context context;

    public contactListAdapter(ArrayList<ModelContacts> contList, Context context) {
        this.contactsList = contList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactsList.size();
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
            convertView = inflater.inflate(R.layout.row_contacts, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.txt_row_name);
        TextView mobile = (TextView) convertView.findViewById(R.id.txt_row_mobile);

        name.setText(contactsList.get(position).getName());
        mobile.setText(contactsList.get(position).getMobile());

        return convertView;
    }


}
