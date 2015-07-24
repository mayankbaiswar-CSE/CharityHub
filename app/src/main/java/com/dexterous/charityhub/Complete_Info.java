package com.dexterous.charityhub;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexterous.charityhub.font.FontelloTextView;
import com.github.clans.fab.FloatingActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Complete_Info extends ActionBarActivity {

    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String NEW = "new";
    public static final String ORG_MOB_NUMBER = "org_mobile_no";
    public static final String CONTACT_PERSON = "person_name";
    public static final String CONTATCT_PERSON_PHONE = "person_mobile_no";
    public static final String EMAIL = "email";
    public static final String ESTIMATE_PEOPLE = "estimates_people";
    public static final String TC = "terms_and_contions";
    public static String TERMS_AND_CONDITIONS;
    public String nextUrl = "http://" + MainActivity.ipNow + ":8080/CharityHub/getnewsfeed?id=";
    public static String receivedData;
    private static final String TAG = "CompleteInfo";

    FontelloTextView complete_name;
    FontelloTextView complete_address;
    FontelloTextView complete_phone_org;
    FontelloTextView complete_contact_person_name;
    FontelloTextView complete_contact_person_phone;
    FontelloTextView complete_contact_person_mail;
    FontelloTextView complete_estimate;

    FloatingActionButton floatingActionButton;

    public static int ORG_ID;
    public static JSONArray NEEDS;
    String completeResponse = "", builder = "";
    public static String resumeJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            finish();
            return;
        } else {
            ORG_ID = bundle.getInt("id");
        }
        Log.i(TAG, String.valueOf(ORG_ID));
        setVars();
        nextUrl = nextUrl + ORG_ID;
        if (nextUrl.contains(" ")) {
            nextUrl = nextUrl.replaceAll(" ", "%20");
        }
//        ***************** LOG.i *****************
        Log.i(TAG, nextUrl);
        new CompleteData().execute();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NeedsList.class));
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            setData(resumeJSON);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.i(TAG, e.toString());
//        }
//    }

    class CompleteData extends AsyncTask<String, Void, String> {
    ProgressBar progressBar = (ProgressBar) findViewById(R.id.complete_progress);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Inside AsynTask",
//                    Toast.LENGTH_LONG
//            ).show();
            Log.i(TAG, "inside AsyncTask");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "starting doInBackground");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(nextUrl);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                Log.i(TAG, httpResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
            }
            InputStream inputStream = null;
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                httpEntity = httpResponse.getEntity();
                try {
                    inputStream = httpEntity.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, e.toString());
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    while ((completeResponse = bufferedReader.readLine()) != null) {
                        builder = builder + completeResponse;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, e.toString());
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                "Problem in network.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
            return builder;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(
//                    getApplicationContext(),
//                    s,
//                    Toast.LENGTH_LONG
//            ).show();
            progressBar.setVisibility(View.GONE);
            try {
                setData(s);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            Log.i(TAG, "AsyncTask Done.");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public void setVars() {
        complete_name = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_name);
        complete_address = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_address);
        complete_phone_org = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_phone_org);
        complete_contact_person_name = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_contact_person_name);
        complete_contact_person_phone = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_contact_person_phone);
        complete_contact_person_mail = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_contact_person_mail);
        complete_estimate = (com.dexterous.charityhub.font.FontelloTextView) findViewById(R.id.complete_estimate);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.needs_button);
    }

    public void setData(String response) throws JSONException {
        Log.i(TAG, response);
        resumeJSON = response;
        JSONObject jsonObject = new JSONObject(response);
        complete_name.setText(jsonObject.getString(NAME));
        complete_address.setText(jsonObject.getString(CITY));
        complete_phone_org.setText(String.valueOf(jsonObject.getLong(ORG_MOB_NUMBER)));
        complete_contact_person_name.setText(jsonObject.getString(CONTACT_PERSON));
        complete_contact_person_phone.setText(String.valueOf(jsonObject.getLong(CONTATCT_PERSON_PHONE)));
        complete_contact_person_mail.setText(jsonObject.getString(EMAIL));
        complete_estimate.setText(String.valueOf(jsonObject.getInt(ESTIMATE_PEOPLE)));
        TERMS_AND_CONDITIONS = jsonObject.getString(TC);
        NEEDS = jsonObject.getJSONArray(NEW);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete__info, menu);
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
