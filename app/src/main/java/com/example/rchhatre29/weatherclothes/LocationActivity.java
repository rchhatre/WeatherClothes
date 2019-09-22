package com.example.rchhatre29.weatherclothes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    private TextView currentTextView;
    private Button currentButton;
    //private EditText otherLocationEditText;
    //private Button otherButton;
    //private ListView cityListView;
    private String city;
    private boolean isMale;
    private double tempMorn;
    private double tempEve;
    private double tempNight;
    private boolean isRain;
    private DataWeather[] dataList;
    private String[] clothesByTime;
    private String WEATHER_API_KEY = "460f4a87dbe0eb9bf0eecbc7e0c4db0e";
    //private List<City> searchResult = new ArrayList<>();
    //private String search_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        currentTextView = (TextView) findViewById(R.id.currentLocation);
        currentButton = (Button) findViewById(R.id.useCurrentLocation);
        //otherLocationEditText = (EditText) findViewById(R.id.otherLocation);
        //otherButton = (Button) findViewById(R.id.useOtherLocation);
        //cityListView = (ListView) findViewById(R.id.cityList);

        isMale = getIntent().getExtras().getBoolean("gender");

        createData();
        startServiceApp();

        // Listener for the button that says "Use Current Location"
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTheRecActivity = new Intent(getApplicationContext(), RecommendationActivity.class);

                goToTheRecActivity.putExtra("gender", isMale);

                goToTheRecActivity.putExtra("tempMorn", tempMorn);
                goToTheRecActivity.putExtra("tempEve", tempEve);
                goToTheRecActivity.putExtra("tempNight", tempNight);
                goToTheRecActivity.putExtra("isRain", isRain);
                goToTheRecActivity.putExtra("city", city);
                goToTheRecActivity.putExtra("clothesByTime", clothesByTime);

                startActivity(goToTheRecActivity);
            }
        });

        /*
        otherLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "Listener is working.");
                otherLocationEditText.setVisibility(View.VISIBLE);
                String pattern = otherLocationEditText.getEditableText().toString();
                JSONSearchTask task = new JSONSearchTask(pattern);
                task.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otherLocationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("TAG", "Listener is working.");
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.setVisibility(View.VISIBLE);
                    String pattern = otherLocationEditText.getEditableText().toString();
                    JSONSearchTask task = new JSONSearchTask(pattern);
                    task.execute();
                    return true;
                }
                return false;
            }
        });

        // Listener for the button that says "Use Other Location"
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTheRecActivity = new Intent(getApplicationContext(), RecommendationActivity.class);

                goToTheRecActivity.putExtra("gender", isMale);

                startActivity(goToTheRecActivity);
            }
        });
        */
    }

    /**
     * Adds the clothing data to an array for the morning evening and night tempurature.
     * @param morn the temperature in the morning
     * @param eve the temperature in the evening
     * @param night the temperature at night
     * @param isRain whether there is rain or not
     */
    private void addData(double morn, double eve, double night, boolean isRain) {
        clothesByTime = new String[3];

        String mornResult = searchData(morn,isRain,isMale);
        String eveningResult = searchData(eve,isRain,isMale);
        String nightResult = searchData(night,isRain,isMale);

        clothesByTime[0] = "Morning Recommendation:\n\n\t" + mornResult;
        clothesByTime[1] = "Evening Recommendation:\n\n\t" + eveningResult;
        clothesByTime[2] = "Night Recommendation:\n\n\t"   + nightResult;
    }

    /**
     * Returns the correct info using the temperature, rain, and gender
     * @param temp the tempurature
     * @param rain whether there is rain or not
     * @param isBoy whether the user is a boy or not
     * @return the info in the data weather
     */
    private String searchData(double temp, boolean rain, boolean isBoy){
        String result ="";

        for(DataWeather dataAux : dataList) {
            if (dataAux.getMinTemp() <= temp && dataAux.getMaxTemp() > temp) {
                if (dataAux.isRain() == rain) {
                    if (isBoy) {
                        result = dataAux.getMale();
                    } else {
                        result = dataAux.getFemale();
                    }
                }
            }
        }

        return result;
    }

    /**
     * Creates all the possible data with and without rain, +/-10°F, and the possible clothes for that weather
     */
    private void createData(){
        dataList = new DataWeather[16];

        dataList[0] = new DataWeather(-1000 , 0, true, "Pants, Long sleeve Shirt, Coat, Hat","Pants, Long sleeve Shirt, Jacket, Hat");
        dataList[1] = new DataWeather(0 , 30, true, "Pants, Long sleeve Shirt, Coat, Hat","Pants, Long sleeve Shirt, Jacket, Hat");
        dataList[2] = new DataWeather(30 , 40, true, "Pants, Long sleeve Shirt, Jacket","Pants, Long sleeve Shirt, Jacket");
        dataList[3] = new DataWeather(40 , 50, true, "Pants, Long sleeve Shirt","Pants, Long sleeve Shirt");
        dataList[4] = new DataWeather(50 , 60, true, "Pants, Short Sleeve Shirt","Pants, Short Sleeve Shirt");
        dataList[5] = new DataWeather(60 , 70, true, "Shorts, Short Sleeve Shirt","Shorts, Short Sleeve Shirt");
        dataList[6] = new DataWeather(70 , 80, true, "Shorts, Short Sleeve Shirt","Shorts, Short Sleeve Shirt");
        dataList[7] = new DataWeather(80 , 1000, true, "Shorts, Tank Top","Shorts, Tank Top");

        dataList[8] = new DataWeather(-1000 , 0, false, "Pants, Long sleeve Shirt, Coat, Hat","Pants, Long sleeve Shirt, Jacket, Hat");
        dataList[9] = new DataWeather(0 , 30, false, "Pants, Long sleeve Shirt, Coat, Hat","Pants, Long sleeve Shirt, Jacket, Hat");
        dataList[10] = new DataWeather(30 , 40, false, "Pants, Long sleeve Shirt, Jacket","Pants, Long sleeve Shirt, Jacket");
        dataList[11] = new DataWeather(40 , 50, false, "Pants, Long sleeve Shirt","Pants, Long sleeve Shirt");
        dataList[12] = new DataWeather(50 , 60, false, "Pants, Short Sleeve Shirt","Pants, Short Sleeve Shirt");
        dataList[13] = new DataWeather(60 , 70, false, "Shorts, Short Sleeve Shirt","Shorts, Short Sleeve Shirt");
        dataList[14] = new DataWeather(70 , 80, false, "Shorts, Short Sleeve Shirt","Shorts, Short Sleeve Shirt");
        dataList[15] = new DataWeather(80 , 1000, false, "Shorts, Tank Top","Shorts, Tank Top");

        Log.d("troubleshoot", "Created the data.");
    }

    /**
     * Gets the weather from the specified city
     * @param cityName the city
     */
    private void getWeather(String cityName){
        DownloadTask downloadTask = new DownloadTask();

        Log.d("troubleshoot", "Gets the weather.");

        String encodedCityName = null;
        try {
            encodedCityName = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + encodedCityName + "&cnt=2&units=imperial&appid=" + WEATHER_API_KEY;
        downloadTask.execute(url);
    }

    /**
     * Gets the weather by parsing the JSON file, getting the temperature in the morning, evening,
     * and night, and then using that data to add it to the clothing recommendation
     */
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            Log.d("troubleshoot", "doInBackground in DownloadTask");

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
                return "Failed";

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("troubleshoot", "onPostExecute in DownloadTask. Data:" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray ja1 = jsonObject.getJSONArray("list");
                Log.d("troubleshoot", "ja1 = " + ja1);
                JSONObject jsToday = ja1.getJSONObject(0);
                JSONArray jsTodayWeather = jsToday.getJSONArray("weather");
                JSONObject mainTodayWeather = jsTodayWeather.getJSONObject(0);
                String mainToday = mainTodayWeather.getString("main");

                isRain = false;

                if(mainToday.equals("Rain")){
                    isRain = true;
                }
                Log.d("troubleshoot", "mainToday " + mainToday);

                JSONObject jsTodayTemp = jsToday.getJSONObject("temp");

                Log.d("troubleshoot", "jsTodayTemp = " + jsTodayTemp);

                if ( jsTodayTemp != null ) {
                    tempMorn = jsTodayTemp.getDouble("morn");
                    tempEve = jsTodayTemp.getDouble("eve");
                    tempNight = jsTodayTemp.getDouble("night");
                }

                addData(tempMorn,tempEve,tempNight,isRain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts the process by finding the location, then getting the weather at that location
     * and then displaying it
     */
    private void startServiceApp() {
        try {

            FetchCoordinates fetchCoordinates = new FetchCoordinates();
            fetchCoordinates.execute();
        } catch (Exception error) {
        }

    }

    /**
     * Finds the coordinates of the device
     */
    public class FetchCoordinates extends AsyncTask<String, Void, String> {

        private double lati = 0.0;
        private double longi = 0.0;

        private LocationManager mLocationManager;
        private CoordinateListener mCoordinateListener;

        @Override
        protected void onPreExecute() {
            mCoordinateListener = new CoordinateListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Log.d("troubleshoot", "onPreExecute in FetchCoordinates");

            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mCoordinateListener);
        }

        @Override
        protected void onCancelled() {
            Log.i("troubleshoot", "Cancelled by user!");
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(mCoordinateListener);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("troubleshoot", "onPostExecute in FetchCoordinates");
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> listAdresses = geocoder.getFromLocation(lati, longi, 1);

                if(listAdresses != null && listAdresses.size() > 0){
                    Address address = listAdresses.get(0);

                    city = "" + address.getLocality();

                    currentTextView.setText("Your Current Location: " + city);
                    getWeather(city);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            while (this.lati == 0.0) {
                Log.d("troubleshoot", "latitude");
            }
            return null;
        }

        /**
         * Updates the coordinates of the device if it changes location
         */
        public class CoordinateListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                Log.d("troubleshoot", "At CoordinateListener");

                try {
                    lati = location.getLatitude();
                    longi = location.getLongitude();

                    Log.d("troubleshoot","lati = " + lati);
                    Log.d("troubleshoot","longi = " + longi);
                } catch (Exception e) {
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");
            }
        }
    }

    /*
    private class JSONSearchTask extends AsyncTask<Void, Void, Void> {

        String search_url = "http://api.openweathermap.org/data/2.5/find?mode=json&type=like&cnt=10&APPID=" + WEATHER_API_KEY + "&q=";

        String search_query;

        JSONSearchTask(String item){

            try {
                search_item = URLEncoder.encode(item, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            search_query = search_url + search_item;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                searchResult = getCityList(sendQuery(search_query));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            otherButton.setEnabled(false);
            otherButton.setText("Wait...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {

            otherButton.setEnabled(true);
            otherButton.setText("Search");

            ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<City>(LocationActivity.this,
                    android.R.layout.simple_list_item_1, searchResult);
            cityListView.setAdapter(cityArrayAdapter);

            super.onPostExecute(result);
        }

    }

    private String sendQuery(String query) throws IOException {
        String result = "";

        URL searchURL = new URL(query);

        HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL.openConnection();

        if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader,
                    8192);

            String line = null;
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }

            bufferedReader.close();
        }
        return result;
    }

    public static List<City> getCityList(String data) throws JSONException {
        JSONObject jObj = new JSONObject(data);
        JSONArray jArr = jObj.getJSONArray("list");

        List<City> cityList = new ArrayList<City>();

        for (int i=0; i < jArr.length(); i++) {
            JSONObject obj = jArr.getJSONObject(i);

            String name = obj.getString("name");
            String id = obj.getString("id");

            JSONObject sys = obj.getJSONObject("sys");
            String country = sys.getString("country");

            City c = new City(id, name, country);

            cityList.add(c);
        }

        return cityList;
    }
    */
}