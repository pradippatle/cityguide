package com.pradipatle.cityguide.tumsar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pradipatle.cityguide.tumsar.R;
import com.pradipatle.cityguide.tumsar.model.FbFeedsModel;
import java.util.ArrayList;

/**
 * Created by Aeon-02 on 9/11/2017.
 */

public class FbFeedsAdapter extends BaseAdapter {

    ArrayList<FbFeedsModel> feedsList;
    Context context;

    public FbFeedsAdapter(ArrayList<FbFeedsModel> feedsList, Context context) {
        this.feedsList = feedsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return feedsList.size();
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
            convertView = inflater.inflate(R.layout.row_fb_feeds, parent, false);
        }
        TextView time = (TextView) convertView.findViewById(R.id.txt_created_time);
        TextView feeds = (TextView) convertView.findViewById(R.id.txt_feeds);

        feeds.setText("Facebook Post :\n\n"+feedsList.get(position).getFeeds());
        time.setText("Created at : "+feedsList.get(position).getCreatedTime());

        return convertView;
    }


}
