package com.example.oliveiras.findpic.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oliveiras on 2/27/15.
 */
public class ImageResult {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    //create a constructor to create image result (raw json item)
    public ImageResult(JSONObject json) {
        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //gets the json and puts it into an array format
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i=0; i<array.length(); i++) {
            try{
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
