package com.e.softwaredesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    TextView resultName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent resultIntent = new Intent(this.getIntent());

        resultName = (TextView)findViewById(R.id.resultTXT);

        resultName.setText(resultIntent.getStringExtra("result")+"수업");

    }
}
