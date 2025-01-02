import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*
 * Create the WeatherAppGUI.java inherit from JFrame class, to get the functionality to add components to front end
 * setDefaultCloseOperation(EXIT_ON_CLOSE); to terminate the GUI when hit the close button
 * setSize() to set size of GUI, setLocationRelativeTo(null); to set GUI in center of screen
 * setLayout() to manually position our components within GUI, setResizable(false); to disable minimize button
 * setBounds(); to set size and location of components within GUI
 * setHorizontalAlignment() affects the entire component's content like JLabel, JButton, and JTextField.
 * while setTextAlignment() affects only the text paragraphs within a text component like JTextArea and JTextPane
 */
public class WeatherAppGUI extends JFrame
{

    private JSONObject weatherData;

    public WeatherAppGUI()
    {
        // Set up GUI and add title
        super("Weather App");

        // Terminate GUI when hit closed (X) button
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the size of GUI (in pixels)
        setSize(450, 650);

        // Load GUI in center of the screen
        setLocationRelativeTo(null);

        // Make our layout manager null to manually position our components within our gui
        setLayout(null);

        // Prevent any resize of GUI
        setResizable(false);

        // Method to add all components to GUI
        addGUIComponents();

    }

    private void addGUIComponents()
    {
        // Search field
        JTextField searchTextField = new JTextField();

        // Set the location and size of search field
        searchTextField.setBounds(15, 15, 351, 45);

        // Change the font style and size of text
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

        // This allows you to listen for focus events, such as when the component gains or loses focus.
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            // When the user click on the text field
            public void focusGained(FocusEvent e) {

                // Check if the text field have "Search here..." text
                if (searchTextField.getText().equals("Search here..."))
                {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.BLACK);
                }
            }
            @Override
            // When the user click away from the text field
            public void focusLost(FocusEvent e) {

                // Check if the text field is empty
                if (searchTextField.getText().isEmpty())
                {
                    searchTextField.setText("Search here...");
                    searchTextField.setForeground(Color.gray);
                }
            }
        });
        // Add search bar to GUI
        add(searchTextField);

        // Add weather condition icon
        JLabel weatherConditionImage = new JLabel(LoadImage("src/images/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);


        // Add temperature text as label
        JLabel temperatureText = new JLabel("0°C");

        // Set location temperature text and set font
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        // Center temperature text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);


        // Add weather condition description as label
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 20));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // Add humidity image as label
        JLabel humidityImage = new JLabel(LoadImage("src/images/humidity.png"));
        humidityImage.setBounds(15, 500 , 74, 66);
        add(humidityImage);

        // Add humidity text as label
        JLabel humidityText = new JLabel("<html> <b>Humidity</b> 0% </html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);
        
        
        // Add windspeed image
        JLabel windspeedImage = new JLabel(LoadImage("src/images/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        // Add windspeed text
        JLabel windspeedText = new JLabel("<html> <b> Windspeed </b> 0km/h </html>");
        windspeedText.setBounds(310, 500, 95, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);


        // Test for search button
//        ImageIcon searchIcon = new ImageIcon("src/images/search.png");
//
//        JButton searchButton = new JButton(searchIcon);

        // Search button
        JButton searchButton = new JButton(LoadImage("src/images/search.png"));

        // change the cursor to a hand cursor when hovering over search button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Set the locations and size of search button
        searchButton.setBounds(375, 13, 47, 45);


        // Change the degree temperature
        JButton degreeButton = new JButton();
        degreeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        degreeButton.setBounds(340, 100, 100, 45);
        degreeButton.setText("Celsius");

        // Add functionality
        degreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (degreeButton.getText().equalsIgnoreCase("Celsius"))
                {
                    degreeButton.setText("Fahrenheit");
                    String temperatureStr =  temperatureText.getText().replaceAll("°C", "");
                    double temperature = Double.parseDouble(temperatureStr);
                    double tempFahrenheit = (temperature * 9/5) + 32;
                    temperatureText.setText(Math.round(tempFahrenheit) + "°F");
                }
                else if (degreeButton.getText().equalsIgnoreCase("Fahrenheit"))
                {
                    degreeButton.setText("Celsius");
                    String temperatureStr =  temperatureText.getText().replaceAll("°F", "");
                    double temperature = Double.parseDouble(temperatureStr);
                    double tempCelsius = (temperature - 32) * 5/9;
                    temperatureText.setText(Math.round(tempCelsius) + "°C");
                }
            }
        });


        // Delay searchButton for the next press by x milliseconds (2000 ms = 2 s)
        searchButton.setMultiClickThreshhold(2000);

        // Add the functionalities after user hit search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Set the degree button to Celsius
                degreeButton.setText("Celsius");

                // Get the location from user's input
                String userInput = searchTextField.getText();

                // Validate input - remove white space
                // If user input a bunch of whitespace, then this if statement will catch that
                // replace all \\s (is whitespace character) with empty string
                if (userInput.replaceAll("\\s", "").length() <= 0)
                {
                    return;
                }

                // Retrieve weather data from WeatherApp.java
                weatherData = WeatherApp.getWeatherData(userInput);

                // Obtain the weather condition
                String weatherCondition = (String) weatherData.get("weather_condition");

                // Update weather image base on weather condition
                switch (weatherCondition)
                {
                    case "Clear":
                        weatherConditionImage.setIcon(LoadImage("src/images/Clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(LoadImage("src/images/Cloudy.png"));
                        break;
                    case "Drizzle":
                        weatherConditionImage.setIcon(LoadImage("src/images/Drizzle.png"));
                        break;
                    case "Fog":
                        weatherConditionImage.setIcon(LoadImage("src/images/Fog.png"));
                        break;
                    case "Freezing Drizzle":
                        weatherConditionImage.setIcon(LoadImage("src/images/FreezingDrizzle.png"));
                        break;
                    case "Freezing Rain":
                        weatherConditionImage.setIcon(LoadImage("src/images/FreezingRain.png"));
                        break;
                    case "Partly Cloudy":
                        weatherConditionImage.setIcon(LoadImage("src/images/PartlyCloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(LoadImage("src/images/Rain.png"));
                        break;
                    case "Rain Showers":
                        weatherConditionImage.setIcon(LoadImage("src/images/RainShowers.png"));
                        break;
                    case "Snow Grains":
                        weatherConditionImage.setIcon(LoadImage("src/images/Snow Grains.png"));
                        break;
                    case "Snow Fall":
                        weatherConditionImage.setIcon(LoadImage("src/images/SnowFall.png"));
                        break;
                    case "Snow Showers":
                        weatherConditionImage.setIcon(LoadImage("src/images/SnowShowers.png"));
                        break;
                    case "Thunderstorm":
                        weatherConditionImage.setIcon(LoadImage("src/images/Thunderstorm.png"));
                        break;
                    case "Thunderstorm with slight and heavy hail":
                        weatherConditionImage.setIcon(LoadImage("src/images/ThunderstormW.png"));
                        break;
                }

                // Update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(Math.round(temperature) + "°C");

                // Update weather description text
                weatherConditionDesc.setText(weatherCondition);

                // Update the humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html> <b>Humidity</b> " + humidity + "%</html>");

                // Update the wind speed
                double windspeed = (double) weatherData.get("windSpeed");
                windspeedText.setText("<html> <b> Windspeed </b>" + windspeed + "km/h </html>");
            }
        });

        // Add search button to GUI
        //add(searchButton);

        // Add degree button to GUI
        add(degreeButton);

    }

    // used to create images in GUI
    private ImageIcon LoadImage(String resourcePath)
    {
        try
        {
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // return ImageIcon object
            return new ImageIcon(image);
        }

        // If file doesn't exist or cannot be loaded, IOException is caught
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Could not load image");
        return null;
    }
}
