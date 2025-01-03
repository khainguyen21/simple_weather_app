# WeatherApp  

WeatherApp is a Java-based application using IntelliJ IDEA with a SWING graphical user interface (GUI) that allows users to fetch and display weather information for a specified location. The app interacts with weather APIs to retrieve real-time data such as temperature, weather code, humidity, and wind speed from Open-Meteo website.

## Features
- **User-friendly GUI**:
  - Centralized window for convenience.
  - Non-resizable to maintain layout consistency.
  - Customizable component alignment for better readability.

- **API Integration**:
  - Communicates with weather APIs to retrieve longitude and latitude based on the user's location input.
  - Fetches detailed weather information using the obtained coordinates.

## Components
### 1. **WeatherAppGUI.java**  
The front-end GUI is implemented by extending the `JFrame` class. It includes the following features:  
- **GUI Window Management**:
  - `setDefaultCloseOperation(EXIT_ON_CLOSE);` ensures the program terminates when the window is closed.
  - `setSize()` and `setLocationRelativeTo(null);` position the GUI window at the center of the screen.
  - `setLayout()` manually positions components within the window.
  - `setResizable(false);` disables resizing for a consistent user experience.
  
- **Component Positioning and Alignment**:
  - `setBounds();` defines the size and location of components.
  - `setHorizontalAlignment();` aligns the content of components like `JLabel`, `JButton`, and `JTextField`.
  - `setTextAlignment();` adjusts text alignment within `JTextArea` and `JTextPane`.

---

### 2. **WeatherApp.java**  
Handles back-end operations, including API communication and data processing. Key functionalities include:  
#### API Request Workflow
1. **Fetch Location Data**:
   - Accepts user input for location and builds an API URL string.
   - Opens a connection using `HttpURLConnection` and sets the request method to `GET`.
   - Sends the request with `.connect()` and verifies response status using `.getResponseCode()`.

2. **Process Server Response**:
   - Reads the response and loops line-by-line using `Scanner` and stores it in a `StringBuilder`.
   - Closes the `Scanner` and disconnects the connection using `.disconnect()`.
   - Parses the raw JSON string into a structured `JSONObject` using `JSONParser`.

3. **Retrieve Coordinates**:
   - Accesses `JSONArray` data for location details (longitude and latitude) using `.get("key property")` and `.get(index)`.

4. **Fetch Weather Data**:
   - Builds a new API URL with obtained longitude and latitude.
   - Opens a new connection using `HttpURLConnection` and sets the request method to `GET`.
   - Reads and processes the server response in the same manner as before.
   - Parses weather data into a `JSONObject`.

#### Data Parsing and Formatting
- Extracts key weather details (e.g., temperature, weather code, humidity, wind speed) from the JSON response.
- Matches server-provided time data with the user's current time using `LocalDateTime` and loops through the `JSONArray` to find the correct data index.
- Packages weather details into a new `JSONObject` for easy display.

## Usage
1. **Run the Application**:  
   Launch the program to open the WeatherApp GUI.
2. **Input Location**:  
   Enter a location in the provided text field.
3. **Retrieve Weather Data**:  
   Click the "Search" button to fetch and display the weather information.
4. **Convert from Celsius to Farhenite**:
   Click the "Celsius" button to convert from °C to °F, and vice versa.

## Dependencies
- **JSON Parsing**: Requires a JSON parsing library such as `org.json.simple` or `com.fasterxml.jackson`.
- **Networking**: Utilizes `HttpURLConnection` for API communication.
