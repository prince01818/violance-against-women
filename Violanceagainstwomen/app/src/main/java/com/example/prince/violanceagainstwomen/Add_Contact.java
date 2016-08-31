package com.example.prince.violanceagainstwomen;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Add_Contact extends AppCompatActivity {
       Button save;
       EditText phone;
       String mobile_number="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__contact);
        Intent intent=getIntent();
        mobile_number=intent.getStringExtra("mobile");
         save = (Button) findViewById(R.id.submit);

        phone= (EditText) findViewById(R.id.mobile_numberET);
        phone.setText(mobile_number);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number = phone.getText().toString();
                Toast.makeText(getBaseContext(), "mbl" + mobile_number, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_Contact.this, MainActivity.class);
                intent.putExtra("mobile", mobile_number);
                startActivity(intent);
            }
        });

    }
}