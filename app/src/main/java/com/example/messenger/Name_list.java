package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Name_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> contact_list_items = new ArrayList<String>();
        ArrayAdapter<String>adapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        StringBuilder contactssb = read_contacts();
        if (contactssb != null){
            String[] contacts = contactssb.toString().split("\n");
            LinearLayout container = (LinearLayout) findViewById(R.id.container);
            int counter = 0;
            for (String s:contacts) {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(p);
                view.setText(s+"\n");
                view.setId(counter);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chat = new Intent(Name_list.this, com.example.messenger.chat.class);
                        Name_list.this.startActivity(chat);
                    }
                });
                container.addView(view);
                counter++;
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