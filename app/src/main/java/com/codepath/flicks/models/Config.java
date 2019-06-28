package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {


    //base url for landing inames
    String imageBaseUrl;
    //poster size
    String posterSize;
    String backdropSize;
    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        posterSize = posterSizeOptions.optString(3,"w342");
        backdropSize = backdropSizeOptions.optString(3,"w500");

    }

    //help method
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path);

    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
    public String getBackdropSize() {
        return backdropSize;
    }
}


