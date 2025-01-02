
// Import the Swing library
import javax.swing.*;

public class AppLauncher
{
    public static void main(String[] args) {

        // Calling invokeLater is useful for Swing GUI because it makes updates to the GUI more thread safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Display our weather app
                WeatherAppGUI weatherAppGUI = new WeatherAppGUI();
                weatherAppGUI.setVisible(true);

//                System.out.println(WeatherApp.getLocationData("Tokyo"));
//
//                System.out.println(WeatherApp.getCurrentTime());

                System.out.println(WeatherApp.getWeatherData("Edmonton"));

            }
        });
    }
}
