package clima.clima;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Current_weather extends AppCompatActivity {



    // Request Codes:
    final int REQUEST_CODE = 123; // Request Code for permission request callback
    final int NEW_CITY_CODE = 456; // Request code for starting new activity for result callback

    // Base URL for the OpenWeatherMap API. More secure https is a premium feature =(
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // Don't want to type 'Clima' in all the logs, so putting this in a constant here.
    final String LOGCAT_TAG = "Clima";

    // Set LOCATION_PROVIDER here. Using GPS_Provider for Fine Location (good for emulator):
    // Recommend using LocationManager.NETWORK_PROVIDER on physical devices (reliable & fast!)
    final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    // Member Variables:
    boolean mUseLocation = true;
    TextView mCityLabel;
    TextView mTemperatureLabel;
    WebView myweb;
    TextView mDate;
    TextView mTemp1,mTemp2,mTemp3,mTemp4;
    TextView mDay1,mDay2,mDay3,mDay4;
    private String longitude;
    private String latitude;
    ImageView mIcon1, mIcon2, mIcon3, mIcon4;
    ConstraintLayout mCur;
    TextToSpeech t1;
    String tospeak;


    // Declaring a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    private static final int TODAY = 0;
    private static final int TOMORROW = 1;
    private static final int DAY_AFTER_TOMORROW = 2;
    private static final int THREE_DAYS_FROM_TODAY = 3;
    private static final int FOUR_DAYS_FROM_TODAY = 4;
    private static final int FIVE_DAYS_FROM_TODAY = 5;
    private static final int SIX_DAYS_FROM_TODAY = 6;
    OpenWeatherMapHelper helper;
    private double la,lo;
    private Swipe swipe;
    String value;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        mCityLabel = findViewById(R.id.current_Location);
        mTemperatureLabel = findViewById(R.id.current_temp);
        myweb = findViewById(R.id.webview);
        myweb.setBackgroundColor(Color.TRANSPARENT);
        mDate = findViewById(R.id.date);
        mTemp1 = findViewById(R.id.temp1);
        mTemp2 = findViewById(R.id.temp2);
        mTemp3 = findViewById(R.id.temp3);
        mTemp4 = findViewById(R.id.temp4);
        mDay1 = findViewById(R.id.day1);
        mDay2 = findViewById(R.id.day2);
        mDay3 = findViewById(R.id.day3);
        mDay4 = findViewById(R.id.day4);
        mIcon1 = findViewById(R.id.day1icon);
        mIcon2 = findViewById(R.id.day2icon);
        mIcon3 = findViewById(R.id.day3icon);
        mIcon4 = findViewById(R.id.day4icon);


        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        mDate.setText(date);

        Current_weather calSample = new Current_weather();
        mDay1.setText(getNameOfDayOfWeek(TOMORROW));
        mDay2.setText(calSample.getNameOfDayOfWeek(DAY_AFTER_TOMORROW));
        mDay3.setText(calSample.getNameOfDayOfWeek(THREE_DAYS_FROM_TODAY));
        mDay4.setText(calSample.getNameOfDayOfWeek(FOUR_DAYS_FROM_TODAY));


        helper = new OpenWeatherMapHelper();
        helper.setApiKey(getString(R.string.API_KEY));
        helper.setUnits(Units.METRIC);
        mCur = findViewById(R.id.relativeLayout);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            value = extras.getString("KEY");
        }



        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if (value != null){
                    switch(value) {
                        case "Bangla (India)":
                            t1.setLanguage(Locale.forLanguageTag("ben"));
                            break;
                        case "English (India)":
                            t1.setLanguage(new Locale("en", "IN"));
                            break;
                        case "English (United States)":
                            t1.setLanguage(Locale.US);
                            break;

                        case "Hindi (India)":
                            t1.setLanguage(Locale.forLanguageTag("hin"));

                            break;
                        case "Italian (Italy)":
                            t1.setLanguage(Locale.forLanguageTag("ita"));
                            break;
                        case "Nepali (Nepal)":
                            t1.setLanguage(Locale.forLanguageTag("nep"));
                            break;
                        case "Slovak (Slovakia)":
                            t1.setLanguage(Locale.forLanguageTag("slo"));
                            break;
                        case "Thai (Thailand)":
                            t1.setLanguage(Locale.forLanguageTag("tha"));
                            break;

                    }}

                    else

                        t1.setLanguage(Locale.US);
                }
            }
        });

        swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent intent=new Intent(Current_weather.this,NewCity.class);
                startActivity(intent);

                return true;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {
                Intent intent=new Intent(Current_weather.this,select_lang.class);
                startActivity(intent);


            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public boolean onSwipedUp(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public boolean onSwipedDown(MotionEvent event) {
                return false;
            }
        });



    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGCAT_TAG, "onResume() called");
        if(mUseLocation) getWeatherForCurrentLocation();
    }



    public String getNameOfDayOfWeek(int noOfDaysFromToday){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, noOfDaysFromToday);
        String name = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        return name;
    }


    // Callback received when a new city name is entered on the second screen.
    // Checking request code and if result is OK before making the API call.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOGCAT_TAG, "onActivityResult() called");

        if (requestCode == NEW_CITY_CODE) {
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("City");
                Log.d(LOGCAT_TAG, "New city is " + city);

                mUseLocation = false;
                getWeatherForNewCity(city);
            }
        }
    }

    // Configuring the parameters when a new city has been entered:
    private void getWeatherForNewCity(String city) {
        Log.d(LOGCAT_TAG, "Getting weather for new city");
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);

        letsDoSomeNetworking(params);
    }


    // Location Listener callbacks here, when the location has changed.
    private void getWeatherForCurrentLocation() {

        Log.d(LOGCAT_TAG, "Getting weather for current location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(LOGCAT_TAG, "onLocationChanged() callback received");
               longitude = String.valueOf(location.getLongitude());
               latitude = String.valueOf(location.getLatitude());

                la = Double.parseDouble(latitude);
                lo = Double.parseDouble(longitude);
                Log.d(LOGCAT_TAG, "longitude is: " + longitude);
                Log.d(LOGCAT_TAG, "latitude is: " + latitude);

                // Providing 'lat' and 'lon' (spelling: Not 'long') parameter values
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
                letsDoForecast(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Log statements to help you debug your app.
                Log.d(LOGCAT_TAG, "onStatusChanged() callback received. Status: " + status);
                Log.d(LOGCAT_TAG, "2 means AVAILABLE, 1: TEMPORARILY_UNAVAILABLE, 0: OUT_OF_SERVICE");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(LOGCAT_TAG, "onProviderEnabled() callback received. Provider: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(LOGCAT_TAG, "onProviderDisabled() callback received. Provider: " + provider);
            }
        };

        // This is the permission check to access (fine) location.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        // Some additional log statements to help you debug
        Log.d(LOGCAT_TAG, "Location Provider used: "
                + mLocationManager.getProvider(LOCATION_PROVIDER).getName());
        Log.d(LOGCAT_TAG, "Location Provider is enabled: "
                + mLocationManager.isProviderEnabled(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Last known location (if any): "
                + mLocationManager.getLastKnownLocation(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Requesting location updates");


        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        mLocationManager.getLastKnownLocation(LOCATION_PROVIDER);

    }

    // This is the callback that's received when the permission is granted (or denied)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking against the request code we specified earlier.
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOGCAT_TAG, "onRequestPermissionsResult(): Permission granted!");

                // Getting weather only if we were granted permission.
                getWeatherForCurrentLocation();
            } else {
                Log.d(LOGCAT_TAG, "Permission denied =( ");
            }
        }

    }


    // This is the actual networking code. Parameters are already configured.
    private void letsDoSomeNetworking(RequestParams params) {

        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(LOGCAT_TAG,"letsdo");

        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(Current_weather.this, "Request Failed", Toast.LENGTH_SHORT).show();


            }

        });
    }

    private void letsDoForecast(RequestParams params) {

        helper.getThreeHourForecastByGeoCoordinates(la, lo, new OpenWeatherMapHelper.ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                double t1 = threeHourForecast.getThreeHourWeatherArray().get(7).getMain().getTemp();
                double t2 = threeHourForecast.getThreeHourWeatherArray().get(15).getMain().getTemp();
                double t3 = threeHourForecast.getThreeHourWeatherArray().get(31).getMain().getTemp();
                double t4 = threeHourForecast.getThreeHourWeatherArray().get(35).getMain().getTemp();
                long id1 = threeHourForecast.getThreeHourWeatherArray().get(7).getWeatherArray().get(0).getId();
                long id2 = threeHourForecast.getThreeHourWeatherArray().get(15).getWeatherArray().get(0).getId();
                long id3 = threeHourForecast.getThreeHourWeatherArray().get(31).getWeatherArray().get(0).getId();
                long id4 = threeHourForecast.getThreeHourWeatherArray().get(35).getWeatherArray().get(0).getId();

                String icday1 = updateicon.updateWeatherIcon(id1);
                switch (icday1) {
                    case "lightrain":
                        mIcon1.setImageResource(R.drawable.lightrain);
                        break;

                    case "thunder":
                        mIcon1.setImageResource(R.drawable.storms);
                        break;

                    case "heavyrain":
                        mIcon1.setImageResource(R.drawable.rain);
                        break;

                    case "snow":
                        mIcon1.setImageResource(R.drawable.snows);
                        break;

                    case "sunny":
                        mIcon1.setImageResource(R.drawable.sunny);
                        break;

                    case "cloud":
                        mIcon1.setImageResource(R.drawable.cloudy);
                        break;

                }

                String icday2 = updateicon.updateWeatherIcon(id2);
                switch (icday2) {
                    case "lightrain":
                        mIcon2.setImageResource(R.drawable.lightrain);
                        break;

                    case "thunder":
                        mIcon2.setImageResource(R.drawable.storms);
                        break;

                    case "heavyrain":
                        mIcon2.setImageResource(R.drawable.rain);
                        break;

                    case "snow":
                        mIcon2.setImageResource(R.drawable.snows);
                        break;

                    case "sunny":
                        mIcon2.setImageResource(R.drawable.sunny);
                        break;

                    case "cloud":
                        mIcon2.setImageResource(R.drawable.cloudy);
                        break;

                }

                String icday3 = updateicon.updateWeatherIcon(id3);
                switch (icday3) {
                    case "lightrain":
                        mIcon3.setImageResource(R.drawable.lightrain);
                        break;

                    case "thunder":
                        mIcon3.setImageResource(R.drawable.storms);
                        break;

                    case "heavyrain":
                        mIcon3.setImageResource(R.drawable.rain);
                        break;

                    case "snow":
                        mIcon3.setImageResource(R.drawable.snows);
                        break;

                    case "sunny":
                        mIcon3.setImageResource(R.drawable.sunny);
                        break;

                    case "cloud":
                        mIcon3.setImageResource(R.drawable.cloudy);
                        break;

                }

                String icday4 = updateicon.updateWeatherIcon(id4);
                switch (icday4) {
                    case "lightrain":
                        mIcon4.setImageResource(R.drawable.lightrain);
                        break;

                    case "thunder":
                        mIcon4.setImageResource(R.drawable.storms);
                        break;

                    case "heavyrain":
                        mIcon4.setImageResource(R.drawable.rain);
                        break;

                    case "snow":
                        mIcon4.setImageResource(R.drawable.snows);
                        break;

                    case "sunny":
                        mIcon4.setImageResource(R.drawable.sunny);
                        break;

                    case "cloud":
                        mIcon4.setImageResource(R.drawable.cloudy);
                        break;

                }



                mTemp1.setText(String.valueOf((int)Math.rint(t1) + "°C"));
                mTemp2.setText(String.valueOf((int)Math.rint(t2)+ "°C"));
                mTemp3.setText(String.valueOf((int)Math.rint(t3)+ "°C"));
                mTemp4.setText(String.valueOf((int)Math.rint(t4)+ "°C"));


            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }




    // Updates the information shown on screen.
    private void updateUI(WeatherDataModel weather) {
        mTemperatureLabel.setText(weather.getTemperature());
        mCityLabel.setText(weather.getCity());
        myweb.setVisibility(View.VISIBLE);


        // Update the icon based on the resource id of the image in the drawable folder.
        String weathernow = weather.getIconName();
        switch (weathernow) {
            case "lightrain": myweb.loadUrl("file:///android_asset/index2.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
            break;

            case "thunder": myweb.loadUrl("file:///android_asset/thunderstorm.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
                break;

            case "heavyrain": myweb.loadUrl("file:///android_asset/heavyrain.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
                break;

            case "snow": myweb.loadUrl("file:///android_asset/snow.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
                break;

            case "sunny": myweb.loadUrl("file:///android_asset/sunny.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
                break;

            case "cloud": myweb.loadUrl("file:///android_asset/cloudy.html");
                tospeak = "Its " + weather.getTemperature() + " at " + weather.getCity() + "with possibility of " + weathernow;
                break;
        }
        t1.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null, null);

    }


    // Freeing up resources when the app enters the paused state.
    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }

}


