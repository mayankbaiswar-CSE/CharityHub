package com.dexterous.charityhub;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mayank on 18-04-2015.
 */
public class BaseAdapter2 extends BaseAdapter {
    private Activity activity;
    public static ArrayList name, address;
    public static LayoutInflater layoutInflater = null;

    public BaseAdapter2(Activity a, ArrayList b, ArrayList c) {
        this.activity = a;
        this.name = b;
        this.address = c;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.row_list_item, null);
        }

        TextView name_of_org = (TextView) view.findViewById(R.id.org_name);
        String orgname = name.get(position).toString();
        name_of_org.setText(orgname);

        TextView add_of_org = (TextView) view.findViewById(R.id.org_address);
        String orgadd = address.get(position).toString();
        add_of_org.setText(orgadd);
        return view;
    }
}
