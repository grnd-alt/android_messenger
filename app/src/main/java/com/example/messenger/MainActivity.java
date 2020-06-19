package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button submit = (Button) findViewById(R.id.submit_button);
        if (read_login()!= "nothing"){
            new check_login().execute(read_login());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new check_registration().execute();
                Intent newInt = new Intent(MainActivity.this,Name_list.class);
                MainActivity.this.startActivity(newInt);
            }
        });
    }
    private class check_login extends AsyncTask{

        private ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            return;
        }
        @Override
        protected Object doInBackground(Object ... objects){
            if (!objects[0].toString().equals("nothing")){
                try {
                    Socket s = new Socket("belprojects.hopto.org",2);
                    DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
                    outputStream.writeUTF("login;"+objects[0].toString()+';');
                    outputStream.flush();
                    DataInputStream inputStream = new DataInputStream(s.getInputStream());
                    String resp = String.valueOf(inputStream.readLine());
                    inputStream.close();
                    return resp;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "worked";
        }
        @Override
        protected void onPostExecute(Object result){
            Intent newInt = new Intent(MainActivity.this,Name_list.class);
            MainActivity.this.startActivity(newInt);
            return;
        }
    }
    private class check_registration extends AsyncTask {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = ProgressDialog.show(MainActivity.this,"","checking registration data",true,true);
            return;
        }

        @Override
        protected String doInBackground(Object ... objects) {
            a = 1;
            final String uname;
            TextView username = (TextView) findViewById(R.id.username_input);
            TextView password = (TextView) findViewById(R.id.password_input);
            try {
                Socket s = new Socket("belprojects.hopto.org", 2);
                DataOutputStream stream = new DataOutputStream(s.getOutputStream());
                String pass_hashed = String.valueOf(password.getText());
                stream.writeUTF("register;" + String.valueOf(username.getText()) + ';' + pass_hashed + ';');
                stream.flush();

                DataInputStream instream = new DataInputStream(s.getInputStream());
                String resp = String.valueOf(instream.readLine());
                stream.close();
                s.close();


                return resp +';'+ String.valueOf(username.getText())+';'+pass_hashed;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                return e.toString();
            }
            return "none;none";
        }
        @Override
        protected void onPostExecute(Object res){
            String result = res.toString();
            TextView username = (TextView)findViewById(R.id.username_input);
            username.setText(res.toString());
            Write_login(result.substring(0,result.indexOf(';')),result.substring(result.indexOf(';')));
            pd.dismiss();

        }
    }
    public void Write_login(String Username,String password){
        try{
            FileOutputStream fos = openFileOutput("data.txt",MODE_PRIVATE);
            String save = Username+password;
            fos.write(save.getBytes());
            fos.close();
            TextView username = (TextView) findViewById(R.id.username_input);
            username.setText("failed no task");
        } catch (IOException e) {
            TextView username = (TextView) findViewById(R.id.username_input);
            username.setText("failed the task");
            e.printStackTrace();
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
};
