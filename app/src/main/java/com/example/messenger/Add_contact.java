package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Add_contact extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Button search_contact = (Button) findViewById(R.id.search);
        search_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask search = new searcher();
                search.execute();
            }
        });
        final Button send_request = (Button) findViewById(R.id.send);
        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (send_request.getVisibility() == View.VISIBLE){
                    TextView userdis = (TextView) findViewById(R.id.user_name_display);
                    TextView userid = (TextView) findViewById(R.id.personid_input);
                    AsyncTask request = new request();
                    request.execute(userid.getText().toString()+';'+userdis.getText().toString());
                }
                else{
                    return;
                }
            }
        });
    }
    private class request extends AsyncTask{
        ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            pd = pd.show(Add_contact.this,"","checking registration data",true,true);
            return;
        }
        @Override
        protected Object doInBackground(Object ... objects){
            add_contact((String) objects[0]);
            pd.dismiss();
            return null;
        }
        @Override
        protected void onPostExecute(Object result){
            pd.dismiss();
            return;
        }
    }
    private class searcher extends AsyncTask{
        ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            //super.onPreExecute();
            pd = ProgressDialog.show(Add_contact.this,"","doing the stuff",true,true);
            return;
        }
        @Override
        protected Object doInBackground(Object ... objects){
            String resp = null;
            TextView UserID = (TextView) findViewById(R.id.personid_input);
            try {
                Socket s = new Socket("belprojects.hopto.org",2);
                DataOutputStream stream = new DataOutputStream(s.getOutputStream());
                stream.writeUTF("search;"+String.valueOf(UserID.getText())+';');
                stream.flush();
                DataInputStream streamin = new DataInputStream(s.getInputStream());

                resp = String.valueOf(streamin.readLine());
                streamin.close();
                stream.close();
                s.close();
            } catch (IOException e) {
                resp = String.valueOf(e);
                e.printStackTrace();
            }
            pd.dismiss();
            return resp;
        }
        @Override
        protected void onPostExecute(Object result){
            TextView user_display = (TextView) findViewById(R.id.user_name_display);
            user_display.setText(result.toString());
            Button search_input = (Button) findViewById(R.id.send);
            search_input.setVisibility(View.VISIBLE);
            pd.dismiss();
        }
    }
    public void add_contact(String Username){
        try{
            FileOutputStream fos = openFileOutput("contacts.txt", Context.MODE_APPEND);
            String save = Username;
            fos.write(save.getBytes());
            fos.write("\r\n".getBytes());
            fos.close();
            TextView username = (TextView) findViewById(R.id.username_input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}