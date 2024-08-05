**[English](STEPS.md)** ∙ [Русский язык](STEPS.ru.md)
___
## Implementation

**To implement this application in `Java`, uses the `Jackson` library to parse the JSON file and process the data accordingly.
The program is structured as follows:**

- **Read and parse the JSON file** to get the list of tickets.
- **Calculate the minimum flight time** for each carrier between Vladivostok and Tel-Aviv.
- **Calculate the difference between the average price and the median price** for flights between Vladivostok and Tel-Aviv.
- **Outputs the flight infos** in console.

### Step-by-step Implementation:

1. **Define the Data Classes (DTO):**
- Create a `FlightTicket` class to represent each ticket. 
- Create a `FlightTicketWrapper` class to hold the list of tickets.
2. **Process the Flight Data**
- Create a `FlightDataService` class to process the flight data.
- Use a `FlightDataFilter` class to filter tickets by cities.
3. **Parse the JSON File:**
- Create a `FlightDataReader` class to read and parse `ticket.json`.
- Use Jackson's `ObjectMapper` to read and parse `ticket.json`.
4. **Calculate the minimum Flight Time:**
- Create a `FlightTimeProvider` class to provide minimum flight time
- Iterate through the tickets, calculate the flight time for each ticket, and track the minimum time for each carrier.
5. **Calculate the Average and Median Prices:**
- Create a `FlightPriceProvider` class to provide the difference between the average and median prices.
- Collect all prices for flights between Vladivostok and Tel-Aviv.
- Calculate the average price.
- Calculate the median price.
- Find the difference between the average and median prices.
6. **Outputs the flight information in console:**
- Create a `FlightReportService` class to display minimum flight time and difference in console.
___
[![Intro](https://img.shields.io/badge/Intro-en-blue.svg)](https://github.com/maxshushanikov/flightinfoapp/blob/main/README.md)
[![use](https://img.shields.io/badge/How--to-use-red.svg)](https://github.com/maxshushanikov/flightinfoapp/blob/main/USAGE.md)
