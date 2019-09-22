package com.example.rchhatre29.weatherclothes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ClothesActivity extends AppCompatActivity {

    private TextView clothesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        clothesTextView = (TextView) findViewById(R.id.rec);

        clothesTextView.setText(getIntent().getExtras().getString("clothes"));
    }
}
