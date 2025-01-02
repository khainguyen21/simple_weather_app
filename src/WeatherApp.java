// retrieve weather data from API - this backend logic will fetch the latest weather
// data from the external API and return it. The GUI will display this data to user

import jdk.jshell.SourceCodeAnalysis;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/*
 * This is WeatherApp.java to use API to communicate with server and obtain the server's response.
 * Get the location from user's input, adjust the string url to match as API url, use url to open connection
 * and cast as HttpURLConnection, and set the requested method to ("GET") means we only retrieve data from server and .connect()
 * Use .getResponseCode() to check if status is good
 * Use Scanner to read each line in server's response and StringBuilder to store the server's response
 * Close scanner, .disconnect() the HttpURLConnection and cast StringBuilder to string
 * Use JSONParser to parse raw JSONString from string builder to structured JSONObject
 * Use get.("key property") to obtain JSONArray and then use .get(index) to access first element in JSONArray to obtain
 * longitude and latitude
 *
 * Use longitude and latitude to adjust the string url, then use url to open the connection and cast as HttpURLConnection
 * setRequestMethod to ("GET"), check if connection.getResponseCode() to check if status is good
 * Use Scanner to read each line in .getInputStream() and StringBuilder to store the server's response
 * Close scanner and .disconnect() the HttpURLConnection and cast StringBuilder to String
 * Use JSONParser to cast raw JSONString into structured JSONObject
 * Access to JSONArray the time, use LocalDateTime to get user's time, format it
 * Loop through JSONArray of time, return the index of time that matched with current time
 * Use index to access the correct element in array to get temperature, weather code, humidity, wind speed
 * Create JSONObject to put temperature, weather code, humidity, wind speed into.
 *
 */
