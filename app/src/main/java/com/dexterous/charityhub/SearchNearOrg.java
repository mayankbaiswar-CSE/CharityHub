package com.dexterous.charityhub;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchNearOrg extends AppCompatActivity {
    ListView nearOrgList;
    ProgressBar progressBar;
    double lat, lon;
    Geocoder geocoder;
    String finaladdress;
    private AQuery aQuery;
    public final String TAG = "SearchNearOrg";
    ArrayList<String> _name = new ArrayList<>();
    ArrayList<String> _add = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_near_org);
        nearOrgList = (ListView) findViewById(R.id.nearBy);
        progressBar = (ProgressBar) findViewById(R.id.nearProgress);
        progressBar.setVisibility(View.VISIBLE);
        aQuery = new AQuery(getApplicationContext());
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
            Log.i("gps", String.valueOf(lat) + " " + String.valueOf(lon));
        } else {
            gpsTracker.showSettingsAlert();
        }
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                finaladdress = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
        }
        Log.i(TAG, ""+finaladdress);
        completeUrl();
        nearOrgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Complete_Info.class);
                try {
                    intent.putExtra(MainActivity.ID, res.getJSONObject(position).getInt(MainActivity.ID));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, e.toString());
                }
                startActivity(intent);
            }
        });
    }

    public String _url;

    protected void completeUrl() {
        _url = "http://" + MainActivity.ipNow +
                MainActivity.base_url +
                finaladdress +
                MainActivity.end_url_location;
        async_json_array();
    }

    protected void async_json_array() {
        aQuery.ajax(_url, JSONArray.class, new AjaxCallback<JSONArray>() {
            public void callback(String url, JSONArray ja, AjaxStatus status) {
                if (ja != null) {
//                    setSupportProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                    showResult(ja);
                } else {
//                    setSupportProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(aQuery.getContext(),
                            "Error: " + status.getCode() + " You might wanna test your connection again.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
    public JSONArray res;
    protected void showResult(JSONArray result) {
        res = result;
        _name.clear();
        _add.clear();
        for (int i = 0, count = result.length(); i < count; i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                _name.add(jsonObject.getString("name"));
                _add.add(jsonObject.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
            }
        }
        BaseAdapter2 adapter = new BaseAdapter2(SearchNearOrg.this, _name, _add);
        nearOrgList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_near_org, menu);
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
