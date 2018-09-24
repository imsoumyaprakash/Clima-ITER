package clima.clima;

public class updateicon {


    public static String updateWeatherIcon(long condition) {

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

    public static String updateNewCityIcon(long condition) {

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
}
