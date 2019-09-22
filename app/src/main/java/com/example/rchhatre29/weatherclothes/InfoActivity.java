package com.example.rchhatre29.weatherclothes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class InfoActivity extends AppCompatActivity {

    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button nextButton;
    private boolean isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        maleRadioButton = (RadioButton) findViewById(R.id.male);
        femaleRadioButton = (RadioButton) findViewById(R.id.female);
        nextButton = (Button) findViewById(R.id.next);

        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = true;
            }
        });

        femaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTheFashionActivity = new Intent(getApplicationContext(), LocationActivity.class);

                goToTheFashionActivity.putExtra("gender", isMale);

                startActivity(goToTheFashionActivity);
            }
        });
    }
}
