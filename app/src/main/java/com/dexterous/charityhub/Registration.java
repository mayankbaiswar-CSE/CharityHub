package com.dexterous.charityhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mayank on 23-06-2015.
 */

public class Registration extends FragmentActivity implements OnDateSetListener {

    public static final String TAG = "Registration";
    EditText txt_name, txt_contact, txt_email, txt_charity_type, txt_requirement, txt_occupation, txt_address;
    TextView txt_date;
    public static String userBook = "http://" + MainActivity.ipNow + ":8080/CharityHub/userbook";
    int yy, mm, dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);
        setVars();
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        findViewById(R.id.btn_date_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(2015, 2030);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), TAG);
            }
        });
        findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSubmit();
            }
        });
        findViewById(R.id.btn_terms_and_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogCalling();
            }
        });
    }

    private void checkAndSubmit() {
        if (txt_name.getText().toString().equals("") ||
                txt_contact.getText().toString().equals("") ||
                txt_address.getText().toString().equals("") ||
                txt_occupation.getText().toString().equals("") ||
                txt_requirement.getText().toString().equals("") ||
                txt_charity_type.getText().toString().equals("") ||
                txt_email.getText().toString().equals("") ||
                txt_date.getText().toString().equals("")
                ) {
            Toast.makeText(getApplicationContext(), "fill all the details", Toast.LENGTH_LONG).show();
        } else {
            new UserBook().execute(userBook);
        }
    }

    //    ****Sendind Data*****
    class UserBook extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(userBook);
            List<NameValuePair> requestParams = new ArrayList<>();
            requestParams.add(new BasicNameValuePair(Complete_Info.NAME, txt_name.getText().toString()));
            requestParams.add(new BasicNameValuePair(Complete_Info.ORG_MOB_NUMBER, txt_contact.getText().toString()));
            requestParams.add(new BasicNameValuePair(Complete_Info.EMAIL, txt_email.getText().toString()));
            requestParams.add(new BasicNameValuePair("charitytype", txt_charity_type.getText().toString()));
            requestParams.add(new BasicNameValuePair("requirement", txt_requirement.getText().toString()));
            requestParams.add(new BasicNameValuePair("occupation", txt_occupation.getText().toString()));
            requestParams.add(new BasicNameValuePair("address", txt_address.getText().toString()));
            requestParams.add(new BasicNameValuePair("id", String.valueOf(Complete_Info.ORG_ID)));
            requestParams.add(new BasicNameValuePair("date", String.valueOf(yy)+"-"+String.valueOf(mm)+"-"+String.valueOf(dd)));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(requestParams));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
            }
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
            }
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.i("statusCode = ", String.valueOf(statusCode));
            if (statusCode == 200) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                "Successfull.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                "Something is wrong with your network.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }
    }
    //    ****Setting Variables****
    public void setVars() {
        txt_name = (EditText) findViewById(R.id.et_name);
        txt_charity_type = (EditText) findViewById(R.id.et_charity_type);
        txt_contact = (EditText) findViewById(R.id.et_contact);
        txt_email = (EditText) findViewById(R.id.et_email);
        txt_requirement = (EditText) findViewById(R.id.et_requirement);
        txt_occupation = (EditText) findViewById(R.id.et_occupation);
        txt_address = (EditText) findViewById(R.id.et_address);
        txt_date = (TextView) findViewById(R.id.txt_date);
    }

    //    *****Setting Terms And Conditions****
    public void alertDialogCalling() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false)
                .setTitle("Terms & Conditions")
                .setMessage(Complete_Info.TERMS_AND_CONDITIONS)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
        Log.e("dialogboxapper", "dialog");
    }

    //    ***** Retrieving Date ******
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //Toast.makeText(getApplicationContext(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        findViewById(R.id.btn_date_pick).setVisibility(View.GONE);
        txt_date.setVisibility(View.VISIBLE);
        txt_date.setText("Date Selected:" + year + "-" + month + "-" + day);
        yy = year;
        mm = month;
        dd = day;
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        findViewById(R.id.btn_change_date).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_change_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog1.setVibrate(false);
                datePickerDialog1.setYearRange(1985, 2028);
                datePickerDialog1.setCloseOnSingleTapDay(false);
                datePickerDialog1.show(getSupportFragmentManager(), TAG);
            }
        });
    }
}
