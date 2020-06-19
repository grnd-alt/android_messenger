package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class home_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView message = (TextView) findViewById(R.id.MessagInput);
                new sending().execute();
            }
        });
    }

    private class sending extends AsyncTask {
        private ProgressDialog pd;
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            pd = ProgressDialog.show(home_screen.this,"","Loading",true,true);
            return;
        }
        @Override
        protected Void doInBackground(Object... objects) {
            TextView message = (TextView) findViewById(R.id.MessagInput);
            try {
                Socket s = new Socket("belprojects.hopto.org", 2);
                DataOutputStream stream = new DataOutputStream(s.getOutputStream());
                stream.writeUTF("message;123123123;"+String.valueOf(message.getText())+';');
                stream.flush();
                stream.close();
                s.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            };
            pd.dismiss();
            message.setText("");
            return null;
        }
    }
}