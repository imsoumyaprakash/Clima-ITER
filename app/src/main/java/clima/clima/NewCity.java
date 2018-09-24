package clima.clima;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class NewCity extends AppCompatActivity {
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
    EditText mCityLabel;
    TextView mTemperatureLabel;
    WebView myweb;
    TextView mDate;
    TextView mTemp1, mTemp2, mTemp3, mTemp4;
    TextView mDay1, mDay2, mDay3, mDay4;
    private String longitude;
    private String latitude;
    ImageView mIcon1, mIcon2, mIcon3, mIcon4;
    String getcity;


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
    private double la, lo;
    private Swipe swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

        mCityLabel = findViewById(R.id.entercity);
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
        mCityLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityLabel.setFocusableInTouchMode(true);
            }
        });



        mCityLabel.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button


                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    mCityLabel.setFocusable(false);
                    mCityLabel.setBackground(null);
                    getcity = mCityLabel.getText().toString();
                    if (!getcity.isEmpty()) {
                        getWeatherForNewCity(getcity);
                    } else {
                        getcity = "bhubaneswar";

                    }
                    return true;
                }
                return false;
            }
        });

        swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {


                return true;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Intent intent=new Intent(NewCity.this,Current_weather.class);
                startActivity(intent);
                return true;
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


    public String getNameOfDayOfWeek(int noOfDaysFromToday) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, noOfDaysFromToday);
        String name = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        return name;
    }


    // Callback received when a new city name is entered on the second screen.
    // Checking request code and if result is OK before making the API call.

    // Configuring the parameters when a new city has been entered:
    private void getWeatherForNewCity(String city) {
        helper.getCurrentWeatherByCityName(city, new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Double temp = currentWeather.getMain().getTemp();
                mTemperatureLabel.setText(String.valueOf((int) Math.rint(temp) + "°C"));
                long id = currentWeather.getWeatherArray().get(0).getId();
                String cityid = updateicon.updateNewCityIcon(id);
                myweb.setVisibility(View.VISIBLE);

                switch (cityid) {
                    case "lightrain":
                        myweb.loadUrl("file:///android_asset/index2.html");
                        break;

                    case "thunder":
                        myweb.loadUrl("file:///android_asset/thunderstorm.html");
                        break;

                    case "heavyrain":
                        myweb.loadUrl("file:///android_asset/heavyrain.html");
                        break;

                    case "snow":
                        myweb.loadUrl("file:///android_asset/snow.html");
                        break;

                    case "sunny":
                        myweb.loadUrl("file:///android_asset/sunny.html");
                        break;

                    case "cloud":
                        myweb.loadUrl("file:///android_asset/cloudy.html");
                        break;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        helper.getThreeHourForecastByCityName(city, new OpenWeatherMapHelper.ThreeHourForecastCallback() {
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


                mTemp1.setText(String.valueOf((int) Math.rint(t1) + "°C"));
                mTemp2.setText(String.valueOf((int) Math.rint(t2) + "°C"));
                mTemp3.setText(String.valueOf((int) Math.rint(t3) + "°C"));
                mTemp4.setText(String.valueOf((int) Math.rint(t4) + "°C"));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


}
