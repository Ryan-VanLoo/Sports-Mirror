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

    public QuoteOfTheDay(){
        urlString = "https://zenquotes.io/api/quotes";
        getNewQuote();
    }

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
