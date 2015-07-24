package com.dexterous.charityhub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    FloatingActionButton searchButton, menu1, menu2;
    FloatingActionMenu menu;
    BootstrapEditText searchBar;
    EditText input;
    ImageView imageView;
    ProgressBar progressBar;
    ListView listView;
    ArrayList<String> oname = new ArrayList<>();
    ArrayList<String> oadd = new ArrayList<>();
    private AQuery aq;
    JSONArray store_info_array;
    Animation bounce;
    public static String ipNow = "";
    public static final String ORG = "name";
    public static final String ADDRESS = "city";
    public static final String ID = "id";
    public static String key;
    public static String Urls;
    public static String base_url = ":8080/CharityHub/search?search_key=";
    public static String end_url = "&search_key_type=search_key_type_no_of_people";
    public static String end_url_location = "&search_key_type=search_key_type_location";
    int backPressCount = 0;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCount++;
        if (backPressCount == 1) {
            Toast.makeText(
                    getApplicationContext(),
                    "Press Back Again to Exit.",
                    Toast.LENGTH_SHORT
            ).show();
        }
        if (backPressCount == 2) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(getApplicationContext());
        listView = (ListView) findViewById(R.id.list);
        searchButton = (FloatingActionButton) findViewById(R.id.fab);
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu1 = (FloatingActionButton) findViewById(R.id.menu_item1);
        menu2 = (FloatingActionButton) findViewById(R.id.menu_item2);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        searchBar = (BootstrapEditText) findViewById(R.id.search);
        bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        menu.startAnimation(bounce);
        //TODO: GCM
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
//                        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(context);
                        boolean sentToken = sharedPreferences
                                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                        if (sentToken) {
                            Log.i("gcm", getString(R.string.gcm_send_message));
                        } else {
                            Log.i("gcm", getString(R.string.token_error_message));
                        }
                    }
                };
                if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(getApplication(), RegistrationIntentService.class);
                    startService(intent);
                }
            }
        }).start();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setVisibility(View.GONE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                setVars();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Complete_Info.class);
                try {
                    intent.putExtra(ID, store_info_array.getJSONObject(position).getInt(ID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("play", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void onClickMenu1(View v) {
        startActivity(new Intent(getApplicationContext(), LatestNeeds.class));
//        Toast.makeText(getApplicationContext(), "Menu Button 1 Pressed", Toast.LENGTH_LONG).show();
    }

    public void onClickMenu2(View v) {
//        Toast.makeText(getApplicationContext(), "Menu Button 2 Pressed", Toast.LENGTH_LONG).show();
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            startActivity(new Intent(this, SearchNearOrg.class));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void setVars() {
        key = searchBar.getText().toString();
        key.replaceAll(" ", "%20");
        setSupportProgressBarIndeterminateVisibility(true);
        if (ipNow.isEmpty()) {
            Toast.makeText(getApplication(), "You left IP Empty.", Toast.LENGTH_LONG).show();
        } else if (key.length() == 0) {
            Toast.makeText(getApplication(), "Please enter something...", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            RadioButton radioButton = (RadioButton) findViewById(R.id.rb_search_by_address);
            if (radioButton.isChecked()) {
                Urls = "http://" + ipNow + base_url + key + end_url_location;
            } else {
                Urls = "http://" + ipNow + base_url + key + end_url;
            }
            Log.e("URL", Urls);
            async_json_array();
        }
    }

    public void async_json_array() {
        aq.ajax(Urls, JSONArray.class, new AjaxCallback<JSONArray>() {
            public void callback(String url, JSONArray ja, AjaxStatus status) {
                if (ja != null) {
                    setSupportProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                    showResult(ja);
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

    static JSONArray resu = null;

    public void showResult(JSONArray jsonArray) {
        oname.clear();
        oadd.clear();
        for (int i = 0, count = jsonArray.length(); i < count; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                oname.add(jsonObject.getString(ORG));
                oadd.add(jsonObject.getString(ADDRESS));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        store_info_array = jsonArray;
        BaseAdapter2 adapter = new BaseAdapter2(MainActivity.this, oname, oadd);
        listView.setAdapter(adapter);
    }

    public void onRadioButtonClicked(View view) {
        searchBar.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resu != null) {
            showResult(resu);
            String s = "" + key;
            searchBar.setText(s);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutUs.class));
            return true;
        }
        if (id == R.id.currentIp) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.prompt_ip, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            input = (EditText) promptView.findViewById(R.id.currentIp);
            alertDialogBuilder.setView(promptView);
            //setting up alert Dialog here
            alertDialogBuilder
                    .setCancelable(false)
                    .setTitle("Enter Server IP")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG", "cancel");
                            dialog.cancel();
                        }
                    });
            //creating the alert dialog here
            AlertDialog alertDialog = alertDialogBuilder.create();
            Log.e("alert", "" + alertDialog);
            alertDialog.show();
            Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            button.setOnClickListener(new CustomListener(alertDialog));
        }

        return super.onOptionsItemSelected(item);
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            String ipvalue = input.getText().toString();
            if (!ipvalue.isEmpty()) {
                ipNow = input.getText().toString();
                Log.e("TAG", "**" + ipNow + "**");
                dialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "IP can't be empty", Toast.LENGTH_LONG).show();
            }
        }
    }
}
