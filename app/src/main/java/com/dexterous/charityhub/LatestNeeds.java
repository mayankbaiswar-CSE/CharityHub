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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class LatestNeeds extends ActionBarActivity {

    public static String TAG = "LatestNeeds";
    ListView listView;
    private AQuery aq;
    ProgressBar progressBar;
    public static String latestNeedUrl = "http://" + MainActivity.ipNow + ":8080/CharityHub/getallnewsfeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_needs);
        aq = new AQuery(getApplicationContext());
        listView = (ListView) findViewById(R.id.latest_needs);
        progressBar = (ProgressBar) findViewById(R.id.progress_needs);
        progressBar.setVisibility(View.VISIBLE);
//        setSupportProgressBarIndeterminateVisibility(true);
        async_json_array();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(),
//                        "Item no. " + i,
//                        Toast.LENGTH_LONG)
//                        .show();
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });

    }

    private void async_json_array() {
        aq.ajax(latestNeedUrl, JSONArray.class, new AjaxCallback<JSONArray>() {
            public void callback(String url, JSONArray ja, AjaxStatus status) {
                if (ja != null) {
                    setSupportProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                    try {
                        showResult(ja);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(TAG, e.toString());
                    }
                } else {
                    setSupportProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(aq.getContext(),
                            "Error: " + status.getCode() + " You might wanna test your connection again.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void showResult(JSONArray jsonArray) throws JSONException {
        ArrayList<String> needArrayList = new ArrayList<>();
        for (int i = 0, count = jsonArray.length(); i < count; i++) {
            needArrayList.add(jsonArray.getJSONObject(i).getString("newFeed"));
        }
        BaseAdapterForNeeds latestNeedAdapter = new BaseAdapterForNeeds(LatestNeeds.this, needArrayList);
        listView.setAdapter(latestNeedAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_latest_needs, menu);
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
