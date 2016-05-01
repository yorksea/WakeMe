package com.amberyork.wakeme;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yorksea on 4/26/16.
 */
class PostClient extends AsyncTask<String, Void, String> {
    public String doInBackground(String... params) {

        String TAG = "PostClient";

        String trigger = params[0];
        String maker_key = params[1];

        //build the url based on the desired trigger
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("maker.ifttt.com")
                .appendPath("trigger")
                .appendPath(trigger)
                .appendPath("with")
                .appendPath("key")
                .appendPath(maker_key);

        String urlString = builder.build().toString();
        try {
            // Stuff variables
            Log.d(TAG, urlString);
            URL url = new URL(urlString.toString());


            String param = "test=testing";
            Log.d(TAG, "param:" + param);

            // Open a connection using HttpURLConnection
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.connect();

            BufferedReader in = null;

            if (con.getResponseCode() != 200) {

                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.d(TAG, "!=200: " + in);
                //ok, so it was a bad request, what code is it?
                Log.d(TAG, "Response code: " + con.getResponseCode());
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d(TAG, "POST request send successful: " + in);

            };

        } catch (MalformedURLException e) {
            Log.d(TAG,"The URL is not valid.");
            Log.d(TAG,e.getMessage());

        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
            return null;
        }
        // Set null and we´e good to go
        return null;
    }
}