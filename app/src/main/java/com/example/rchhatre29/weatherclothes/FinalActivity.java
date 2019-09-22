package com.example.rchhatre29.weatherclothes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalActivity extends AppCompatActivity {

    private Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        restartButton = (Button) findViewById(R.id.restart);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restartFromBeginning = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(restartFromBeginning);
            }
        });
    }
}
