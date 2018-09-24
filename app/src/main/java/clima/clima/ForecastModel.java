package clima.clima;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastModel {

    private String mFTemperature;
    private String mFIconName;
    private int mFCondition;


    // Create a WeatherDataModel from a JSON.
    // We will call this instead of the standard constructor.
    public static ForecastModel fromJson(JSONObject jsonObject) {

        // JSON parsing is risky business. Need to surround the parsing code with a try-catch block.
        try {
            ForecastModel forecastData = new ForecastModel();
            forecastData.mFCondition = jsonObject.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getInt("id");
            forecastData.mFIconName = updateWeatherIcon(forecastData.mFCondition);

            double tempResult = jsonObject.getJSONArray("list").getJSONObject(1).getDouble("temp")- 273.15;
            int roundedValue = (int) Math.rint(tempResult);

            forecastData.mFTemperature = Integer.toString(roundedValue);

            return forecastData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "thunder";
        } else if (condition >= 300 && condition < 500) {
            return "lightrain";
        } else if (condition >= 500 && condition < 600) {
            return "heavyrain";
        } else if (condition >= 600 && condition <= 700) {
            return "snow";
        } else if (condition >= 701 && condition <= 771) {
            return "cloud";
        } else if (condition >= 772 && condition < 800) {
            return "thunder";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloud";
        } else if (condition >= 900 && condition <= 902) {
            return "cloud";
        } else if (condition == 903) {
            return "snow";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "cloud ";
        }

        return "dunno";
    }

    // Getter methods for temperature, city, and icon name:

    public String getTemperature() {
        return mFTemperature + "°C";
    }

    public String getIconName() {
        return mFIconName;
    }
}
