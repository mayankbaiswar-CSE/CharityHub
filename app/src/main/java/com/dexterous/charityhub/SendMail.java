package com.dexterous.charityhub;

/**
 * Created by mudit on 21-04-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMail extends Activity {

    EditText et_add_more;
    private TextView et_recipient, et_subject, et_message;
    private Button btn_sendmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        et_recipient = (TextView) findViewById(R.id.et_recipient);
        et_message = (TextView) findViewById(R.id.et_message);
        et_subject = (TextView) findViewById(R.id.et_subject);
        btn_sendmail = (Button) findViewById(R.id.btn_sendmail);
        et_add_more = (EditText) findViewById(R.id.et_add_more);
        et_message.setText("This is to notify you that I want to do donation in your organisation on 21st april,15 so please kindly make arragnments ");
        et_subject.setText("Message for organisation selection");
        btn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
//                et_recipient.setText("");
//                et_message.setText("");
//                et_subject.setText("");
            }
        });
    }

    protected void sendMail() {
        String[] recipients = {et_recipient.getText().toString()};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, et_subject.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, et_message.getText().toString() + ".\n" + et_add_more.getText().toString());
        try {

            // the user can choose the email client

            startActivity(Intent.createChooser(email, "Choose an email client from..."));


        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(SendMail.this, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this,AboutUs.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
