package org.sportsMirror;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class QuoteOfTheDay {
    public String quote;
    public String author;
    private String urlString;
    private ArrayList<String> quoteArray = new ArrayList<String>();
    private ArrayList<String> authorArray = new ArrayList<String>();

    /*
    * The class constructor initializes the API url from https://zenquotes.io and
    * uses the getNewQuote() function to fetch the Quote of the Day
    */
    public QuoteOfTheDay(){
        urlString = "https://zenquotes.io/api/quotes";
        getNewQuote();
    }

    /*
    This function connects to the API, parses the JSON data,
    and loads the quoteArray and authorArray ArrayLists
    with converted String data from the API.
    For more information on the API, go to: https://docs.zenquotes.io/zenquotes-documentation/
    */
    private void refreshQuoteArrays(){
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            String jsonString = "";

            while((output = bufferedReader.readLine()) != null){
                jsonString = output;
            }
            connection.disconnect();
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonString);
            JSONObject jsonObject;

            for(Object obj : jsonArray){
                jsonObject = new JSONObject((Map) obj);
                quoteArray.add(jsonObject.get("q").toString());
                authorArray.add(jsonObject.get("a").toString());
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    This function gets the first quote and corresponding author from the
    quoteArray and authorArray ArrayList variables and initializes the
    quote and author variables with the String data. It also checks if either
    ArrayList is empty, and if so, uses the refreshQuoteArrays() function
    to reload the arrays with new Quote and Author data.
    */
    public void getNewQuote(){
        if(quoteArray.size() < 1 || authorArray.size() < 1){
            refreshQuoteArrays();
        }
        quote = quoteArray.get(0);
        author = authorArray.get(0);

        quoteArray.remove(0);
        authorArray.remove(0);
    }
}
