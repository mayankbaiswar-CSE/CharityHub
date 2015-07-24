package com.dexterous.charityhub;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mayank on 25-06-2015.
 */
public class BaseAdapterForNeeds extends BaseAdapter {
    private Activity activity;
    public static ArrayList needs;
    public static LayoutInflater layoutInflater = null;

    public BaseAdapterForNeeds(Activity a, ArrayList b) {
        activity = a;
        needs = b;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return needs.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;
        if (mView == null) {
            mView = layoutInflater.inflate(R.layout.needs_list_row, null);
        }
        TextView need_of_org = (TextView) mView.findViewById(R.id.org_needs);
        if (need_of_org == null) {
            Log.i("baseadapter", "it is null.");
        }
        need_of_org.setText(needs.get(i).toString());
        return mView;
    }
}
