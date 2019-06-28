package com.codepath.flicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.flicks.models.Config;
import com.codepath.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //constants
    //base url for api always going to be the same

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //paramater for api key
    public final static String API_KEY_PARAM = "api_key";

    public final static String TAG = "MainActivity";

    AsyncHttpClient client;


    //the currently playing movies
    //note that we created a model - Movie and now are making an arrayList of Type Movie
    ArrayList<Movie> movies;
    //its only declares - not initialized yet
    RecyclerView rvMovies;
    movieAdapter adapter;
    Config config;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new AsyncHttpClient();
        //initializing the list
        movies = new ArrayList<>();
        getConfiguration();
        adapter = new movieAdapter(movies);
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

    }

    //Calling on the Api to populate the lise
    private void getNowPlaying(){
        String url = API_BASE_URL + "/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22";
        // this is the endpoint given by the apo
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //on success of the call we will get the movoie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i =0 ; i<results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //adding movies to the list
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies",results.length() ));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               logError("Failed to get data from playing endpoint", throwable, true);
            //set to true so that a Message will display
            }
        });
    }

    private void getConfiguration() {
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               //get the base url
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and poster size %s",config.getImageBaseUrl(), config.getPosterSize() ));
                    //getting the new movies
                    adapter.setConfig(config);


                    //pass to adapter
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration",throwable, true);
            }
        });
    }
//handle errors log and alert user
    private void logError(String message, Throwable error, boolean alertUSer){
        Log.e(TAG, message, error);
        //loggint to the console
        //alert to avoid silent errors
        if(alertUSer){
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
        }
    }
}