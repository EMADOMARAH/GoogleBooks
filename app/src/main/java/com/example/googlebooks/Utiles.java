package com.example.googlebooks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utiles {

    static String title ,author,thumbnail;

    public static List<BookModel> utils(String api) throws Exception
    {
        URL url = createURL(api);
        String json = makeHTTPrequest(url);
        List<BookModel> bookModels = extractDatafromJSON(json);

        return bookModels;
    }

    private static URL createURL(String api) throws MalformedURLException {
        URL url = new URL(api);
        return url;
    }
    private static String makeHTTPrequest(URL url)
    {
        String response = "";
        if (url==null)
        {
            return response;
        }
        HttpURLConnection httpURLConnection  = null;
        InputStream inputStream = null;

        try {
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode()==200)
            {
                inputStream=httpURLConnection.getInputStream();
                response = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (httpURLConnection!=null)
            httpURLConnection.disconnect();

            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null)
                {
                    stringBuilder.append(line);
                    line=reader.readLine();
                }
            }catch (IOException e)
            {
               e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private  static List<BookModel> extractDatafromJSON(String json) throws JSONException
    {
        if (json.isEmpty())
            return null;

        List<BookModel>bk=new ArrayList<>();
        JSONObject root = new JSONObject(json);
        JSONArray items = root.getJSONArray("items");
        for (int i=0;i<items.length();i++)
        {
            JSONObject each_item = items.getJSONObject(i);
            JSONObject vi = each_item.getJSONObject("volumeInfo");
            if (vi.has("title"))
            {
                title=vi.getString("title");
            }else {
                title="no title found";
            }
            if (vi.has("authors"))
            {
                author=vi.getJSONArray("authors").getString(0);
            }else {
                author="no author found";
            }
            if (vi.has("imageLinks"))
            {
                thumbnail=vi.getJSONObject("imageLinks").getString("thumbnail");
            }else {
                thumbnail="";
            }
            BookModel bookModel = new BookModel(title,author,thumbnail);
            bk.add(bookModel);
        }
        return bk;

    }



}
