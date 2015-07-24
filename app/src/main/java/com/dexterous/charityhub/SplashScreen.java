package com.dexterous.charityhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by mudit on 20-04-2015.
 */
class ApplicationUtility {

    public ApplicationUtility() {
    }

    protected boolean checkConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}

public class SplashScreen extends Activity implements OnClickListener {

    AlertDialog alertDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        Timer t = new Timer();
//        boolean checkConnection=new ApplicationUtility().checkConnection(this);
//        if (checkConnection) {
//            t.schedule(new splash(), 3000);
//        } else {
//                alertDialogCalling();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer t = new Timer();
        boolean checkConnection=new ApplicationUtility().checkConnection(this);
        if (checkConnection) {
            t.schedule(new splash(), 3000);
        } else {
            alertDialogCalling();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(alertDialog != null)
        alertDialog.dismiss();
    }

    class splash extends TimerTask {

        @Override
        public void run() {
            Intent i = new Intent(SplashScreen.this,MainActivity.class);
            finish();
            startActivity(i);
        }
    }

    public void alertDialogCalling(){
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Internet not Available")
                .setMessage("Do u want to Connect to Internet??")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        System.exit(0);
                    }
                })
                .setIcon(R.mipmap.error)
                .show();
        Log.e("dialogboxapper", "dialog");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_settings:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case R.id.btn_exit:
                break;
            default:
                break;
        }
    }
}

