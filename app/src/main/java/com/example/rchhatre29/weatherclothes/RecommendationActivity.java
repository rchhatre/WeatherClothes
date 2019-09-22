package com.example.rchhatre29.weatherclothes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView genderTextView;
    private TextView locationTextView;
    private TextView tempMornTextView;
    private TextView tempEveTextView;
    private TextView tempNightTextView;
    private TextView rainTextView;
    private ListView diffRecsListView;
    private Button finishButton;
    private String[] clothesByTime;
    private String[] recs = {"Morning Recommendation", "Evening Recommendation", "Night Recommendation"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        genderTextView = (TextView) findViewById(R.id.gender);
        locationTextView = (TextView) findViewById(R.id.location);
        tempMornTextView = (TextView) findViewById(R.id.tempMorn);
        tempEveTextView = (TextView) findViewById(R.id.tempEve);
        tempNightTextView = (TextView) findViewById(R.id.tempNight);
        rainTextView = (TextView) findViewById(R.id.rain);
        diffRecsListView = (ListView) findViewById(R.id.diffRecs);
        finishButton = (Button) findViewById(R.id.finish);

        clothesByTime = getIntent().getExtras().getStringArray("clothesByTime");

        // The gender of the user
        boolean gender = getIntent().getExtras().getBoolean("gender");
        if (gender) {
            genderTextView.setText("Gender: Male");
        } else {
            genderTextView.setText("Gender: Female");
        }

        // The location of the device
        String city = getIntent().getExtras().getString("city");
        locationTextView.setText("Location: " + city);

        // Sets the text of the temperatures
        tempMornTextView.setText("Morning Tempurature: " + getIntent().getExtras().getDouble("tempMorn") + " °F");
        tempEveTextView.setText("Evening Tempurature: " + getIntent().getExtras().getDouble("tempEve") + " °F");
        tempNightTextView.setText("Night Tempurature: " + getIntent().getExtras().getDouble("tempNight")+ " °F");

        // Whether there is rain or not
        boolean isRain = getIntent().getExtras().getBoolean("isRain");
        if(isRain) {
            rainTextView.setText("Rain? Yes");
        } else {
            rainTextView.setText("Rain? No");
        }

        // The recommendations in the morning, evening, and night
        ArrayAdapter<String> diffRecsAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, recs);
        diffRecsListView.setAdapter(diffRecsAdapter);
        diffRecsListView.setOnItemClickListener(this);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTheFinalActivity = new Intent(getApplicationContext(), FinalActivity.class);
                startActivity(goToTheFinalActivity);
            }
        });
    }

    /**
     * Transfers the correct clothing recommendation to the ClothesActivity
     * @param parent
     * @param view
     * @param position the position of the button pressed in the list view
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent moveToClothes = new Intent(getApplicationContext(), ClothesActivity.class);
        moveToClothes.putExtra("clothes", clothesByTime[position]);
        startActivity(moveToClothes);
    }
}