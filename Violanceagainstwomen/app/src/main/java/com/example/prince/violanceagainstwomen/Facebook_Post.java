package com.example.prince.violanceagainstwomen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Facebook_Post extends AppCompatActivity {
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook__post);
        post= (Button) findViewById(R.id.postbtn);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Post Complete",Toast.LENGTH_LONG).show();
            }
        });
    }
}
