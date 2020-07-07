package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Name_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> contact_list_items = new ArrayList<String>();
        ArrayAdapter<String>adapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        StringBuilder contactssb = read_contacts();
        final Bundle extras = new Bundle(getIntent().getExtras());
        String User_id = extras.getString("User_ID");
        TextView User_ID_view = (TextView) findViewById(R.id.user_id_field);
        User_ID_view.setText(User_ID_view.getText().toString()+User_id);
        if (contactssb != null){
            String[] contacts = contactssb.toString().split("\n");
            LinearLayout container = (LinearLayout) findViewById(R.id.container);
            for (String s:contacts) {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(p);
                view.setText(s.split(";")[1]+"\n");
                view.setId(Integer.parseInt(s.split(";")[0]));
                final String contact =s.split(";")[1];
                final String contact_id = s.split(";")[0];
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chat = new Intent(Name_list.this, com.example.messenger.chat.class);
                        chat.putExtra("to_chat",contact);
                        chat.putExtra("to_chat_id",contact_id);
                        final Bundle extras = getIntent().getExtras();
                        chat.putExtra("User_ID",extras.getString("User_ID").toString());
                        Name_list.this.startActivity(chat);
                    }
                });

                container.addView(view);
            }
        }
        else{
            LinearLayout container = (LinearLayout) findViewById(R.id.container);
            TextView view = new TextView(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(p);
            view.setText("Your contacts will be appearing right here!\n");
            view.setId(1);
            container.addView(view);
        }
        Button new_contact = (Button) findViewById(R.id.new_contact_button);
        new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_contact_intent = new Intent(Name_list.this,Add_contact.class);
                Name_list.this.startActivity(add_contact_intent);
            }
        });
        final Handler handler = new Handler();
        final Runnable[] runnables = new Runnable[1];
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                new search_message().execute();
                handler.postDelayed(runnables[0],2000);
            }
        };
        runnables[0] = runnable;
        handler.post(runnable);

        //new search_message().execute();
    }
    class search_message extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Socket s = new Socket("belprojects.hopto.org",2);
                DataOutputStream outstream = new DataOutputStream(s.getOutputStream());
                final Bundle extras = getIntent().getExtras();
                String User_ID = extras.getString("User_ID");
                outstream.writeUTF("message request;"+User_ID+";");
                outstream.flush();
                DataInputStream instream = new DataInputStream(s.getInputStream());
                String line;
                ArrayList <String[]> messages = new ArrayList<String[]>();
                String firstline = instream.readLine().toString();
                if (firstline.equals("start")) {
                    while (true){
                        if ((line = instream.readLine().toString()).equals("end")){
                            return messages;
                        }
                        else{
                            messages.add(line.split(";"));
                        }
                    }
                }
                else{
                    return "nothing";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "nothing";
        }
        @Override
        protected void onPostExecute(Object result){
            TextView view;
            String[] contacts = read_contacts().toString().split("\n");
            if (result.toString().equals("nothing")) {
                System.out.println("hello here");
                StringBuilder contactssb = read_contacts();
                if (contacts!=null){
                    for(String contact:contacts){
                        view = (TextView) findViewById(Integer.parseInt(contact.split(";")[0]));
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }
                };


            }
            else{
                ArrayList<String[]> messages = (ArrayList<String[]>) result;
                ArrayList<String> messages_sender= new ArrayList<String>();
                for (String[] i:messages){
                    messages_sender.add(i[0]);
                }
                for (String contact:contacts){
                    if (messages_sender.contains(contact.split(";")[0])== true){
                        view = (TextView) findViewById(Integer.parseInt(contact.split(";")[0].toString()));
                        //view.setText(m[2]);
                        view.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        view = (TextView) findViewById(Integer.parseInt(contact.split(";")[0].toString()));
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }
                };
                /*for (String[] m:messages){
                    try {
                        view = (TextView) findViewById(Integer.parseInt(m[0]));
                        //view.setText(m[2]);
                        view.setBackgroundColor(Color.GREEN);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }*/

            }
        }
    }
    public StringBuilder read_contacts(){
        FileInputStream fis = null;
        TextView username = (TextView) findViewById(R.id.username_input);
        try {
            fis = openFileInput("contacts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line+"\n");
            }
            fis.close();
            return sb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    };
}