public class WeatherApp
{
    // Fetch weather data for given location
    public static JSONObject getWeatherData(String locationName)
    {
        // Get location coordinates using the geolocation API
        JSONArray locationData = getLocationData(locationName);

        // locationData.get(0) will give object then need to cast to JSONObject to retrieve the values
        // using the .get("json_property")
        // Extract latitude and longitude from data and cast to JSONObject
        JSONObject location = (JSONObject) locationData.get(0);

        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FLos_Angeles";

        try{
            // Call API and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check if status code is stable
            if (conn.getResponseCode()!= 200)
            {
                System.out.println("Could not make a connection!");
                return null;
            }

            else
            {
                // Define string builder to add results from server's response
                StringBuilder resultsJson = new StringBuilder();

                // Use scanner to read server's response line by line
                Scanner scanner = new Scanner(conn.getInputStream());

                // Check if server still have responses
                while (scanner.hasNext())
                {
                    // Add message into the string builder
                    resultsJson.append(scanner.nextLine());
                }

                // Close scanner
                scanner.close();

                // Close url connection
                conn.disconnect();

                // Tool to convert raw JSON string into structured JSONObject
                JSONParser parser = new JSONParser();

                // Convert raw JSON string into JSONObject
                JSONObject resultJSONObject = (JSONObject) parser.parse(resultsJson.toString());

                // Retrieve hourly data
                JSONObject hourly = (JSONObject) resultJSONObject.get("hourly");

                // We want to get the current hour's data
                // so we need to get the index of our current hour
                JSONArray timeData = (JSONArray) hourly.get("time");

                // Get the index of current time in time data
                int index = findIndexOfCurrentTime(timeData);

                // Obtain the list of object of temperature data
                JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
                // Obtain current temperature in temperature data
                double temperature = (double) temperatureData.get(index);

                // Obtain the weather code data
                JSONArray weatherCodeData = (JSONArray) hourly.get("weather_code");
                // Obtain the weather condition
                String weatherCondition = convertWeatherCode((long)(weatherCodeData.get(index)));

                // Obtain humidity
                JSONArray humidityData = (JSONArray) hourly.get("relative_humidity_2m");
                long humidity = (long) humidityData.get(index);

                // Obtain the wind speed
                JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
                double windSpeed = (double) windSpeedData.get(index);

                // Create JSONObject with current data that we're going to access in frontend

                JSONObject weatherData = new JSONObject();

                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("windSpeed", windSpeed);

                return weatherData;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    // Convert the weather code into weather condition
    private static String convertWeatherCode(long weatherCode)
    {
        String weaterCodition = "";

        if (weatherCode == 0L)
        {
            // Clear
            weaterCodition = "Clear";
        }
        else if (weatherCode >= 1L && weatherCode <= 3L)
        {
            // Cloudy
            weaterCodition = "Cloudy";
        }
        else if (weatherCode == 45L || weatherCode ==  48L)
        {
            // Rain
            weaterCodition = "Fog";
        }
        else if (weatherCode == 51L || weatherCode == 53L || weatherCode == 55L)
        {
            // Snow
            weaterCodition = "Drizzle";
        }
        else if (weatherCode >= 56L && weatherCode <= 57L)
        {
            weaterCodition = "Freezing Drizzle";
        }

        else if (weatherCode == 61L || weatherCode == 63L || weatherCode == 65L)
        {
            weaterCodition = "Rain";
        }

        else if (weatherCode == 66L || weatherCode == 67L)
        {
            weaterCodition = "Freezing Rain";
        }

        else if (weatherCode == 71L || weatherCode == 73L || weatherCode == 75L)
        {
            weaterCodition = "Snow Fall";
        }

        else if (weatherCode == 77L)
        {
            weaterCodition = "Snow grains";
        }

        else if (weatherCode >= 80L && weatherCode <= 82L)
        {
            weaterCodition = "Rain Showers";
        }

        else if (weatherCode == 85L || weatherCode == 86L)
        {
            weaterCodition = "Snow Showers";
        }

        else if (weatherCode == 95L)
        {
            weaterCodition = "Thunderstorm";
        }

        else if (weatherCode == 96L || weatherCode == 99L)
        {
            weaterCodition = "Thunderstorm with slight and heavy hail";
        }

        return weaterCodition;
    }


    // Retrieves geographic coordinates for given location name
    public static JSONArray getLocationData(String locationName)
    {
        // Replace any whitespace in locationName to "+" to match API's request format
        locationName = locationName.replaceAll(" ", "+");

        // Build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try{
            // Call API and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check response status
            // 200 means successful connection
            if (conn.getResponseCode() != 200)
            {
                System.out.println("Error: Could not connect to API ");
                return null;
            }
            else
            {
                // Store the API results
                StringBuilder resultJson = new StringBuilder();

                // Get the input stream and read it using Scanner
                Scanner scanner = new Scanner(conn.getInputStream());

                // Read and store json data into string builder
                while(scanner.hasNext())
                {
                    resultJson.append(scanner.nextLine());
                }

                // Close scanner
                scanner.close();

                // Close url connection
                conn.disconnect();

                // This tool from org.json.simple.parser takes the raw JSON string and converts it into a JSONObject
                JSONParser parser = new JSONParser();

                // Cast StringBuilder to String
                // Takes the raw JSON string and turns it into a structured JSONObject.
                // Because parser.parse() returns an Object, and we want to cast it's as JSONObject.
                JSONObject resultsJsonObj = (JSONObject) parser.parse(resultJson.toString());

                // Access the values from the parsed JSONObject
                // Cast into the JSONArray
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

                return locationData;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // could not capture the location
        return null;
    }


    // Tries to make connection from url and set requested method to GET
    private static HttpURLConnection fetchApiResponse(String urlString)
    {
        try{
            // Attempt to create connection
            URL url = new URL(urlString);

            // URLConnection returned by openConnection() is cast to HttpURLConnection to use GET and POST
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Used to set the request method. Default is GET
            conn.setRequestMethod("GET");

            // Connect to our API
            conn.connect();
            return conn;

        }catch (IOException e){
            e.printStackTrace();
        }

        // could not make a connection
        return null;
    }


    public static int findIndexOfCurrentTime(JSONArray timeList)
    {
        // Obtain the current time
        String currentTime = getCurrentTime();

        // Index of list of object in data
        int index = 0;

        // iterate through the time list and see which one matches our current time
        for(Object time : timeList)
        {
            // Check if the current time is same as time data
            if(time.toString().equalsIgnoreCase(currentTime))
            {
                // Return the index position in list of objects
                return index;
            }

            // Otherwise move to next object in the data
            index++;
        }

//        // Obtain the current time
//        String currentTime = getCurrentTime();
//
//        // Iterate through the whole time data
//        for (int i = 0; i < timeList.size(); i++)
//        {
//            // Obtain each element in time data
//            String time = (String) timeList.get(i);
//
//            // Check if current time is same as time in data
//            if (currentTime.equalsIgnoreCase(time))
//            {
//                // return position
//                return i;
//            }
//        }
        return 0;
    }

    public static String getCurrentTime()
    {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Set format of time as 2024-12-17T00:00 (this is how it reads in API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // Format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

}
