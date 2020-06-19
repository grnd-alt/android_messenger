package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.jar.Attributes;

public class Name_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        Button new_contact = (Button) findViewById(R.id.new_contact_button);
        new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_contact_intent = new Intent(Name_list.this,Add_contact.class);
                Name_list.this.startActivity(add_contact_intent);
            }
        });
    }
}