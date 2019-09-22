package com.example.rchhatre29.weatherclothes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button letsGetStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letsGetStartedButton = (Button) findViewById(R.id.letsGetStarted);

        letsGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTheInfoActivity = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(goToTheInfoActivity);
            }
        });
    }
}
