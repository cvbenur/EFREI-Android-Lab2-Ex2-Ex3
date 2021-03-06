package fr.rnabet.flickrapp.async;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.rnabet.flickrapp.adapter.MyAdapter;


public class AsyncFlickrJSONDataForList extends AsyncTask<String, Void, JSONObject> {

    // Attributes
    private final MyAdapter _adapter;


    // Ctor
    public AsyncFlickrJSONDataForList (MyAdapter adapter) {
        this._adapter = adapter;
    }


    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        JSONObject json = new JSONObject();

        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String s = readStream(in);
                Log.i("NABET", s);

                // Strip retrieved JSON String of unnecessary Strings and cast it to JSON Object
                json = new JSONObject(s.subSequence(15, s.length()-1).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }



    // Decode input stream as String
    private String readStream(InputStream in) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while(i != -1) {
                bo.write(i);
                i = in.read();
            }

            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }




    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        // Build url String from destructured JSONObject provided in response
        try {
            JSONArray array = jsonObject.getJSONArray("items");

            for (int i=0; i<array.length(); i++) {  // Iterating over JSON Array

                // Build each url String
                String url = ((JSONObject) array.get(i))
                        .getJSONObject("media")
                        .getString("m");

                // Add built URL to adapter
                this._adapter.add(url);
                Log.i("NABET", String.format("Adding url '%s' to adapter.", url));
            }

            this._adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
