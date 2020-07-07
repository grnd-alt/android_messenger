package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final Bundle extras = getIntent().getExtras();
        String contact = null;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (extras != null){
            contact = extras.getString("to_chat");
            String contact_id = extras.getString("to_chat_id");
            new del_from_to().execute(extras.getString("User_ID"),contact_id);
        }
        TextView contact_headline = (TextView) findViewById(R.id.contact_header);
        contact_headline.setText(contact);
        final Button send_message = (Button) findViewById(R.id.send_button);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView message_input = (TextView) findViewById(R.id.message_input);
                AsyncTask sender = new send_class();
                sender.execute(message_input.getText().toString());
                message_input.setText("");
            }
        });
    }
    private class del_from_to extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            Socket s = null;
            String to = objects[1].toString();
            String from = objects[0].toString();
            try {
                s = new Socket("belprojects.hopto.org",2);
                DataOutputStream outstream = new DataOutputStream(s.getOutputStream());
                outstream.writeUTF("delete;"+to+";"+from+";");
                outstream.flush();
                outstream.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class send_class extends AsyncTask{
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Socket sender = null;
            try {
                final Bundle extras = getIntent().getExtras();
                sender = new Socket("belprojects.hopto.org",2);
                DataOutputStream outputStream = new DataOutputStream(sender.getOutputStream());
                String contact_id = extras.getString("to_chat_id");
                outputStream.writeUTF("message;"+contact_id+';'+objects[0].toString()+';'+read_login().toString().split(";")[0]+";");
                outputStream.flush();
                outputStream.close();
                sender.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Object result){

        }
    }
    public  String read_login(){
        String returner = "nothing";
        FileInputStream fis = null;
        TextView username = (TextView) findViewById(R.id.username_input);
        try {
            fis = openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            fis.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returner;
    };
}