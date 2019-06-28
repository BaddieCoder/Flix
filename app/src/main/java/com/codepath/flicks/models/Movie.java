package com.codepath.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    //tracking information for movies
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;


    public Movie(JSONObject object) throws JSONException {
        //getting these fields from the Json object
        //to do this we made a constructor to access the object
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    public String getTitle() {
        return title;
    }

    public  String getBackdropPath(){
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
