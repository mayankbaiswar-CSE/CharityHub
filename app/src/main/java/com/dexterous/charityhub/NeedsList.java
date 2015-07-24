package com.dexterous.charityhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;


public class NeedsList extends ActionBarActivity {
    public static String NEWS = "newsfeed";
    public static String TAG = "NeedList";
    public static String needSelect;
    ArrayList<String> need_of_org = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need);
        listView = (ListView) findViewById(R.id.list_need);
        try {
            showdata();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    needSelect = Complete_Info.NEEDS.getJSONObject(i).getString(NEWS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, e.toString());
                }
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
    }

    public void showdata() throws JSONException {
        need_of_org.clear();
        for (int i = 0, count = Complete_Info.NEEDS.length(); i < count; i++) {
            need_of_org.add(Complete_Info.NEEDS.getJSONObject(i).getString(NEWS));
        }
        BaseAdapterForNeeds baseAdapterForNeeds = new BaseAdapterForNeeds(NeedsList.this, need_of_org);
        listView.setAdapter(baseAdapterForNeeds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_needs_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
